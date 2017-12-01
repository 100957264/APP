package com.android.fisewatchlauncher.prenster.dao;

import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import com.android.fisewatchlauncher.BuildConfig;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.dao.CenterLocationDao;
import com.android.fisewatchlauncher.entity.dao.CenterLocation;
import com.android.fisewatchlauncher.function.DaoFlagUtils;
import com.android.fisewatchlauncher.manager.DBManager;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/5
 * @time 0:52
 */
public class LocationDaoUtils implements GreenDaoChargeRecordImpl<CenterLocation> {

    private LocationDaoUtils() {
    }

    private static class SingletonHolder {
        private static final LocationDaoUtils INSTANCE = new LocationDaoUtils();
    }

    public static LocationDaoUtils instance() {
        return SingletonHolder.INSTANCE;
    }

    private CenterLocationDao settingsDao;

    private CenterLocationDao getLocationDao() {
        if (null == settingsDao) {
            settingsDao = DBManager.instance().getDaoSession().getCenterLocationDao();
        }
        return settingsDao;
    }

    @Override
    public long insert(CenterLocation data) {
        long add = getLocationDao().insert(data);
        return add;
    }

    /**
     * 获取每次要上传的历史定位信息(用于紧急呼叫等情况)
     *
     * @return
     */
    public String queryLocationStr() {
        String result = "";
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return result;
        }
        CenterLocationDao dao = getLocationDao();
        boolean isSmartPhone = BuildConfig.isSmartProtocol;
        try {
            CenterLocation recentLocation = dao.queryBuilder() .where(CenterLocationDao.Properties.IsSmartPhone.eq(isSmartPhone))
                    .orderDesc(CenterLocationDao.Properties.Id).limit(1).unique();
            if (null != recentLocation) {
                result = recentLocation.getLocation();
            }
        } catch (Exception e) {
            LogUtils.e("查询 " + dao.getClass().getSimpleName() + "失败");
        } finally {
            return result;
        }
    }

    /**
     * 获取最新的城市地址信息
     *
     * @return
     */
    public String queryRecentLocationCity() {
        String result = "";
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return result;
        }
        CenterLocationDao dao = getLocationDao();
        List<String> locationStrs = new ArrayList<>();
        boolean isSmartPhone = BuildConfig.isSmartProtocol;
        try {
            CenterLocation recentLocation = dao.queryBuilder().where(CenterLocationDao.Properties.IsSmartPhone.eq(isSmartPhone))
                    .orderDesc(CenterLocationDao.Properties.Id).limit(1).unique();
            if (null != recentLocation) {
                String province = recentLocation.getProvince();
                String city = recentLocation.getCity();
                String address = recentLocation.getAddr();
                addLocation(province,locationStrs);
                addLocation(city,locationStrs);
                addLocation(address,locationStrs);
            }
        } catch (Exception e) {
            LogUtils.e("查询 " + dao.getClass().getSimpleName() + "失败");
        } finally {
            return TextUtils.join(",",locationStrs);
        }
    }

    private List<String> addLocation(String field, List<String> locationStrs) {
        if (!TextUtils.isEmpty(field)) {
            locationStrs.add(field);
        }
        return locationStrs;
    }

    @Override
    public boolean update(CenterLocation data) {
        String city = data.getCity();
        CenterLocation userQuery = null;
        long insertUsersID = -1;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei) || TextUtils.isEmpty(city)) {
            return false;
        }
        String className = data.getClass().getSimpleName();
        CenterLocationDao dao = getLocationDao();
        boolean isSmartPhone = BuildConfig.isSmartProtocol;
        long time = data.getTime();
        try {
            userQuery = dao.queryBuilder().where(CenterLocationDao.Properties.City.eq(city),
                    CenterLocationDao.Properties.Time.eq(time),
                    CenterLocationDao.Properties.IsSmartPhone.eq(isSmartPhone)).build().forCurrentThread().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {//覆盖更新
            data.setId(userQuery.getId());
            LogUtils.d("开始更新" + className + "数据...");
        } else {
            data.setId(time);
            LogUtils.d("没找到" + className + "的所在数据 开始插入");
        }
        insertUsersID = dao.insertOrReplace(data);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 地理位置" + "数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

    @Override
    public void deleteAll() {
        getLocationDao().deleteAll();
    }

    @Override
    public void deleteWhere(long id) {
        getLocationDao().deleteByKey(id);
    }

    @Override
    public List<CenterLocation> selectAll() {
        List<CenterLocation> list = getLocationDao().loadAll();
        return null != list && list.size() > 0 ? list : null;
    }

    @Override
    public List<CenterLocation> selectWhere(CenterLocation data) {
        String city = data.getCity();
        List<CenterLocation> list = getLocationDao().queryBuilder().where(
                CenterLocationDao.Properties.City.like(city)).build().list();
        return null != list && list.size() > 0 ? list : null;
    }

    @Override
    public CenterLocation seelctWhrer(String city) {
        CenterLocation user = getLocationDao().queryBuilder().where(
                CenterLocationDao.Properties.City.eq(city)).build().unique();
        return null != user ? user : null;
    }

    @Override
    public List<CenterLocation> selectWhrer(long id) {
        List<CenterLocation> users = getLocationDao().queryBuilder().where(
                CenterLocationDao.Properties.Id.le(id)).build().list();
        return null != users && users.size() > 0 ? users : null;
    }
}
