package com.android.fisewatchlauncher.event;

import android.telephony.TelephonyManager;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4
 * @time 15:09
 */
public class DataConnectionStateChangedEvent {

    public int state;

    public DataConnectionStateChangedEvent(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DataConnectionStateChangedEvent{" +
                "state=" + state +
                '}';
    }

    public String format2String(int state) {
        String result = "";
        switch (state) {
            case TelephonyManager.DATA_CONNECTED:
                result = "DATA_CONNECTED";
                break;
            case TelephonyManager.DATA_CONNECTING:
                result = "DATA_CONNECTING";
                break;
            case TelephonyManager.DATA_DISCONNECTED:
                result = "DATA_DISCONNECTED";
                break;
            case TelephonyManager.DATA_SUSPENDED:
                result = "DATA_SUSPENDED";
                break;
        }
        return result;
    }
}
