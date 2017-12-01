package com.android.fisewatchlauncher.entity.wifi;

/**
 * @author mare
 * @Description:TODO 同步Wifi所用到的JavaBean
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/3
 * @time 17:40
 */
public class WifiSyncBean {
    public String ssid;
    public String pwd;
    public WifiDataType type;//免密码 WEP WPA WPA2
    public boolean isNeedResult = false;//是否要wifi搜索结果(默认false)

    public WifiSyncBean(String ssid, String pwd) {
        this(ssid, pwd, WifiDataType.WIFI_CIPHER_WPA);//(默认WPA)
    }

    public WifiSyncBean(String ssid, String pwd, WifiDataType type) {
        this(ssid, pwd,type,false);//(默认不需要wifi列表)
    }

    public WifiSyncBean(String ssid, String pwd, WifiDataType type, boolean isNeedResult) {
        this.ssid = ssid;
        this.pwd = pwd;
        this.type = type;
        this.isNeedResult = isNeedResult;
    }

    @Override
    public String toString() {
        return "WifiSyncBean{" +
                "ssid='" + ssid + '\'' +
                ", pwd='" + pwd + '\'' +
                ", type=" + type.toString() +
                ", isNeedResult=" + isNeedResult +
                '}';
    }
}
