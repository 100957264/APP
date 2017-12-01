//package com.android.fisewatchlauncher.utils;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteException;
//
//import com.fise.marechat.KApplication;
//import com.fise.marechat.bean.dao.CenterSettings;
//import com.fise.marechat.dao.CenterClockDao;
//import com.fise.marechat.dao.CenterSettingsDao;
//import com.fise.marechat.dao.ClockFormatDao;
//import com.fise.marechat.dao.DaoMaster;
//import com.fise.marechat.dao.DaoSession;
//
///**
// * @author mare
// * @Description:
// * @csdnblog http://blog.csdn.net/mare_blue
// * @date 20187//10
// * @time 16:52
// */
//public class DaoUtils {
//    private DaoMaster daoMaster;
//    private DaoSession daoSession;
//
//    private DaoUtils() {
//        initDao();
//    }
//
//    private static class SingletonHolder {
//        private static final DaoUtils INSTANCE = new DaoUtils();
//    }
//
//    public static DaoUtils getInstance() {
//        return SingletonHolder.INSTANCE;
//    }
//
//    private void initDao() {
//        if (null == daoSession || null == daoMaster) {
//            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(KApplication.sContext, "user.db");
//            daoMaster = new DaoMaster(helper.getWritableDb());
//            daoSession = daoMaster.newSession();
//        }
//    }
//
//    private CenterSettingsDao centerSettings;
//
//    private CenterSettingsDao getSettingsDao() {
//        if (null == centerSettings) {
//            centerSettings = daoSession.getCenterSettingsDao();
//        }
//        return centerSettings;
//    }
//
//    private CenterClockDao clockDao;
//
//    private CenterClockDao getClockDao() {
//        if (null == centerSettings) {
//            clockDao = daoSession.getCenterClockDao();
//        }
//        return clockDao;
//    }
//
//    private ClockFormatDao clockFormatDao;
//
//    private ClockFormatDao getClockFormatDao() {
//        if (null == centerSettings) {
//            clockFormatDao = daoSession.getClockFormatDao();
//        }
//        return clockFormatDao;
//    }
//
//    /**
//     * 更新中心设置信息
//     *
//     * @param ctx
//     * @param settings
//     * @return
//     */
//    public boolean updateCenterPhoneNum(Context ctx, CenterSettings settings) {
//        String phoneNum = settings.getCenterPhoneNum();
//        String imei = settings.getImei();
//        CenterSettings userQuery = null;
//        CenterSettingsDao dao = getSettingsDao();
//        long insertUsersID = -1;
//        String className = settings.getClass().getSimpleName();
//        try {
//            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
//        } catch (SQLiteException e) {
//            LogUtils.e("查询 " + className + "失败" );
//        }
//        if (null != userQuery) {
//            userQuery.setCenterPhoneNum(phoneNum);
//            LogUtils.d("开始更新" + className+ "数据...");
//            insertUsersID = dao.insertOrReplace(userQuery);
//        } else {
//            LogUtils.d("没找到" + className + "的所在数据 开始插入");
//            insertUsersID = dao.insertOrReplace(settings);
//        }
//        boolean isSuccess = insertUsersID >= 0;
//        return isSuccess;
//    }
//
//    /**
//     * 清空中心设置信息
//     *
//     * @param ctx
//     * @return
//     */
//    public void clearCenterSettings(Context ctx) {
//        CenterSettingsDao dao = getSettingsDao();
//        dao.deleteAll();
//    }
//
//}
