package com.android.fisewatchlauncher.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.listener.CustomPhoneStateListener;


/**
 * Created by qingfeng on 2017/9/22.
 */

public class NetorkManager {
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    private static class SingletonHolder {
        private static final NetorkManager INSTANCE = new NetorkManager();
    }
    public static final int MAX_LEVEL = 5;
    public static int[] SIGNAL_ICON = new int[]{
            R.drawable.ic_signal_0,
            R.drawable.ic_signal_1,
            R.drawable.ic_signal_2,
            R.drawable.ic_signal_3,
            R.drawable.ic_signal_4,
    };
    public static int[] WIFI_ICON = new int[]{
            R.drawable.ic_wifi_0,
            R.drawable.ic_wifi_1,
            R.drawable.ic_wifi_2,
            R.drawable.ic_wifi_3,
            R.drawable.ic_wifi_4,
    };
    public static NetorkManager instance() {
        return SingletonHolder.INSTANCE;
    }
    public boolean hasInsertSIM(){
        TelephonyManager tm = (TelephonyManager)KApplication.sContext.getSystemService(Context.TELEPHONY_SERVICE);
        if(tm != null && tm.hasIccCard()){
            return true;
        }
        return false;
    }
    public void registPhoneStateListener(){
        TelephonyManager tm = (TelephonyManager)KApplication.sContext.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(new CustomPhoneStateListener(KApplication.sContext),PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        connectivityManager = (ConnectivityManager) KApplication.sContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

    }
    public int getWifiStrength(){
        int wifiStrength = 0;
        WifiManager wifiManager = (WifiManager)KApplication.sContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if(wifiInfo.getBSSID() != null){
            wifiStrength = WifiManager.calculateSignalLevel(wifiInfo.getRssi(),5);
        }
        return wifiStrength;
    }
    public boolean isWifiOn(){
        boolean isWifiOn = false;
        if(networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
            isWifiOn = true;
        }
        return isWifiOn;
    }
    public String getNetworkType(int type){
        String networkType = "";
                switch (type){
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        networkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        networkType = "E";
                        break;
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        networkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        networkType = "4G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        networkType = "";
                        break;
                }
        return networkType;
    }
}
