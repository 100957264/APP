package com.android.fisewatchlauncher.prenster.dao;

import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import com.android.fisewatchlauncher.dao.AlarmInfoDao;
import com.android.fisewatchlauncher.entity.dao.AlarmInfo;
import com.android.fisewatchlauncher.function.DaoFlagUtils;
import com.android.fisewatchlauncher.manager.DBManager;
import com.android.fisewatchlauncher.utils.LogUtils;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import static android.R.attr.repeatMode;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/5
 * @time 18:08
 */
public class AlarmClockDaoUtils implements GreenDaoChargeRecordImpl<AlarmInfo> {
    public static final int REPEAT_MODE_ONCE = 1;
    public static final int REPEAT_MODE_EVERDAY = 2;
    public static final int REPEAT_MODE_WEEK = 3;

    private AlarmClockDaoUtils() {
    }

    private static class SingletonHolder {
        private static final AlarmClockDaoUtils INSTANCE = new AlarmClockDaoUtils();
    }

    public static AlarmClockDaoUtils instance() {
        return SingletonHolder.INSTANCE;
    }

    private AlarmInfoDao settingsDao;

    private AlarmInfoDao getDao() {
        if (null == settingsDao) {
            settingsDao = DBManager.instance().getDaoSession().getAlarmInfoDao();
        }
        return settingsDao;
    }

    @Override
    public long insert(AlarmInfo data) {
        long add = getDao().insert(data);
        return add;
    }

    /***
     * 通过city 更新闹钟 相同时间 相同重复周期视为一个
     * @param data
     * @return
     */
    @Override
    public boolean update(AlarmInfo data) {
        if (null == data) {
            return false;
        }
        String time = data.getTime();
        String repeatDayByWeek = data.getRepeatDayByWeek();
        AlarmInfo userQuery = null;
        long insertUsersID = -1;
        String className = data.getClass().getSimpleName();
        AlarmInfoDao dao = getDao();
        Query<AlarmInfo> query;
        try {
            if (TextUtils.isEmpty(repeatDayByWeek)) {//兼容防走丢闹钟非重复周期闹钟解析
                query = dao.queryBuilder().where(AlarmInfoDao.Properties.Time.eq(time)).build();
            } else {
                query = dao.queryBuilder().where(AlarmInfoDao.Properties.Time.eq(time),
                        AlarmInfoDao.Properties.RepeatDayByWeek.eq(repeatMode)).build();
            }
            userQuery = query.forCurrentThread().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {//覆盖更新闹钟
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
     * 从指定ID开始插入闹钟
     *
     * @param alarmInfos
     * @param start
     */
    private void insertContactorsOffset(final List<AlarmInfo> alarmInfos, final int start) {
        final AlarmInfoDao dao = getDao();
        dao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < alarmInfos.size(); i++) {
                    AlarmInfo alarmInfo = alarmInfos.get(i);
                    alarmInfo.setId((long) (i + start));
                    long insertUsersID = dao.insertOrReplace(alarmInfo);
                    boolean isSuccess = insertUsersID >= 0;
                    LogUtils.i("更新 id= " + i + start + " 的闹钟数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
                }
            }
        });
    }


    /**
     * 批量插入闹钟
     *
     * @param alarmInfos
     */
    public void updateClockList(final List<AlarmInfo> alarmInfos) {
        final AlarmInfoDao dao = getDao();
        if (null == alarmInfos || alarmInfos.isEmpty()) return;
        dao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < alarmInfos.size(); i++) {
                    AlarmInfo alarmInfo = alarmInfos.get(i);
                    String time = alarmInfo.getTime();
                    String repeatMode = alarmInfo.getRepeatDayByWeek();
                    //兼容防走丢的闹钟解析
//                    08:02-1-3-1000001,00:00-0-1,00:00-0-1
//                    14:32-0-3-0111110,10:05-1-3-1010110,17:07-1-3-01111
//                    时间相等并且(重复周期为空或者重复周期相等)
                    AlarmInfo queryInfo;
                    if (TextUtils.isEmpty(repeatMode)) {
                        queryInfo = dao.queryBuilder().where(AlarmInfoDao.Properties.Time.eq(time)).build().unique();
                    } else {
                        queryInfo = dao.queryBuilder().where(AlarmInfoDao.Properties.Time.eq(time),
                                AlarmInfoDao.Properties.RepeatDayByWeek.eq(repeatMode)).build().unique();
                    }
                    if (null != queryInfo) {
                        alarmInfo.setId(queryInfo.getId());
                    } else {
                        alarmInfo.setId(System.currentTimeMillis());//以当前时间戳作为id
                    }
                    long insertUsersID = dao.insertOrReplace(alarmInfo);
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
    public List<AlarmInfo> selectAll() {
        return getDao().loadAll();
    }

    @Override
    public List<AlarmInfo> selectWhere(AlarmInfo data) {
        return null;
    }

    @Override
    public AlarmInfo seelctWhrer(String name) {
        return null;
    }

    @Override
    public List<AlarmInfo> selectWhrer(long id) {
        return null;
    }
}
