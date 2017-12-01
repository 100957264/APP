package com.android.fisewatchlauncher.prenster.dao;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.constant.TimeConstants;
import com.android.fisewatchlauncher.dao.CenterSettingsDao;
import com.android.fisewatchlauncher.entity.dao.CenterSettings;
import com.android.fisewatchlauncher.event.IpUpdateEvent;
import com.android.fisewatchlauncher.event.SwitchLowBatEvent;
import com.android.fisewatchlauncher.event.SwitchSmsSosEvent;
import com.android.fisewatchlauncher.function.DaoFlagUtils;
import com.android.fisewatchlauncher.manager.DBManager;
import com.android.fisewatchlauncher.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/11
 * @time 17:12
 */
public class CenterSettingsUtils implements GreenDaoChargeRecordImpl<CenterSettings> {

    private CenterSettingsDao settingsDao;

    private CenterSettingsUtils() {

    }

    private static class SingletonHolder {
        private static final CenterSettingsUtils INSTANCE = new CenterSettingsUtils();
    }

    public static CenterSettingsUtils instance() {
        return SingletonHolder.INSTANCE;
    }

    private CenterSettingsDao getSettingsDao() {
        if (null == settingsDao) {
            settingsDao = DBManager.instance().getDaoSession().getCenterSettingsDao();
        }
        return settingsDao;
    }

    @Override
    public long insert(CenterSettings data) {
        long add = settingsDao.insert(data);
        return add;
    }

    /****
     * 指定imei修改信息
     * @param data
     */
    @Override
    public boolean update(CenterSettings data) {
        String imei = data.getImei();
        CenterSettings userQuery = null;
        CenterSettingsDao dao = getSettingsDao();
        long insertUsersID = -1;
        String className = data.getClass().getSimpleName();
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {//覆盖存储
            data.setId(userQuery.getId());
            LogUtils.d("开始更新" + className + "数据...");
            insertUsersID = dao.insertOrReplace(data);
        } else {
            LogUtils.d("没找到" + className + "的所在数据 开始插入");
            insertUsersID = dao.insertOrReplace(data);
        }
        boolean isSuccess = insertUsersID >= 0;
        return isSuccess;
    }

    /***
     * 删除全部
     */
    @Override
    public void deleteAll() {
        getSettingsDao().deleteAll();
    }

    /***
     * 条件删除
     * @param id
     */
    @Override
    public void deleteWhere(long id) {
        getSettingsDao().deleteByKey(id);
    }

    /***
     * 查询全部
     * @return
     */
    @Override
    public List<CenterSettings> selectAll() {
        List<CenterSettings> list = getSettingsDao().loadAll();
        return null != list && list.size() > 0 ? list : null;
    }

    /***
     * 模糊查询
     * @param data
     * @return
     */
    @Override
    public List<CenterSettings> selectWhere(CenterSettings data) {
        String imei = data.getImei();
        List<CenterSettings> list = getSettingsDao().queryBuilder().where(
                CenterSettingsDao.Properties.Imei.like(imei)).build().list();
        return null != list && list.size() > 0 ? list : null;
    }

    /***
     * 唯一查询
     * @param name
     * @return
     */
    @Override
    public CenterSettings seelctWhrer(String name) {
        CenterSettings user = getSettingsDao().queryBuilder().where(
                CenterSettingsDao.Properties.Imei.eq(name)).build().unique();
        return null != user ? user : null;
    }

    /***
     * Id查询
     * @param id
     * @return
     */
    @Override
    public List<CenterSettings> selectWhrer(long id) {
        List<CenterSettings> users = getSettingsDao().queryBuilder().where(
                CenterSettingsDao.Properties.Id.le(id)).build().list();
        return null != users && users.size() > 0 ? users : null;
    }


    /**
     * 设置端口号
     *
     * @param port
     * @return
     */
    public boolean updatePort(int port) {
        if (port <= 0) {
            return false;
        }
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) return false;
        CenterSettings userQuery = null;
        CenterSettingsDao dao = getSettingsDao();
        long insertUsersID;
        String className = this.getClass().getSimpleName();
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {
            LogUtils.d("开始更新" + className + "数据...");
        } else {
            LogUtils.d("没找到" + className + "的所在数据 开始插入");
            userQuery = new CenterSettings(imei);
        }
        userQuery.setCenterPort(port);
        insertUsersID = dao.insertOrReplace(userQuery);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 centerPort " + "数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

