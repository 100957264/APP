package com.android.fisewatchlauncher.entity.wifi;

/**
 * @author mare
 * @Description:TODO 密码加密类型
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/3
 * @time 17:43
 */
public enum  WifiDataType {
    WIFI_CIPHER_NOPASS(0), WIFI_CIPHER_WEP(1), WIFI_CIPHER_WPA(2), WIFI_CIPHER_WPA2(3);

    private final int value;

    WifiDataType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
