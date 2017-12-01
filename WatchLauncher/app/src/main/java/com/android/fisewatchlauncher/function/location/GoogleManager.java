package com.android.fisewatchlauncher.function.location;

/**
 * @author mare
 * @Description:TODO 谷歌相关功能(地图 定位 导航 推送)
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/13
 * @time 13:46
 */
public class GoogleManager {
    private GoogleManager() {
    }

    private static class SingletonHolder {
        private static final GoogleManager INSTANCE = new GoogleManager();
    }

    public static GoogleManager instance() {
        return SingletonHolder.INSTANCE;
    }

    public void initLocationSDK() {
    }

    /**
     * 开始定位
     */
    public void startLocate() {
        GoogleMapManager.instance().start();
    }

    /**
     * 停止定位
     */
    public void stopLocate() {
        GoogleMapManager.instance().stop();
    }

}
