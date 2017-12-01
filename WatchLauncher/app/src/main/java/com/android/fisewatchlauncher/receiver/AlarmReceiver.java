package com.android.fisewatchlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.fisewatchlauncher.acty.ClockAlarmActivity;
import com.android.fisewatchlauncher.function.alarm.RemindUtils;
import com.android.fisewatchlauncher.utils.AlarmManagerUtil;

/**
 *
 * Created by ChenJP on 2017/9/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        RemindUtils.setNearAlarm();
        String msg = intent.getStringExtra("msg");
        int flag = intent.getIntExtra(AlarmManagerUtil.RING_MODE, AlarmManagerUtil.RING_MODE_SOUND_AND_VIBRATOR);
        Intent clockIntent = new Intent(context, ClockAlarmActivity.class);
        clockIntent.putExtra("msg", msg);
        clockIntent.putExtra("flag", flag);
        clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(clockIntent);
    }


}
