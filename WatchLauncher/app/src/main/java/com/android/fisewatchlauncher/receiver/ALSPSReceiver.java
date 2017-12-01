package com.android.fisewatchlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.fisewatchlauncher.function.alert.AlertManager;
import com.android.fisewatchlauncher.manager.StaticManager;
import com.android.fisewatchlauncher.utils.LogUtils;

/**
 * Created by qingfeng on 2017/11/14.
 */

public class ALSPSReceiver extends BroadcastReceiver {
    public static final float DISTANCE_LONG = 10.0f;
    public static final float DISTANCE_SHORT = 0.0f;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtils.d("receive ALSPS broadcast....intent =" + intent);
        if (action.equals("com.fise.intent.ACTION_PSENSOR")) {
            Bundle bundle = intent.getExtras();
            float value = 0;
            try {
                value = bundle.getFloat("psensor");
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e("获取脱落参数出错 " + e);
                return;
            }
            LogUtils.d("receive ALSPS broadcast....value =" + value);
            if (value == DISTANCE_LONG) {//远离
                StaticManager.instance().setTakenOff(true);
                AlertManager.instance().sendSmartAlert();
                LogUtils.e("ALSPSReceiver " + true);
            } else if (value == DISTANCE_SHORT) {//靠近
                StaticManager.instance().setTakenOff(false);
                AlertManager.instance().sendSmartAlert();
                LogUtils.e("ALSPSReceiver " + false);
            }
        }
    }
}
