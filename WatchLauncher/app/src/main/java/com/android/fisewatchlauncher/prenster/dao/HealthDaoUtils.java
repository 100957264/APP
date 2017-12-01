package com.android.fisewatchlauncher.prenster.dao;

import com.android.fisewatchlauncher.dao.HealthInfoDao;
import com.android.fisewatchlauncher.entity.dao.HealthInfo;
import com.android.fisewatchlauncher.manager.DBManager;

import java.util.List;

/**
 * project : WatchLauncher
 * author : ChenJP
 * date : 2017/11/6  14:51
 * describe : HealthDaoUtils
 */

public class HealthDaoUtils implements GreenDaoChargeRecordImpl<HealthInfo> {
    private HealthDaoUtils() {
    }

    private static class SingletonHolder {
        private static final HealthDaoUtils INSTANCE = new HealthDaoUtils();
    }

    public static HealthDaoUtils instance() {
        return SingletonHolder.INSTANCE;
    }

    private HealthInfoDao settingsDao;

    private HealthInfoDao getDao() {
        if (null == settingsDao) {
            settingsDao = DBManager.instance().getDaoSession().getHealthInfoDao();
        }
        return settingsDao;
    }

    @Override
    public long insert(HealthInfo data) {
        long add = getDao().insert(data);
        return add;
    }

    @Override
    public boolean update(HealthInfo data) {
        return getDao().insertOrReplace(data) >= 0;
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
    public List<HealthInfo> selectAll() {
        List<HealthInfo> list = getDao().loadAll();
        return null != list && list.size() > 0 ? list : null;
    }

    @Override
    public List<HealthInfo> selectWhere(HealthInfo data) {
        return null;
    }

    @Override
    public HealthInfo seelctWhrer(String name) {
        return null;
    }

    @Override
    public List<HealthInfo> selectWhrer(long id) {
        return null;
    }
}
