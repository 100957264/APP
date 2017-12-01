package com.android.fisewatchlauncher.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.constant.ReceiverConstant;
import com.android.fisewatchlauncher.event.BindUnBindEvent;
import com.android.fisewatchlauncher.function.view.ViewManager;
import com.android.fisewatchlauncher.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * @author mare
 * @Description: config of preference
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/25
 * @time 16:23
 */
public class PreferControler {
    public static final String KEY_PREFS = "prefer_controlller";
    public static final String KEY_WEATHER_CITY = "config_weather_city";
    public static final String KEY_WEATHER_DESCRIPTION = "config_weather_description";
    public static final String KEY_WEATHER_LAST_UPDATE_TIME = "config_weather_last_time";

    public static final String KEY_ANALOG_CLOCK_STYLE = "config_analog_clock_style";
    public static final int ANALOG_CLOCK_STYLE_INDEX_DEFAULT = 2;

    public static final String KEY_LOCKSCREEN_ENABLE = "config_screenlock_enable";

    public static final String KEY_IMEI = "config_imei";

    public static final String KEY_DEVICE_BIND = "config_device_bond";

    public static final String KEY_PREFS_CRASH_COOLECTOR = "crash_collector";//CrashHandler收集日志管理器
    public static final String KEY_EXTRA_CRASH_LASTTIME = "timestamp";//上一次异常日志的收集时间
    public static final String KEY_EXTRA_CRASH_CAUSE = "cause";//上一次异常原因

    //环信配置
    private static final String KEY_PREFS_EASEMOB = "easemob";
    private static final String KEY_EXTRA_CALL_MIN_VIDEO_KBPS = "CALL_MIN_VIDEO_KBPS";
    private static final String KEY_EXTRA_CALL_MAX_VIDEO_KBPS = "CALL_Max_VIDEO_KBPS";
    private static final String KEY_EXTRA_CALL_MAX_FRAME_RATE = "CALL_MAX_FRAME_RATE";
    private static final String KEY_EXTRA_CALL_AUDIO_SAMPLE_RATE = "CALL_AUDIO_SAMPLE_RATE";
    private static final String KEY_EXTRA_CALL_BACK_CAMERA_RESOLUTION = "CALL_BACK_CAMERA_RESOLUTION";
    private static final String KEY_EXTRA_CALL_FRONT_CAMERA_RESOLUTION = "FRONT_CAMERA_RESOLUTIOIN";
    private static final String KEY_EXTRA_CALL_FIX_SAMPLE_RATE = "CALL_FIX_SAMPLE_RATE";
    //环信账户相关数据
    public static final String KEY_EXTRA_EASEMOB_USRID = "userId";
    public static final String KEY_EXTRA_EASEMOB_PWD = "pwd";
    public static final String KEY_EXTRA_EASEMOB_TOKEN = "token";
    public static final String KEY_EXTRA_EASEMOB_TOKEN_EXPIRE = "token_expire";//token的默认有效期

    private HashMap<String, SharedPreferences> preferences = new HashMap<String, SharedPreferences>();

    private PreferControler() {
    }

    private static class SingletonHolder {
        private static final PreferControler INSTANCE = new PreferControler();
    }

    public static PreferControler instance() {
        return SingletonHolder.INSTANCE;
    }

    private SharedPreferences getSharedPreferences(String key) {
        SharedPreferences sharedPreferences;
        if (preferences.containsKey(key)) {
            sharedPreferences = preferences.get(key);
        } else {
            sharedPreferences = KApplication.sContext.getSharedPreferences(key, Context.MODE_PRIVATE);
            preferences.put(key, sharedPreferences);
        }
        return sharedPreferences;
    }

    public void recycleAllPreferences() {
        for (String key : preferences.keySet()) {
            recycleSinglePreferences(key);
        }
    }

    public void recycleSinglePreferences(String key) {
        if (preferences.containsKey(key)) {
            preferences.remove(key);
        }
    }

    private String getString(String keyPreference, String keyValue, String defaultValue) {
        SharedPreferences settings = getSharedPreferences(keyPreference);
        return settings.getString(keyValue, defaultValue);
    }

    private int getInt(String keyPreference, String keyValue, int defaultValue) {
        SharedPreferences settings = getSharedPreferences(keyPreference);
        return settings.getInt(keyValue, defaultValue);
    }

    private long getLong(String keyPreference, String keyValue, long defaultValue) {
        SharedPreferences settings = getSharedPreferences(keyPreference);
        return settings.getLong(keyValue, defaultValue);
    }

    private boolean getBoolean(String keyPreference, String keyValue, boolean defaultValue) {
        SharedPreferences settings = getSharedPreferences(keyPreference);
        return settings.getBoolean(keyValue, defaultValue);
    }

