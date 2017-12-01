package com.android.fisewatchlauncher.prenster.dao;

import android.database.sqlite.SQLiteException;

import com.android.fisewatchlauncher.dao.EaseFriendDao;
import com.android.fisewatchlauncher.entity.dao.EaseFriend;
import com.android.fisewatchlauncher.function.DaoFlagUtils;
import com.android.fisewatchlauncher.manager.DBManager;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.util.List;

/**
 * @author mare
 * @Description:TODO 环信好友数据库管理类
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/5
 * @time 18:08
 */
public class EaseFriendDaoUtils implements GreenDaoChargeRecordImpl<EaseFriend> {

    private EaseFriendDaoUtils() {
    }

    private static class SingletonHolder {
        private static final EaseFriendDaoUtils INSTANCE = new EaseFriendDaoUtils();
    }

    public static EaseFriendDaoUtils instance() {
        return SingletonHolder.INSTANCE;
    }

    private EaseFriendDao friendDao;

    private EaseFriendDao getDao() {
        if (null == friendDao) {
            friendDao = DBManager.instance().getDaoSession().getEaseFriendDao();
        }
        return friendDao;
    }

    @Override
    public long insert(EaseFriend data) {
        long add = getDao().insert(data);
        return add;
    }

    /***
     * 通过city 更新闹钟 相同时间 相同重复周期视为一个
     * @param data
     * @return
     */
    @Override
    public boolean update(EaseFriend data) {
        if (null == data) {
            return false;
        }
        EaseFriend userQuery = null;
        long insertUsersID = -1;
        String className = data.getClass().getSimpleName();
        EaseFriendDao dao = getDao();
        String friendId = data.getFriendId();
        try {
            userQuery = dao.queryBuilder().where(EaseFriendDao.Properties.FriendId.eq(friendId)).build().forCurrentThread().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {//覆盖更新好友信息
            data.setId(userQuery.getId());
            LogUtils.d("开始更新" + className + "数据...");
        } else {
            data.setId(System.currentTimeMillis());
            LogUtils.d("没找到" + className + "的所在数据 开始插入");
        }
        insertUsersID = dao.insertOrReplace(data);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 AlarmInfo " + "的闹钟数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

    /**
     * 从指定ID开始插入好友
     *
     * @param friends
     * @param start
     */
    private void insertFriendsOffset(final List<EaseFriend> friends, final int start) {
        final EaseFriendDao dao = getDao();
        dao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < friends.size(); i++) {
                    EaseFriend friend = friends.get(i);
                    friend.setId((long) (i + start));
                    long insertUsersID = dao.insertOrReplace(friend);
                    boolean isSuccess = insertUsersID >= 0;
                    LogUtils.i("更新 id= " + i + start + " 的闹钟数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
                }
            }
        });
    }


    /**
     * 批量插入闹钟
     *
     * @param friends
     */
    public void updateFriendList(final List<EaseFriend> friends) {
        final EaseFriendDao dao = getDao();
        if (null == friends || friends.isEmpty()) return;
        dao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < friends.size(); i++) {
                    EaseFriend friend = friends.get(i);
                    String friendId = friend.getFriendId();
                    EaseFriend queryInfo;
                    queryInfo = dao.queryBuilder().where(EaseFriendDao.Properties.FriendId.eq(friendId)).build().forCurrentThread().unique();
                    if (null != queryInfo) {
                        friend.setId(queryInfo.getId());
                    } else {
                        friend.setId(System.currentTimeMillis());//以当前时间戳作为id
                    }
                    long insertUsersID = dao.insertOrReplace(friend);
                    boolean isSuccess = insertUsersID >= 0;
                    LogUtils.i("更新 id= " + i + " 的数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
                }
            }
        });
    }

    @Override
    public void deleteAll() {
        getDao().deleteAll();
    }

    @Override
    public void deleteWhere(long id) {
        getDao().deleteByKey(id);
    }

    @Override
    public List<EaseFriend> selectAll() {
        return getDao().loadAll();
    }

    @Override
    public List<EaseFriend> selectWhere(EaseFriend data) {
        return null;
    }

    @Override
    public EaseFriend seelctWhrer(String name) {
        return null;
    }

    @Override
    public List<EaseFriend> selectWhrer(long id) {
        return null;
    }
}
