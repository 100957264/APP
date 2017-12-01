package com.android.fisewatchlauncher.function.heartrate;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.entity.dao.HealthInfo;
import com.android.fisewatchlauncher.prenster.dao.HealthDaoUtils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * project : WatchLauncher
 * date : 2017/11/2  16:10
 * describe : 心率相关
 *
 * @author : ChenJP
 */

public class HealthUtils {
    public static final int    MAX_UPDATE_INTERVAL = 65535;
    public static final int    MIN_UPDATE_INTERVAL = 300;
    public static final String SP_NAME_HR          = "sp_name_heart_rate";
    public static final int    STATE_DANGER_HR     = 5;
    public static final int    STATE_ABNORMAL_HR   = 3;
    public static final int    STATE_NORMAL_HR     = 1;

    public static final String SP_KEY_HEARTRATE_FUNCTION_SWITCH = "sp_key_heartrate_function_switch";
    /**
     * 最大心率危险值
     */
    public static final String SP_KEY_MAX_DANGER_HR_VALUE       = "sp_key_max_danger_hr_value";
    public static int sMaxDangerHrValue;
    /**
     * 最小心率危险值
     */
    public static final String SP_KEY_MIN_DANGER_HR_VALUE = "sp_key_min_danger_hr_value";
    public static int sMinDangerHrValue;
    /**
     * 危险值幅度
     */
    public static final String SP_KEY_DANGER_HR_VALUE_RANGE = "sp_key_danger_hr_value_range";
    public static int sDangerHrValueRange;
    /**
     * 正常心率值
     */
    public static final String SP_KEY_NORMAL_HR_VALUE = "sp_key_normal_hr_value";
    public static int sNormalHrValue;
    /**
     * 正常心率值幅度
     */
    public static final String SP_KEY_NORMAL_HR_VALUE_RANGE = "sp_key_normal_hr_value_range";
    public static int sNormalHrValueRange;
    /**
     * 正常心率时上传频率
     */
    public static final String SP_KEY_NORMAL_HR_UPDATE_INTERVAL = "sp_key_normal_hr_update_interval";
    public static long sNormalHrUpdateInterval;
    /**
     * 危险心率时上传频率
     */
    public static final String SP_KEY_DANGER_HR_UPDATE_INTERVAL = "sp_key_danger_hr_update_interval";
    public static long sDangerHrUpdateInterval;
    /**
     * 非危险非正常心率时上传频率
     */
    public static final String SP_KEY_ABNORMAL_HR_UPDATE_INTERVAL = "sp_key_abnormal_hr_update_interval";
    public static long sAbnormalHrUpdateInterval;
    public static long sHrUpdateInterval;

    private static HealthUtils               mHRUtils;
    private static SensorManager             mSensorManager;
    private static Sensor                    mSensor;
    private        HealthInfo                mHealthInfo;
    private        HealthSensorEventListener mHealthSensorEventListener;
    public static  boolean                   sHeartrateFunctionSwitch;
    public ScheduledExecutorService mExecutorService;
    public AutoUpdateRunnable mAutoUpdateRunnable;

    private HealthUtils() {
    }

    public static HealthUtils instance() {
        if (mHRUtils == null) {
            mHRUtils = new HealthUtils();
        }
        return mHRUtils;
    }

    /**
     * @param content 为上传间隔时间，单位秒,连续上传时最小时间不小于 300 秒，最大不超过 65535.
     *                为 1 则代表终端心率单次上传，上传完后自动关闭。
     *                为 0 则代表终端心率上传关闭
     */
    public static void parseAndHandle(String content) {
        int code = Integer.parseInt(content);
        if (code == 0) {
            return;
        }
        if (code == 1) {
            return;
        }
        if (code > MAX_UPDATE_INTERVAL) {
            code = MAX_UPDATE_INTERVAL;
        } else if (code < MIN_UPDATE_INTERVAL) {
            code = MIN_UPDATE_INTERVAL;
        }
        //TODO handle it
    }

