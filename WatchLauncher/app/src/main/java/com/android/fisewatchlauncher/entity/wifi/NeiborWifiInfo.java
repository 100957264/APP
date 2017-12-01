package com.android.fisewatchlauncher.entity.wifi;

import android.net.wifi.WifiInfo;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4
 * @time 18:09
 */
public class NeiborWifiInfo {

    public static final String LINK_SPEED_UNITS = WifiInfo.LINK_SPEED_UNITS;
    public String ssid;
    public String mac;
    public int rssi;
    public int linkSpeed;
    public String bSsid;

    public NeiborWifiInfo(String ssid, String mac, int rssi) {
        this.ssid = ssid;
        this.mac = mac;
        this.rssi = rssi;
    }

    public NeiborWifiInfo(String ssid, String mac, int rssi, String bSsid) {
        this.ssid = ssid;
        this.mac = mac;
        this.rssi = rssi;
        this.bSsid = bSsid;
    }

    public NeiborWifiInfo(String ssid, String mac, int rssi, String bSsid, int linkSpeed) {
        this.ssid = ssid;
        this.mac = mac;
        this.rssi = rssi;
        this.bSsid = bSsid;
        this.linkSpeed = linkSpeed;
    }

    @Override
    public String toString() {
        return "NeiborWifiInfo{" +
                "ssid='" + ssid + '\'' +
                ", mac='" + mac + '\'' +
                ", rssi=" + rssi +
                ", bSsid='" + bSsid + '\'' +
                ", linkSpeed=" + linkSpeed + LINK_SPEED_UNITS +
                '}';
    }

    public String format() {
        return ssid + "," + mac + "," + rssi;
    }
}