    /**
     * 查询端口号
     *
     * @return
     */
    public int queryPort() {
        int port = -1;
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return port;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
            return port;
        }
        if (null != userQuery) {
            LogUtils.d("查询 端口号" + "数据...");
        } else {
            LogUtils.d("没找到 端口号 " + "的数据");
            return port;
        }
        port = userQuery.getCenterPort();
        LogUtils.d("查询到端口号 port " + port);
        return port;
    }

    /**
     * 设置中心号码信息
     *
     * @param phoneNum
     * @return
     */
    public boolean updateCenterPhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return false;
        }
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) return false;
        CenterSettings userQuery = null;
        CenterSettingsDao dao = getSettingsDao();
        long insertUsersID;
        String className = phoneNum;
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {
            LogUtils.e("开始更新" + className + "数据...");
        } else {
            LogUtils.e("没找到" + className + "的所在数据 开始插入");
            userQuery = new CenterSettings(imei);
        }
        userQuery.setCenterPhoneNum(phoneNum);
        insertUsersID = dao.insertOrReplace(userQuery);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 PhoneNum" + "数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

    /**
     * 查询中心号码
     *
     * @return
     */
    public String queryCenterPhoneNum() {
        String phoneNum;
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return null;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
            return null;
        }
        if (null != userQuery) {
            LogUtils.d("查询 中心号码" + "数据...");
        } else {
            LogUtils.d("没找到 中心号码 " + "的数据");
            return null;
        }
        phoneNum = userQuery.getCenterPhoneNum();
        LogUtils.d("查询到中心号码 phoneNum " + phoneNum);
        return phoneNum;
    }

    /**
     * 设置中心报警号码
     *
     * @param num1
     * @param num2
     * @param num3
     */
    public boolean updateSOSNum(String num1, String num2, String num3) {
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return false;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
        }
        long insertUsersID = -1;
        if (null != userQuery) {
            LogUtils.d("开始更新 SOS" + "数据...");
        } else {
            LogUtils.d("没找到 SOS " + "的所在数据 开始插入");
            userQuery = new CenterSettings(imei);
        }
        userQuery.setSosPhone(num1);
        userQuery.setSosPhone2(num2);
        userQuery.setSosPhone3(num3);
        insertUsersID = dao.insertOrReplace(userQuery);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 SOS" + "数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

    /**
     * 短信报警开关
     *
     * @param content
     */
    public boolean updateSMSSwitch(String content) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        int flag = Integer.parseInt(content);
        boolean isOn = flag == 1;
        EventBus.getDefault().post(new SwitchSmsSosEvent(isOn));
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return false;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
        }
        long insertUsersID = -1;
        if (null != userQuery) {
            LogUtils.d("开始更新 SMS报警开关" + "数据...");
        } else {
            LogUtils.d("没找到 SMS报警开关 " + "的所在数据 开始插入");
            userQuery = new CenterSettings(imei);
        }
        userQuery.setSosSMSSwitch(flag);
        insertUsersID = dao.insertOrReplace(userQuery);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 SMS报警开关" + "数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

    /**
     * 取下手表报警开关
     *
     * @param content
     */
    public boolean updateTakenOffSwitch(String content) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        int flag = Integer.parseInt(content);
        boolean isOn = flag == 1;
        EventBus.getDefault().post(new SwitchSmsSosEvent(isOn));
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return false;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
        }
        long insertUsersID = -1;
        if (null != userQuery) {
            LogUtils.d("开始更新 取下手表报警开关" + "数据...");
        } else {
            LogUtils.d("没找到 取下手表报警开关 " + "的所在数据 开始插入");
            userQuery = new CenterSettings(imei);
        }
        userQuery.setRemoveAlertSwitch(flag);
        insertUsersID = dao.insertOrReplace(userQuery);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 取下手表报警开关" + "数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

    /**
     * 查询取下手表报警开关
     *
     * @return
     */
    public int queryTakenOffSwitch() {
        int isOn = 0;
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return isOn;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
            return isOn;
        }
        if (null != userQuery) {
            LogUtils.d("查询 查询取下手表报警开关" + "数据...");
        } else {
            LogUtils.d("没找到 取下手表报警开关 " + "的数据");
            return isOn;
        }
        isOn = userQuery.getRemoveAlertSwitch();
        LogUtils.d("查询取下手表报警开关  " + isOn);
        return isOn;
    }

    /**
     * TODO 更新 取下手表短信报警开关
     *
     * @param content
     */
    public boolean updateTakenOffSMSSwitch(String content) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        int flag = Integer.parseInt(content);
        boolean isOn = flag == 1;
        EventBus.getDefault().post(new SwitchSmsSosEvent(isOn));
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return false;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
        }
        long insertUsersID = -1;
        if (null != userQuery) {
            LogUtils.d("开始更新 取下手表短信报警开关" + "数据...");
        } else {
            LogUtils.d("没找到 取下手表短信报警开关 " + "的所在数据 开始插入");
            userQuery = new CenterSettings(imei);
        }
        userQuery.setRemoveSmsAlertSswitch(flag);
        insertUsersID = dao.insertOrReplace(userQuery);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 取下手表短信报警开关" + "数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

    /**
     * TODO 查询取下手表短信报警开关
     *
     * @return
     */
    public int queryTakenOffSMSSwitch() {
        int isOn = 0;
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return isOn;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
            return isOn;
        }
        if (null != userQuery) {
            LogUtils.d("查询 取下手表短信报警开关" + "数据...");
        } else {
            LogUtils.d("没找到 取下手表短信报警开关 " + "的数据");
            return isOn;
        }
        isOn = userQuery.getRemoveSmsAlertSswitch();
        LogUtils.d("查询取下手表短信报警开关  " + isOn);
        return isOn;
    }

    /**
     * 更新跌倒报警开关
     *
     * @param content
     */
    public boolean updateFallDownSwitch(String content) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        int flag = Integer.parseInt(content);
        boolean isOn = flag == 1;
        EventBus.getDefault().post(new SwitchSmsSosEvent(isOn));
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return false;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
        }
        long insertUsersID = -1;
        if (null != userQuery) {
            LogUtils.d("开始更新 跌倒报警开关" + "数据...");
        } else {
            LogUtils.d("没找到 跌倒报警开关 " + "的所在数据 开始插入");
            userQuery = new CenterSettings(imei);
        }
        userQuery.setFallDownSwitch(flag);
        insertUsersID = dao.insertOrReplace(userQuery);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 跌倒报警开关" + "数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

    /**
     * 查询跌倒报警开关
     *
     * @return
     */
    public int queryFallDownSwitch() {
        int isOn = 0;
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return isOn;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
            return isOn;
        }
        if (null != userQuery) {
            LogUtils.d("查询 查询跌倒报警开关" + "数据...");
        } else {
            LogUtils.d("没找到 跌倒报警开关 " + "的数据");
            return isOn;
        }
        isOn = userQuery.getFallDownSwitch();
        LogUtils.d("查询跌倒报警开关  " + isOn);
        return isOn;
    }

    /**
     * 设置中心短信控制密码
     *
     * @param pwd
     */
    public boolean updateSMSControlPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return false;
        }
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return false;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
        }
        long insertUsersID = -1;
        if (null != userQuery) {
            LogUtils.d("开始查询 短信控制密码" + "数据...");
        } else {
            LogUtils.d("没找到 短信控制密码 " + "的所在数据 开始插入");
            userQuery = new CenterSettings(imei);
        }
        userQuery.setCenterPwd(pwd);
        insertUsersID = dao.insertOrReplace(userQuery);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 短信控制密码" + "数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

    /**
     * 查询中心短信控制密码
     *
     * @return
     */
    public String querySMSControlPwd() {
        String pwd = null;
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return pwd;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
            return pwd;
        }
        if (null != userQuery) {
            LogUtils.d("查询 中心短信控制密码" + "数据...");
        } else {
            LogUtils.d("没找到 中心短信控制密码 " + "的数据");
            return pwd;
        }
        pwd = userQuery.getCenterPwd();
        LogUtils.d("中心短信控制密码  " + pwd);
        return pwd;
    }

    /**
     * 低电短信报警开关
     *
     * @param content
     */
    public boolean updateLowBatSwitch(String content) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        int flag = Integer.parseInt(content);
        boolean isOn = flag == 1;
        EventBus.getDefault().post(new SwitchLowBatEvent(isOn));
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return false;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
        }
        long insertUsersID = -1;
        if (null != userQuery) {
            LogUtils.d("开始更新 低电量报警开关" + "数据...");
        } else {
            LogUtils.d("没找到 低电量报警开关 " + "所在数据 开始插入");
            userQuery = new CenterSettings(imei);
        }
        userQuery.setLowBatterySwitch(flag);
        insertUsersID = dao.insertOrReplace(userQuery);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 低电量报警开关" + "数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

    /**
     * 设置上传时间间隔
     *
     * @param interval
     */
    public boolean updateUploadInteral(long interval) {
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return false;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
        }
        long insertUsersID = -1;
        if (null != userQuery) {
            LogUtils.d("开始更新 上传间隔" + "数据...");
        } else {
            LogUtils.d("没找到 上传间隔 " + "所在数据 开始插入");
            userQuery = new CenterSettings(imei);
        }
        userQuery.setUpload_interval(interval);
        insertUsersID = dao.insertOrReplace(userQuery);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 上传间隔" + "数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

    /**
     * 查询上传时间间隔
     *
     * @return
     */
    public long queryUploadInteral() {
        long interval = TimeConstants.DEFAULT_CONFIRMED__UPLOAD_FREQUENCY;
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return interval;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
            return interval;
        }
        if (null != userQuery) {
            LogUtils.d("查询 上传时间间隔" + "数据...");
        } else {
            LogUtils.d("没找到 上传时间间隔 " + "的数据");
            return interval;
        }
        interval = userQuery.getUpload_interval();
        LogUtils.d("上传时间间隔 interval " + interval);
        return interval;
    }

    /**
     * 查询短信报警开关是否打开
     *
     * @return
     */
    public boolean querySMSSwitch() {
        boolean isOn;
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return false;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
            return false;
        }
        if (null != userQuery) {
            LogUtils.d("查询 SMS报警开关" + "数据...");
        } else {
            LogUtils.d("没找到 SMS报警开关 " + "的数据");
            return false;
        }
        isOn = userQuery.getSosSMSSwitch() == 1;
        LogUtils.d("SMS报警开关 isOn " + isOn);
        return isOn;
    }

    /**
     * 查询低电量开关是否打开
     *
     * @return
     */
    public boolean queryLowBatterySwitch() {
        boolean isOn;
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return false;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
            return false;
        }
        if (null != userQuery) {
            LogUtils.d("查询 低电量报警开关" + "数据...");
        } else {
            LogUtils.d("没找到 低电量报警开关 " + "的数据");
            return false;
        }
        isOn = userQuery.getLowBatterySwitch() == 1;
        LogUtils.d("低电量报警开关 isOn " + isOn);
        return isOn;
    }

    /**
     * 查询SOS报警号码
     *
     * @return 返回报警号码集合 已过滤为空情况
     */
    public List<String> querySOSList() {
        List<String> sosList = new ArrayList<>();
        CenterSettingsDao dao = getSettingsDao();
        CenterSettings userQuery = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return sosList;
        }
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
            return sosList;
        }
        if (null != userQuery) {
            LogUtils.d("查询 SOS报警号码" + "数据...");
        } else {
            LogUtils.d("没找到 SOS报警号码 " + "的数据");
            return sosList;
        }
        getSosList(sosList, userQuery.getSosPhone());
        getSosList(sosList, userQuery.getSosPhone2());
        sosList = getSosList(sosList, userQuery.getSosPhone3());
        LogUtils.d("SOS报警号码  " + TextUtils.join(",", sosList));
        return sosList;
    }

    private List<String> getSosList(List<String> sosList, String sosPhoneNum) {
        if (!TextUtils.isEmpty(sosPhoneNum)) {
            sosList.add(sosPhoneNum);
        }
        return sosList;
    }

    /**
     * 清空中心设置信息
     *
     * @param ctx
     * @return
     */
    public void clearCenterSettings(Context ctx) {
        CenterSettingsDao dao = getSettingsDao();
        dao.deleteAll();
    }

    /**
     * 更新中心IP
     *
     * @param settings
     * @return
     */
    public boolean updateIp(CenterSettings settings) {
        String ip = settings.getCenter_ip();
        String imei = settings.getImei();
        CenterSettings userQuery = null;
        CenterSettingsDao dao = getSettingsDao();
        long insertUsersID = -1;
        String className = settings.getClass().getSimpleName();
        try {
            userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {
            String srcIp = userQuery.getCenter_ip();
            if (!TextUtils.isEmpty(srcIp) && !srcIp.equals(ip)) {
                EventBus.getDefault().post(new IpUpdateEvent(ip));
            }
            userQuery.setCenter_ip(ip);
            LogUtils.d("开始更新" + className + "数据...");
            insertUsersID = dao.insertOrReplace(userQuery);
        } else {
            LogUtils.d("没找到" + className + "的所在数据 开始插入");
            insertUsersID = dao.insertOrReplace(settings);
        }
        boolean isSuccess = insertUsersID >= 0;
        return isSuccess;
    }

    public String getIP() {
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return null;
        }
        CenterSettingsDao dao = getSettingsDao();
        String srcIP = null;
        try {
            CenterSettings userQuery = dao.queryBuilder().where(CenterSettingsDao.Properties.Imei.eq(imei)).build().unique();
            if (null != userQuery) {
                srcIP = userQuery.getCenter_ip();
                if (!TextUtils.isEmpty(srcIP)) {
                    return srcIP;
                }
            }
        } catch (SQLiteException e) {
            LogUtils.e("查询 IP 失败");
        } finally {
            return srcIP;
        }
    }

}