    public static void initParam() {
        SharedPreferences sp = KApplication.sContext.getSharedPreferences(SP_NAME_HR, Context.MODE_PRIVATE);
        sHeartrateFunctionSwitch = sp.getBoolean(SP_KEY_HEARTRATE_FUNCTION_SWITCH, false);
        if (sHeartrateFunctionSwitch) {
            sMaxDangerHrValue = sp.getInt(SP_KEY_MAX_DANGER_HR_VALUE, sMaxDangerHrValue);
            sMinDangerHrValue = sp.getInt(SP_KEY_MIN_DANGER_HR_VALUE, sMinDangerHrValue);
            sDangerHrValueRange = sp.getInt(SP_KEY_DANGER_HR_VALUE_RANGE, sDangerHrValueRange);
            sNormalHrValue = sp.getInt(SP_KEY_NORMAL_HR_VALUE, sNormalHrValue);
            sNormalHrValueRange = sp.getInt(SP_KEY_NORMAL_HR_VALUE_RANGE, sNormalHrValueRange);
            sNormalHrUpdateInterval = sp.getLong(SP_KEY_NORMAL_HR_UPDATE_INTERVAL, sNormalHrUpdateInterval);
            sDangerHrUpdateInterval = sp.getLong(SP_KEY_DANGER_HR_UPDATE_INTERVAL, sDangerHrUpdateInterval);
            sAbnormalHrUpdateInterval = sp.getLong(SP_KEY_ABNORMAL_HR_UPDATE_INTERVAL, sAbnormalHrUpdateInterval);
        }
    }

    public static void saveParam(boolean open, int maxDangerHrValue, int minDangerHrValue, int dangerHrValueRange,
                                 int normalHrValue, int normalHrValueRange, int normalHrUpdateInterval,
                                 int dangerHrUpdateInterval, int abnormalHrUpdateInterval) {
        SharedPreferences sp = KApplication.sContext.getSharedPreferences(SP_NAME_HR, Context.MODE_PRIVATE);
        SharedPreferences.Editor SPEditor = sp.edit();
        SPEditor.putBoolean(SP_KEY_HEARTRATE_FUNCTION_SWITCH, open);
        SPEditor.putInt(SP_KEY_MAX_DANGER_HR_VALUE, maxDangerHrValue);
        SPEditor.putInt(SP_KEY_MIN_DANGER_HR_VALUE, minDangerHrValue);
        SPEditor.putInt(SP_KEY_DANGER_HR_VALUE_RANGE, dangerHrValueRange);
        SPEditor.putInt(SP_KEY_NORMAL_HR_VALUE, normalHrValue);
        SPEditor.putInt(SP_KEY_NORMAL_HR_VALUE_RANGE, normalHrValueRange);
        SPEditor.putInt(SP_KEY_NORMAL_HR_UPDATE_INTERVAL, normalHrUpdateInterval);
        SPEditor.putInt(SP_KEY_DANGER_HR_UPDATE_INTERVAL, dangerHrUpdateInterval);
        SPEditor.putInt(SP_KEY_ABNORMAL_HR_UPDATE_INTERVAL, abnormalHrUpdateInterval);
        SPEditor.apply();
        sMaxDangerHrValue = maxDangerHrValue;
        sMinDangerHrValue = minDangerHrValue;
        sDangerHrValueRange = dangerHrValueRange;
        sNormalHrValue = normalHrValue;
        sNormalHrValueRange = normalHrValueRange;
        sNormalHrUpdateInterval = normalHrUpdateInterval;
        sDangerHrUpdateInterval = dangerHrUpdateInterval;
        sAbnormalHrUpdateInterval = abnormalHrUpdateInterval;
    }

