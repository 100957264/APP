package com.android.fisewatchlauncher.manager;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.fisewatchlauncher.BuildConfig;
import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.function.alert.AlertManager;
import com.android.fisewatchlauncher.function.location.AMapLocationManager;
import com.android.fisewatchlauncher.function.location.FunctionLocManager;
import com.android.fisewatchlauncher.function.location.GoogleManager;
import com.android.fisewatchlauncher.function.weather.WeatherPresenter;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.util.Locale;

/**
 * @author mare
 * @Description:TODO 判断用高德还是谷歌地图还是原生API
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/16
 * @time 20:48
 */
public class LocationManager {
    private LocationManager() {
    }

    private static class SingletonHolder {
        private static final LocationManager INSTANCE = new LocationManager();
    }

    public static LocationManager instance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean isGsm(TelephonyManager tm) {
        return tm.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM;
    }

    public boolean isCdma(TelephonyManager tm) {
        return tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA;
    }

    /**
     * 高德地图 谷歌地图 谷歌原生API
     */
    public enum LocationPickerType {
        AMAP(0), GOOGLE(1), Native(2);
        int type;

        LocationPickerType(int type) {
            this.type = type;
        }
    }

    /**
     * 判断用谷歌还是高德地图还是android原生API
     *
     * @return
     */
    public LocationPickerType getLocationType() {
        boolean isSmart = BuildConfig.isSmartProtocol;
        LocationPickerType type;
        TelephonyManager tm = (TelephonyManager) KApplication.sContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (null == tm) {
            LogUtils.e("tm 还没插入SIM卡 ");
            return null;
        }
        // 返回值MCC + MNC
        LogUtils.e("tm: " + tm);
        String operator = tm.getNetworkOperator();
        LogUtils.e("operator " + operator);
        boolean isOverSeas;
        if (TextUtils.isEmpty(operator)) {
            isOverSeas = isCN(tm);
        } else {
            String mcc = operator.substring(0, 3);
            isOverSeas = !TextUtils.equals(mcc, "460");//是否身在海外
        }

        if (!isSmart) {//非智能机统一用Native API
            type = LocationPickerType.Native;
        } else if (isOverSeas) {// 身在海外
            type = LocationPickerType.GOOGLE;
        } else {// 身在海内
            type = LocationPickerType.AMAP;
        }
        return type;
    }

    private boolean isCN(TelephonyManager tm) {
        String countryIso = tm.getSimCountryIso();
        boolean isCN = false;//判断是不是大陆
        if (!TextUtils.isEmpty(countryIso)) {
            countryIso = countryIso.toUpperCase(Locale.US);
            if (countryIso.contains("CN")) {
                isCN = true;
            }
        }
        return isCN;
    }

    /**
     * 开始定位
     */
    public void startLocate() {
        LocationPickerType type = getLocationType();
        switch (type) {
            case Native:
                //TODO Native API
                FunctionLocManager.instance().start();
                break;
            case GOOGLE:
                //TODO Google Map
                GoogleManager.instance().startLocate();
                break;
            case AMAP:
                //TODO Amap
                AMapLocationManager.instance().start();
                break;
            default:
                break;
        }
    }

    public void initLocationSDK() {
        LocationPickerType type = getLocationType();
        switch (type) {
            case Native:
                //TODO Native API
                //do nothing
                break;
            case GOOGLE:
                //TODO Google Map
                GoogleManager.instance().initLocationSDK();
                break;
            case AMAP:
                //TODO Amap
                AMapLocationManager.instance().initLocationSDK();
                break;
            default:
                break;
        }
    }

    /**
     * 停止定位
     */
    public void stopLocate() {
        LocationPickerType type = getLocationType();
        switch (type) {
            case Native:
                //TODO Native API
                FunctionLocManager.instance().stop();
                break;
            case GOOGLE:
                //TODO Google Map
                GoogleManager.instance().stopLocate();
                break;
            case AMAP:
                //TODO Amap
                AMapLocationManager.instance().stop();
                break;
            default:
                break;
        }
    }

    /**
     * SOS上传警情信息
     */
    public void sendAlert() {
        boolean isSmart = BuildConfig.isSmartProtocol;
        if (isSmart) {
            AlertManager.instance().sendSmartAlert();
        } else {
            AlertManager.instance().sendFunctionAlert();
        }
    }

    /**
     * TODO 请求天气信息
     */
    public void requestWeather(){
        boolean isSmart = BuildConfig.isSmartProtocol;
        if (isSmart) {
            WeatherPresenter.requestSmartWeather();
        } else {
            WeatherPresenter.requestFunctionWeather();
        }
    }

}
