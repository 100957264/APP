package com.android.fisewatchlauncher.prenster.dao;

import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.dao.CenterCounterDao;
import com.android.fisewatchlauncher.entity.dao.CenterCounter;
import com.android.fisewatchlauncher.manager.DBManager;
import com.android.fisewatchlauncher.utils.ListUtils;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.util.List;

/**
 * @author mare
 * @Description:TODO 计步翻转数据库辅助类
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/5
 * @time 18:08
 */
public class CenterCounterDaoUtils implements GreenDaoChargeRecordImpl<CenterCounter> {
    private CenterCounterDaoUtils() {
    }

    private static class SingletonHolder {
        private static final CenterCounterDaoUtils INSTANCE = new CenterCounterDaoUtils();
    }

    public static CenterCounterDaoUtils instance() {
        return SingletonHolder.INSTANCE;
    }

    private CenterCounterDao settingsDao;

    private CenterCounterDao getDao() {
        if (null == settingsDao) {
            settingsDao = DBManager.instance().getDaoSession().getCenterCounterDao();
        }
        return settingsDao;
    }

    @Override
    public long insert(CenterCounter data) {
        long add = getDao().insert(data);
        return add;
    }

    /***
     * 通过imei 统一更新计步翻转信息
     * @param data
     * @return
     */
    @Override
    public boolean update(CenterCounter data) {
        String imei = GlobalSettings.instance().getImei();
        CenterCounter userQuery = null;
        long insertUsersID = -1;
        String className = data.getClass().getSimpleName();
        if (TextUtils.isEmpty(imei)) {
            LogUtils.e("imei 为null 更新失败...");
            return false;
        }
        CenterCounterDao dao = getDao();
        try {
            userQuery = dao.queryBuilder().where(CenterCounterDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {//覆盖更新
            data.setId(userQuery.getId());
            LogUtils.d("开始更新" + className + "数据...");
        } else {
            LogUtils.d("没找到" + className + "的所在数据 开始插入");
        }
        insertUsersID = dao.insertOrReplace(data);
        boolean isSuccess = insertUsersID >= 0;
        return isSuccess;
    }

    /***
     * 更新计步开关状态
     * @param toOpen
     * @return
     */
    public boolean updateStepSwitch(boolean toOpen) {
        String imei = GlobalSettings.instance().getImei();
        CenterCounter userQuery = null;
        long insertUsersID = -1;
        String className = CenterCounter.class.getSimpleName();
        if (TextUtils.isEmpty(imei)) {
            LogUtils.e("imei 为null 更新计步开关失败...");
            return false;
        }
        CenterCounterDao dao = getDao();
        try {
            userQuery = dao.queryBuilder().where(CenterCounterDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {
            LogUtils.d("开始更新" + className + "数据...");
        } else {
            LogUtils.d("没找到" + className + "的所在数据 开始插入");
            userQuery = new CenterCounter();
            userQuery.setId(System.currentTimeMillis());
            userQuery.setImei(imei);
        }
        userQuery.setStepSwitch(toOpen);
        insertUsersID = dao.insertOrReplace(userQuery);
        boolean isSuccess = insertUsersID >= 0;
        return isSuccess;
    }

    /***
     * 更新计步时间段
     * @param times
     * @return
     */
    public boolean updateStepWalkingTimes(List<String> times) {
        String imei = GlobalSettings.instance().getImei();
        CenterCounter userQuery = null;
        long insertUsersID = -1;
        String className = CenterCounter.class.getSimpleName();
        if (ListUtils.isEmpty(times)) {
            LogUtils.e("times 为null 更新计步时间段失败...");
            return false;
        }
        if (TextUtils.isEmpty(imei)) {
            LogUtils.e("imei 为null 更新计步时间段失败...");
            return false;
        }
        CenterCounterDao dao = getDao();
        try {
            userQuery = dao.queryBuilder().where(CenterCounterDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {
            LogUtils.d("开始更新" + className + "数据...");
        } else {
            LogUtils.d("没找到" + className + "的所在数据 开始插入");
            userQuery = new CenterCounter();
            userQuery.setId(System.currentTimeMillis());
            userQuery.setImei(imei);
        }
        userQuery.setStep_time(times);
        insertUsersID = dao.insertOrReplace(userQuery);
        boolean isSuccess = insertUsersID >= 0;
        return isSuccess;
    }

    /***
     * 更新翻转检测时间段
     * @param times
     * @return
     */
    public boolean updateTurnOverTimes(List<String> times) {
        String imei = GlobalSettings.instance().getImei();
        CenterCounter userQuery = null;
        long insertUsersID = -1;
        String className = CenterCounter.class.getSimpleName();
        if (ListUtils.isEmpty(times)) {
            LogUtils.e("times 为null 更新翻转失败...");
            return false;
        }
        if (TextUtils.isEmpty(imei)) {
            LogUtils.e("imei 为null 更新翻转时间段失败...");
            return false;
        }
        CenterCounterDao dao = getDao();
        try {
            userQuery = dao.queryBuilder().where(CenterCounterDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {
            LogUtils.d("开始更新" + className + "数据...");
        } else {
            LogUtils.d("没找到" + className + "的所在数据 开始插入");
            userQuery = new CenterCounter();
            userQuery.setId(System.currentTimeMillis());
            userQuery.setImei(imei);
        }
        userQuery.setFlip_check_time(times);
        insertUsersID = dao.insertOrReplace(userQuery);
        boolean isSuccess = insertUsersID >= 0;
        return isSuccess;
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
    public List<CenterCounter> selectAll() {
        List<CenterCounter> list = getDao().loadAll();
        return null != list && list.size() > 0 ? list : null;
    }

    @Override
    public List<CenterCounter> selectWhere(CenterCounter data) {
        return null;
    }

    @Override
    public CenterCounter seelctWhrer(String name) {
        return null;
    }

    @Override
    public List<CenterCounter> selectWhrer(long id) {
        return null;
    }
}