    public void registerHealthListener() {
        if (mSensorManager == null) {
            mSensorManager = (SensorManager) KApplication.sContext.getSystemService(Context.SENSOR_SERVICE);
        }
        if (mSensor == null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
        if (mHealthSensorEventListener == null) {
            mHealthSensorEventListener = new HealthSensorEventListener();
        }
        mSensorManager.registerListener(mHealthSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unRegisterHealthListener() {
        if (null != mSensor && mSensorManager != null && mHealthSensorEventListener != null) {
            mSensorManager.unregisterListener(mHealthSensorEventListener, mSensor);
        }
        mHealthSensorEventListener = null;
        mSensor = null;
        mSensorManager = null;
    }

    /**
     * 将原始数据解析转化成HealthInfo
     *
     * @param value 传感器的原始数据，因为同用一个接口，所以心率、血压乘以不同倍数（心率10倍、舒张压100倍、收缩压1000倍）
     *              以不同区间来区分心率和血压数据，应用层需除去这些倍数
     * @return HealthInfo
     */
    public static HealthInfo parseValue(float value) {
        int mValueType;
        if (200f < value) {
            if (value < 2000f) {
                mValueType = HealthInfo.TYPE_HEARTRATE;
                value = value / 10;
            } else if (value < 15000f) {
                mValueType = HealthInfo.TYPE_DBP;
                value = value / 100;
            } else if (value < 60000f) {
                return null;//空区间
            } else if (value < 360000f) {
                mValueType = HealthInfo.TYPE_SBP;
                value = value / 1000;
            } else if (value == 1000000f) {
                mValueType = HealthInfo.TYPE_LTH;
            } else {
                return null;
            }
        } else {
            return null;
        }
        return new HealthInfo(System.currentTimeMillis(), mValueType, (int) value, System.currentTimeMillis());
    }

    /**
     * @param heartrate
     * @return 返回心率值所反应的健康状态 STATE_DANGER_HR 、 STATE_ABNORMAL_HR 、STATE_NORMAL_HR
     */
    public static int getHeartrateState(int heartrate) {
        if (heartrate < sMinDangerHrValue + sDangerHrValueRange) {
            return STATE_DANGER_HR;
        } else if (heartrate < sNormalHrValue - sNormalHrValueRange) {
            return STATE_ABNORMAL_HR;
        } else if (heartrate < sNormalHrValue + sNormalHrValueRange) {
            return STATE_NORMAL_HR;
        } else if (heartrate < sMaxDangerHrValue - sDangerHrValueRange) {
            return STATE_ABNORMAL_HR;
        } else {
            return STATE_DANGER_HR;
        }
    }

    public static long getHeartrateUpdateInterval(HealthInfo healthInfo) {
        return healthInfo.getType() == HealthInfo.TYPE_HEARTRATE
                ? getHeartrateUpdateInterval(getHeartrateState(healthInfo.getValue()))
                : sNormalHrUpdateInterval;
    }

    public static long getHeartrateUpdateInterval(int heartrateState) {
        switch (heartrateState) {
            case STATE_NORMAL_HR:
                return sNormalHrUpdateInterval;
            case STATE_ABNORMAL_HR:
                return sAbnormalHrUpdateInterval;
            case STATE_DANGER_HR:
                return sDangerHrUpdateInterval;
            default:
                return 0;
        }
    }

    class HealthSensorEventListener implements SensorEventListener {
        float tempValue = 0;

        @Override
        public void onSensorChanged(SensorEvent event) {
            float value = event.values[0];
            Log.d("chenjp", "onSensorChanged: "+value);
            if (value > 2000f) {//暂时过滤非心率数据
                return;
            }
            if (tempValue == 0) {
                tempValue = value;
            }
            if (tempValue != value) {
                mHealthInfo = parseValue(value);
                if (mHealthInfo != null) {
                    HealthDaoUtils.instance().update(mHealthInfo);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    public HealthInfo getHealthInfo() {
        return mHealthInfo;
    }

    public void autoUpdateHRValue() {
        mExecutorService = new ScheduledThreadPoolExecutor(1);
        mAutoUpdateRunnable = new AutoUpdateRunnable();
        mExecutorService.schedule(mAutoUpdateRunnable,getHeartrateUpdateInterval(mHealthInfo),TimeUnit.MILLISECONDS);
    }

    class AutoUpdateRunnable implements Runnable {

        @Override
        public void run() {
            //TODO 上传数据
            mExecutorService.schedule(mAutoUpdateRunnable,getHeartrateUpdateInterval(mHealthInfo),TimeUnit.MILLISECONDS);
        }
    }
}