    private boolean putInt(String keyPreference, String key, int value) {
        SharedPreferences settings = getSharedPreferences(keyPreference);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    private boolean putLong(String keyPreference, String key, long value) {
        SharedPreferences settings = getSharedPreferences(keyPreference);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    private boolean putBoolean(String keyPreference, String key, boolean value) {
        SharedPreferences settings = getSharedPreferences(keyPreference);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    private boolean putString(String keyPreference, String key, String value) {
        SharedPreferences settings = getSharedPreferences(keyPreference);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public String getWeather() {
        return getString(KEY_PREFS, KEY_WEATHER_DESCRIPTION, "");
    }

    public String getWeatherCity() {
        return getString(KEY_PREFS, KEY_WEATHER_CITY, "");
    }

    public Long getWeatherLastUpdateTime() {
        return getLong(KEY_PREFS, KEY_WEATHER_LAST_UPDATE_TIME, 0);
    }

    public boolean setWeatherTime(long time) {
        return putLong(KEY_PREFS, KEY_WEATHER_LAST_UPDATE_TIME, time);
    }

    public boolean setWeather(String city, String weather) {
        SharedPreferences settings = getSharedPreferences(KEY_PREFS);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(KEY_WEATHER_CITY, city);
        editor.putString(KEY_WEATHER_DESCRIPTION, weather);
        return editor.commit();
    }

    public int getAnalogClockIndex() {
        return getInt(KEY_PREFS, KEY_ANALOG_CLOCK_STYLE, ANALOG_CLOCK_STYLE_INDEX_DEFAULT);
    }

    public boolean setAnalogClockIndex(int value) {
        return putInt(KEY_PREFS, KEY_ANALOG_CLOCK_STYLE, value);
    }

    public boolean isLockScreenEnable() {
        return getBoolean(KEY_PREFS, KEY_LOCKSCREEN_ENABLE, false);//默认关掉锁屏界面
    }

    public boolean setlockScreenEnable(boolean enable) {
        return putBoolean(KEY_PREFS, KEY_LOCKSCREEN_ENABLE, enable);
    }

    public boolean setImei(String imei) {
        return putString(KEY_PREFS, KEY_IMEI, imei);
    }

    public String getImei() {
        return getString(KEY_PREFS, KEY_IMEI, "");
    }

    public boolean setDeviceBindState(boolean state) {
        boolean oldState = getDeviceBondState();
        boolean isChanged = /*oldState != state*/true;//强制写值
        LogUtils.i("isChanged " + isChanged + " ,isBond " + state);
        if (isChanged) {
            if (state) {//如若之前还没绑定就置绑定状态
                KApplication.sContext.sendBroadcast(new Intent(ReceiverConstant.ACTION_CMD_BIND_DEVICE));
            } else {//如若之前已绑定就置解绑状态
                KApplication.sContext.sendBroadcast(new Intent(ReceiverConstant.ACTION_CMD_UNBIN_DEVICE));
            }
            EventBus.getDefault().postSticky(new BindUnBindEvent(state));
            return putBoolean(KEY_PREFS, KEY_DEVICE_BIND, state);
        }
        ViewManager.instance().updateInnerPagers();
        return true;//不用重复设置
    }

    public boolean getDeviceBondState() {
        return getBoolean(KEY_PREFS, KEY_DEVICE_BIND, false);
    }

    public boolean setEaseToken(String token) {
        return putString(KEY_PREFS_EASEMOB, KEY_EXTRA_EASEMOB_TOKEN, token);
    }

    public String getEaseToken() {
        return getString(KEY_PREFS_EASEMOB, KEY_EXTRA_EASEMOB_TOKEN, "");
    }

    public boolean setEaseTokenExpire(long tokenExpire) {
        return putLong(KEY_PREFS_EASEMOB, KEY_EXTRA_EASEMOB_TOKEN_EXPIRE, tokenExpire);
    }

    public long isEaseTokenExpire() {
        return getLong(KEY_PREFS_EASEMOB, KEY_EXTRA_EASEMOB_TOKEN_EXPIRE, System.currentTimeMillis());
    }

    public boolean setEaseId(String userid) {
        return putString(KEY_PREFS_EASEMOB, KEY_EXTRA_EASEMOB_USRID, userid);
    }

    public String getEaseId() {
        return getString(KEY_PREFS_EASEMOB, KEY_EXTRA_EASEMOB_USRID, "");
    }
    public boolean setEasePwd(String pwd) {
        return putString(KEY_PREFS_EASEMOB, KEY_EXTRA_EASEMOB_PWD, pwd);
    }

    public String getEasePwd() {
        return getString(KEY_PREFS_EASEMOB, KEY_EXTRA_EASEMOB_PWD, "");
    }



    /**
     * 设置当次异常日志的时间
     *
     * @param timeStamp
     * @return
     */
    public boolean setLastCrashTime(long timeStamp) {
        return putLong(KEY_PREFS_CRASH_COOLECTOR, KEY_EXTRA_CRASH_LASTTIME, timeStamp);
    }


    public long getLastCrashTime() {
        return getLong(KEY_PREFS_CRASH_COOLECTOR, KEY_EXTRA_CRASH_LASTTIME, System.currentTimeMillis());
    }

    /**
     * 设置当次异常日志的原因
     *
     * @param cause
     * @return
     */
    public boolean setLastCrashCause(String cause) {
        return putString(KEY_PREFS_CRASH_COOLECTOR, KEY_EXTRA_CRASH_CAUSE, cause);
    }

    public String getLastCrashCause() {
        return getString(KEY_PREFS_CRASH_COOLECTOR, KEY_EXTRA_CRASH_CAUSE, "");
    }

    /**
     * Min Video kbps
     * if no value was set, return -1
     *
     * @return
     */
    public int getCallMinVideoKbps() {
        return getInt(KEY_PREFS_EASEMOB, KEY_EXTRA_CALL_MIN_VIDEO_KBPS, -1);
    }

    public void setCallMinVideoKbps(int minBitRate) {
        putInt(KEY_PREFS_EASEMOB, KEY_EXTRA_CALL_MIN_VIDEO_KBPS, minBitRate);
    }

    /**
     * Max Video kbps
     * if no value was set, return -1
     *
     * @return
     */
    public int getCallMaxVideoKbps() {
        return getInt(KEY_PREFS_EASEMOB, KEY_EXTRA_CALL_MAX_VIDEO_KBPS, -1);
    }

    public void setCallMaxVideoKbps(int maxBitRate) {
        putInt(KEY_PREFS_EASEMOB, KEY_EXTRA_CALL_MAX_VIDEO_KBPS, maxBitRate);
    }

    /**
     * audio sample rate
     * if no value was set, return -1
     *
     * @return
     */
    public int getCallAudioSampleRate() {
        return getInt(KEY_PREFS_EASEMOB, KEY_EXTRA_CALL_MAX_VIDEO_KBPS, -1);
    }

    public void setCallAudioSampleRate(int audioSampleRate) {
        putInt(KEY_PREFS_EASEMOB, KEY_EXTRA_CALL_MAX_VIDEO_KBPS, audioSampleRate);
    }

    /**
     * Max frame rate
     * if no value was set, return -1
     *
     * @return
     */
    public int getCallMaxFrameRate() {
        return getInt(KEY_PREFS_EASEMOB, KEY_EXTRA_CALL_MAX_FRAME_RATE, -1);
    }

    public void setCallMaxFrameRate(int maxFrameRate) {
        putInt(KEY_PREFS_EASEMOB, KEY_EXTRA_CALL_MAX_FRAME_RATE, maxFrameRate);
    }

    /**
     * back camera resolution
     * format: 320x240
     * if no value was set, return ""
     */
    public String getCallBackCameraResolution() {
        return getString(KEY_PREFS_EASEMOB, KEY_EXTRA_CALL_BACK_CAMERA_RESOLUTION, "");
    }

    public void setCallBackCameraResolution(String resolution) {
        putString(KEY_PREFS_EASEMOB, KEY_EXTRA_CALL_BACK_CAMERA_RESOLUTION, resolution);
    }

    /**
     * front camera resolution
     * format: 320x240
     * if no value was set, return ""
     */
    public String getCallFrontCameraResolution() {
        return getString(KEY_PREFS_EASEMOB, KEY_EXTRA_CALL_FRONT_CAMERA_RESOLUTION, "");
    }

    public void setCallFrontCameraResolution(String resolution) {
        putString(KEY_PREFS_EASEMOB, KEY_EXTRA_CALL_FRONT_CAMERA_RESOLUTION, resolution);
    }

    /**
     * fixed video sample rate
     * if no value was set, return false
     *
     * @return
     */
    public boolean isCallFixedVideoResolution() {
        return getBoolean(KEY_PREFS_EASEMOB, KEY_EXTRA_CALL_FIX_SAMPLE_RATE, false);
    }

    public void setCallFixedVideoResolution(boolean enable) {
        putBoolean(KEY_PREFS_EASEMOB, KEY_EXTRA_CALL_FIX_SAMPLE_RATE, enable);
    }
}

