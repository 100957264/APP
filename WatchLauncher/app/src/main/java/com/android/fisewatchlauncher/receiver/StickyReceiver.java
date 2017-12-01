package com.android.fisewatchlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.fisewatchlauncher.service.FiseService;

/**
 * Created by sean on 9/21/16.
 */
public class StickyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("StickyReceiver", "action:" + action);
        FiseService.pull(context);
    }
}
