package com.android.fisewatchlauncher.entity.wifi;

import android.net.wifi.WifiInfo;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4
 * @time 18:10
 */
public class ConnectedWifiInfo {

    public static final String LINK_SPEED_UNITS = WifiInfo.LINK_SPEED_UNITS;
    public String ssid;
    public String mac;
    public int rssi;
    public String bSsid;
    public int linkSpeed;

    public ConnectedWifiInfo(String ssid, String mac, int rssi) {
        this(ssid, mac, rssi, null);
    }

    public ConnectedWifiInfo(String ssid, String mac, int rssi, String bSsid) {
        this(ssid, mac, rssi, bSsid, 0);
    }

    public ConnectedWifiInfo(String ssid, String mac, int rssi, String bSsid, int linkSpeed) {
        this.ssid = ssid;
        this.mac = mac;
        this.rssi = rssi;
        this.bSsid = bSsid;
        this.linkSpeed = linkSpeed;
    }

    @Override
    public String toString() {
        return "ConnectedWifiInfo{" +
                "ssid='" + ssid + '\'' +
                ", mac='" + mac + '\'' +
                ", rssi=" + rssi +
                ", bSsid='" + bSsid + '\'' +
                ", linkSpeed=" + linkSpeed + LINK_SPEED_UNITS +
                '}';
    }
}
