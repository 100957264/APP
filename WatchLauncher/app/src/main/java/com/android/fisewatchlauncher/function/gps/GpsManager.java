package com.android.fisewatchlauncher.function.gps;

import android.text.TextUtils;

import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.entity.msg.GPSPoint;
import com.android.fisewatchlauncher.function.location.FunctionLocManager;
import com.android.fisewatchlauncher.utils.LogUtils;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/16
 * @time 14:52
 */
public class GpsManager {
    private GpsManager() {
    }

    private static class SingletonHolder {
        private static final GpsManager INSTANCE = new GpsManager();
    }

    public static GpsManager instance() {
        return SingletonHolder.INSTANCE;
    }

    private GPSResponsListener listener;

    public void start(GPSResponsListener listener) {
        this.listener = listener;

    }

    private void stop() {

    }

    /**
     * 解析服务器返回的经纬度
     *
     * @param content
     */
    public void parseGpsPoint(String content) {
//        BASE,22.571707,N,113.8613968,E
        if (TextUtils.isEmpty(content)) {
            return;
        }
        String[] results = TextUtils.split(content, GlobalSettings.MSG_CONTENT_SEPERATOR);
        if (null ==results || results.length<5) {
            LogUtils.e("parseGpsPoint 格式不对...");
            return;
        }
        switch (results[0]) {
            case FunctionLocManager.LOCATION_TYPE_BASE:
                break;
            case FunctionLocManager.LOCATION_TYPE_WIFI:
                break;
            default:
                break;
        }
        GPSPoint point = new GPSPoint(Double.parseDouble(results[1]),results[2],Double.parseDouble(results[3]),results[4]);

        //TODO 存到StaticManager里面
        FunctionLocManager.instance().upload();
    }
}
