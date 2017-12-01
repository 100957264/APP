package com.android.fisewatchlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.fisewatchlauncher.acty.FiseKeyguardActivity;
import com.android.fisewatchlauncher.event.ScreenOffEvent;
import com.android.fisewatchlauncher.manager.PreferControler;
import com.android.fisewatchlauncher.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by sean on 9/13/16.
 */
public class ScreenStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtils.i("onReceive ScreenStatusReceiver:" + action);
        if (action.equals(Intent.ACTION_SCREEN_ON)) {

        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {

            if (PreferControler.instance().isLockScreenEnable()) {
                EventBus.getDefault().post(new ScreenOffEvent());
                FiseKeyguardActivity.showLockScreen(context);
            }
        }
    }
}