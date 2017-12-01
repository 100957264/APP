package com.android.fisewatchlauncher.prenster.dao;

import android.database.sqlite.SQLiteException;

import com.android.fisewatchlauncher.dao.WeatherDao;
import com.android.fisewatchlauncher.entity.weather.Weather;
import com.android.fisewatchlauncher.manager.DBManager;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.util.List;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/5
 * @time 18:08
 */
public class WeatherDaoUtils implements GreenDaoChargeRecordImpl<Weather> {
    private WeatherDaoUtils() {
    }

    private static class SingletonHolder {
        private static final WeatherDaoUtils INSTANCE = new WeatherDaoUtils();
    }

    public static WeatherDaoUtils instance() {
        return SingletonHolder.INSTANCE;
    }

    private WeatherDao settingsDao;

    private WeatherDao getDao() {
        if (null == settingsDao) {
            settingsDao = DBManager.instance().getDaoSession().getWeatherDao();
        }
        return settingsDao;
    }

    @Override
    public long insert(Weather data) {
        long add = getDao().insert(data);
        return add;
    }

    /***
     * 通过city 更新天气
     * @param data
     * @return
     */
    @Override
    public boolean update(Weather data) {
        String city = data.getCity();
        Weather userQuery = null;
        long insertUsersID = -1;
        String className = data.getClass().getSimpleName();
        WeatherDao dao = getDao();
        Weather.CmdType type = data.getCmdType();
        String date = data.getDate();
        String time = data.getTime();
        try {
            userQuery = dao.queryBuilder().where(WeatherDao.Properties.City.eq(city),WeatherDao.Properties.CmdType.eq(type)
                    ,WeatherDao.Properties.Date.eq(date)
                    ,WeatherDao.Properties.Time.eq(time) ).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {//覆盖更新
            data.setId(userQuery.getId());
            LogUtils.d("开始更新" + className + "数据...");
        } else {
            data.setId(System.currentTimeMillis());
            LogUtils.d("没找到" + className + "的所在数据 开始插入");
        }
        insertUsersID = dao.insertOrReplace(data);
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
    public List<Weather> selectAll() {
        List<Weather> list = getDao().loadAll();
        return null != list && list.size() > 0 ? list : null;
    }

    @Override
    public List<Weather> selectWhere(Weather data) {
        return null;
    }

    @Override
    public Weather seelctWhrer(String name) {
        return null;
    }

    @Override
    public List<Weather> selectWhrer(long id) {
        return null;
    }
}
