package com.android.fisewatchlauncher.function.location;

import android.text.TextUtils;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.function.alarm.AlarmTimer;
import com.android.fisewatchlauncher.manager.LocationManager;
import com.android.fisewatchlauncher.prenster.dao.CenterSettingsUtils;
import com.android.fisewatchlauncher.utils.LogUtils;


/**
 * @author mare
 * @Description:TODO 固定位置上传
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/20
 * @time 17:23
 */
public class LocationUploadManager {

    private LocationUploadManager() {
    }

    private static class SingletonHolder {
        private static final LocationUploadManager INSTANCE = new LocationUploadManager();
    }

    public static LocationUploadManager instance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 开启定时定位
     *
     * @param content
     */
    public void parseInteraval(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        long interal = Long.parseLong(content) * 1000;//单位是秒
        LogUtils.e("location 定时时间间隔 : " + interal);
        CenterSettingsUtils.instance().updateUploadInteral(interal);
        LocationManager.instance().startLocate();//开启第一次定位
        AlarmTimer.startConfirmedFrequencyUpload(KApplication.sContext);
    }
}
