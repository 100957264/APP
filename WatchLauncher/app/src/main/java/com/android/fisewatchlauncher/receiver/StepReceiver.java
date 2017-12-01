package com.android.fisewatchlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.fisewatchlauncher.constant.ReceiverConstant;
import com.android.fisewatchlauncher.entity.dao.StepHistory;
import com.android.fisewatchlauncher.event.StepUpdateEvent;
import com.android.fisewatchlauncher.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/31
 * @time 11:26
 */
public class StepReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (ReceiverConstant.ACTION_STEP .equals(intent.getAction())){
            int currentStep = intent.getIntExtra(ReceiverConstant.EXTRA_STEP_COUNT,0);
            LogUtils.i("currentStep " + currentStep);
            StepHistory history = new StepHistory(currentStep, System.currentTimeMillis());
            EventBus.getDefault().post( new StepUpdateEvent(history));
        }
    }
}
