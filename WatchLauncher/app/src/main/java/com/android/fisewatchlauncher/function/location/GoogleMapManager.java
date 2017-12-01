package com.android.fisewatchlauncher.function.location;

import com.android.fisewatchlauncher.utils.LogUtils;

/**
 * @author mare
 * @Description:TODO 谷歌地图定位
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/16
 * @time 19:35
 */
public class GoogleMapManager {

    private GoogleMapManager() {
    }

    private static class SingletonHolder {
        private static final GoogleMapManager INSTANCE = new GoogleMapManager();
    }

    public static GoogleMapManager instance() {
        return SingletonHolder.INSTANCE;
    }

    public void start() {
        LogUtils.e("开始定位了....");
    }

    public void stop(){
        LogUtils.e("停止定位....");
    }


}
