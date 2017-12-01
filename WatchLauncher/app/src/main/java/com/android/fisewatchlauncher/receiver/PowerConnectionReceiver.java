package com.android.fisewatchlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.android.fisewatchlauncher.event.PowerConnection;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by sean on 8/30/16.
 */
public class PowerConnectionReceiver extends BroadcastReceiver {
    private final static String TAG = "PowerConnectionReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "PowerConnectionReceiver receive:" + action);
        if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
            EventBus.getDefault().post(new PowerConnection(true));
        } else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            EventBus.getDefault().post(new PowerConnection(false));
        }
    }
}
