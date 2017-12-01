package com.android.fisewatchlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.android.fisewatchlauncher.event.WifiStateChangedEvent;
import com.android.fisewatchlauncher.manager.StaticManager;
import com.android.fisewatchlauncher.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by qingfeng on 2017/9/25.
 */

public class WifiStateReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        WifiStateChangedEvent event = new WifiStateChangedEvent();
        if(intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
            event.state = intent.getIntExtra("wifi_state",0);
        }else if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
            final NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(networkInfo != null && networkInfo.isConnected()){
                event.connected = true;
                final WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                if (wifiInfo != null){
                    event.wifiStrength =WifiManager.calculateSignalLevel(wifiInfo.getRssi(),5);
                }
            }
        }else  if(intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)){
            event.wifiStrength = WifiManager.calculateSignalLevel(intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI,-500),5);
        }
        LogUtils.i("WifiStateReceiver: state =" + event.state + ",wifiStrength ="+ event.wifiStrength );
        EventBus.getDefault().post(event);
        StaticManager.instance().wifiStateChangedEvent =event;
    }
}
