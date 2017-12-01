package com.android.fisewatchlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.fisewatchlauncher.utils.AlarmManagerUtil;
import com.android.fisewatchlauncher.utils.SilenceTimeUtils;

import java.util.Calendar;

import static com.android.fisewatchlauncher.utils.AlarmManagerUtil.ALARM_TIME_HOUR;
import static com.android.fisewatchlauncher.utils.AlarmManagerUtil.ALARM_TIME_MIN;
import static com.android.fisewatchlauncher.utils.AlarmManagerUtil.ALARM_TIME_MODE_NULL;

/**
 * Created by ChenJP on 2017/9/20.
 */
public class SilenceTimeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //设置明天闹钟
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                        (Calendar.DAY_OF_MONTH),
                intent.getIntExtra(ALARM_TIME_HOUR, 0),
                intent.getIntExtra(ALARM_TIME_MIN, 0), 0);
        AlarmManagerUtil.setAlarmOfMillis(context, calendar.getTimeInMillis() + 1000 * 60 * 60 * 24, intent);

        Log.d("chenjp", "onReceive: silenceTime");
        //过滤周六日
        if (calendar.get(Calendar.DAY_OF_WEEK)== Calendar.SUNDAY||calendar.get(Calendar.DAY_OF_WEEK)== Calendar.SATURDAY) {
            Log.d("chenjp", "今天周六日不禁用");
            return;
        }
        //实际操作
        int silenceMode = intent.getIntExtra(AlarmManagerUtil.ALARM_TIME_MODE, ALARM_TIME_MODE_NULL);
        if (silenceMode == SilenceTimeUtils.SILENCE_TIME_MODE_ON) {
            SilenceTimeUtils.handleSilenceTimeMode(true);
        } else if (silenceMode == SilenceTimeUtils.SILENCE_TIME_MODE_OFF) {
            SilenceTimeUtils.handleSilenceTimeMode(false);
        }
    }


}
