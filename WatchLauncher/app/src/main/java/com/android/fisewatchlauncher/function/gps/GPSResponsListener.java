package com.android.fisewatchlauncher.function.gps;

/**
 * @author mare
 * @Description:TODO GPS定位结果监听
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/16
 * @time 14:18
 */
public interface GPSResponsListener {
    void onLocateFailed(GpsErrorType errorType);

    void onLocateSuccess(GpsBean result);
}
