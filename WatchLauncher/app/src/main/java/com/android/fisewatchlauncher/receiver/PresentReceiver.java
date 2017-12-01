package com.android.fisewatchlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sean on 8/30/16.
 */
public class PresentReceiver extends BroadcastReceiver {
    private final static String TAG = "PresentReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive PresentReceiver!!!!");
    }
}
