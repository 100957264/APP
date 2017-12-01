package com.android.fisewatchlauncher.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.entity.dao.AlarmInfo;
import com.android.fisewatchlauncher.prenster.dao.AlarmClockDaoUtils;

import java.util.Calendar;
import java.util.List;

/**
 *
 * Created by ChenJP on 2017/9/16.
 */
public class AlarmManagerUtil {
    public static final String ALARM_ACTION                 = "com.fise.alarm.clock";
    public static final String RING_MODE                    = "ring_mode";
    public static final int    RING_MODE_VIBRATOR           = 0;
    public static final int    RING_MODE_SOUND              = 1;
    public static final int    RING_MODE_SOUND_AND_VIBRATOR = 2;
    public static final String SP_ALARM                     = "sp_fise_alarm";
    public static final String SP_KEY_DISPOSABLE_ALARM      = "disposable_alarm";
    public static final long   LENGTH_OF_DAY_IN_MILLIS      = 60 * 60 * 24 * 1000;
    public static final String ALARM_TIME_MODE              = "alarm_time_mode";
    public static final int    ALARM_TIME_MODE_NULL         = 0;
    public static final int    ALARM_TIME_MODE_A_ONE_TIME   = 1;
    public static final int    ALARM_TIME_MODE_EVERYDAY     = 2;
    public static final int    ALARM_TIME_MODE_CUSTOMS      = 3;
    public static final String ALARM_TIME_HOUR              = "alarm_time_hour";
    public static final String ALARM_TIME_MIN               = "alarm_time_min";
    public static final String REPEAT_DAY_OF_WEEK           = "repeat_day_of_week";

    public static void setAlarm(int hour, int minute, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), hour, minute, 0);
        long alarmTimeInMillis = calendar.getTimeInMillis();
        alarmTimeInMillis = alarmTimeInMillis > System.currentTimeMillis() ? alarmTimeInMillis : alarmTimeInMillis + LENGTH_OF_DAY_IN_MILLIS;
        setAlarmOfMillis(KApplication.sContext, alarmTimeInMillis, intent);
    }


    public static void setAlarmOfMillis(Context context, long timeInMillis, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, intent.getIntExtra("id", 0),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
        }
    }

    public static void cancelAlarm(Context context, String action, int id) {
        Intent intent = new Intent(action);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }

    /**
     * @return 下一个闹钟毫秒值，为 -1 时没有下一个闹钟
     */
    public static long getNextAlarmTriggerAtMillis() {
        long nextAlarmTriggerAtMillis = System.currentTimeMillis() + 8 * LENGTH_OF_DAY_IN_MILLIS;
        long tempNextAlarmTriggerAtMillis = -1;
        Calendar calendar = Calendar.getInstance();
        List<AlarmInfo> alarmInfos = AlarmClockDaoUtils.instance().selectAll();
        for (AlarmInfo alarmInfo : alarmInfos) {
            if (!alarmInfo.isOpen()) {
                continue;
            }
            if (alarmInfo.getMode() == ALARM_TIME_MODE_A_ONE_TIME) {
                if (alarmInfo.getTriggerAtMillis() > System.currentTimeMillis()) {
                    tempNextAlarmTriggerAtMillis = alarmInfo.getTriggerAtMillis();
                } else {
                    alarmInfo.setIsOpen(false);
                    AlarmClockDaoUtils.instance().update(alarmInfo);
                }

            } else if (alarmInfo.getMode() == ALARM_TIME_MODE_EVERYDAY) {
                long triggerAtMillis = getTriggerAtMillisOfAlarmInfo(calendar, alarmInfo);
                if (triggerAtMillis < System.currentTimeMillis()) {
                    triggerAtMillis = triggerAtMillis + LENGTH_OF_DAY_IN_MILLIS;
                }
                tempNextAlarmTriggerAtMillis = triggerAtMillis;
            } else if (alarmInfo.getMode() == ALARM_TIME_MODE_CUSTOMS) {
                long triggerAtMillis = getTriggerAtMillisOfAlarmInfo(calendar, alarmInfo);
                String repeatDayOfWeek = alarmInfo.getRepeatDayByWeek();
                int offsetDay = getOffsetDay(calendar, repeatDayOfWeek);
                if (offsetDay < 0) {
                    continue;
                }
                tempNextAlarmTriggerAtMillis = triggerAtMillis + offsetDay * LENGTH_OF_DAY_IN_MILLIS;
            }
            if (0 < tempNextAlarmTriggerAtMillis && tempNextAlarmTriggerAtMillis < nextAlarmTriggerAtMillis) {
                nextAlarmTriggerAtMillis = tempNextAlarmTriggerAtMillis;
            }
        }
        if (tempNextAlarmTriggerAtMillis < 0) {
            return -1;
        }
        return nextAlarmTriggerAtMillis < System.currentTimeMillis() ? -1 : nextAlarmTriggerAtMillis;
    }

    public static long getTriggerAtMillisOfOnceAlarmInfo(AlarmInfo alarmInfo) {
        long timeMillis = -1;
        if (alarmInfo.getMode() == ALARM_TIME_MODE_A_ONE_TIME) {
            timeMillis = getTriggerAtMillisOfAlarmInfo(Calendar.getInstance(), alarmInfo);
            if (timeMillis < System.currentTimeMillis()) {
                timeMillis = timeMillis + LENGTH_OF_DAY_IN_MILLIS;
            }
        }
        return timeMillis;
    }

    public static long getTriggerAtMillisOfAlarmInfo(Calendar calendar, AlarmInfo alarmInfo) {
        String[] timeSplit = alarmInfo.getTime().split(":");
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                Integer.parseInt(timeSplit[0]),
                Integer.parseInt(timeSplit[1]),
                0);
        return calendar.getTimeInMillis();
    }

    /**
     * @param calendar
     * @param repeatDayOfWeek
     * @return 距离最近闹钟的天数，-1时表示没有一天是开启的
     */
    public static int getOffsetDay(Calendar calendar, String repeatDayOfWeek) {
        int offsetDay = -1;
        int index = calendar.getTimeInMillis() < System.currentTimeMillis() ? 1 : 0;
        //重组闹钟生效日的String，将今天排在第一位和最后一位
        repeatDayOfWeek = repeatDayOfWeek.substring(
                //calendar.get(Calendar.DAY_OF_WEEK)返回值周日为1，周一为2，以此类推
                calendar.get(Calendar.DAY_OF_WEEK) - 1) +
                repeatDayOfWeek.substring(0, calendar.get(Calendar.DAY_OF_WEEK) - 1) +
                repeatDayOfWeek.charAt(calendar.get(Calendar.DAY_OF_WEEK) - 1);
        Log.d("chenjp", "重组闹钟生效日: " + repeatDayOfWeek);
        while (index < repeatDayOfWeek.length()) {
            if ("1".equals(repeatDayOfWeek.charAt(index) + "")) {
                offsetDay = index;
                break;
            }
            index++;
        }
        Log.d("chenjp", offsetDay + "天之后有自定义闹钟");
        return offsetDay;
    }

}
