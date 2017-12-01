package com.android.fisewatchlauncher.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.constant.FileConstant;
import com.android.fisewatchlauncher.entity.schedule.SilenceTimeInfo;
import com.android.fisewatchlauncher.event.DisturbSwitchEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenJP on 2017/9/20.
 */

public class SilenceTimeUtils {

    public static final String SP_SILENCE_TIME       = "silence_time";
    public static final String SP_KEY_SILENCE_TIME   = "key_silence_time";
    public static final String ACTION_SILENCE_TIME   = "android.intent.action.silence.time";
    public static final int    SILENCE_TIME_MODE_OFF = -1;
    public static final int    SILENCE_TIME_MODE_ON  = 1;
    public static final String IS_SILENCE_TIME       = "is_silence_time";

    /**
     * @param oldSilenceTimeData 需要取消的旧数据
     */
    public static void initSilenceTime(String oldSilenceTimeData, String newSilenceTimeData) {
        if (!TextUtils.isEmpty(oldSilenceTimeData)) {
            cancelSilenceAlarm(oldSilenceTimeData);
        }
        setSilenceTimeContent(newSilenceTimeData);
        List<SilenceTimeInfo> silenceTimeInfos = parseData(newSilenceTimeData);
        boolean isInTimeScope = false;
        for (SilenceTimeInfo silenceTimeInfo : silenceTimeInfos) {
            setAlarm(silenceTimeInfo);
            if (isCurrentInTimeScope(silenceTimeInfo)) {
                isInTimeScope = true;
            }
        }
        handleSilenceTimeMode(isInTimeScope);
    }

    public static String getSilenceTimeContent() {
        SharedPreferences SP = KApplication.sContext.getSharedPreferences(SP_SILENCE_TIME, Context.MODE_PRIVATE);
        return SP.getString(SilenceTimeUtils.SP_KEY_SILENCE_TIME, "");
    }

    public static void setSilenceTimeContent(String newSilenceTimeData) {
        SharedPreferences SP = KApplication.sContext.getSharedPreferences(SP_SILENCE_TIME, Context.MODE_PRIVATE);
        SP.edit().putString(SilenceTimeUtils.SP_KEY_SILENCE_TIME, newSilenceTimeData).apply();
    }

    private static void cancelSilenceAlarm(String oldSilenceTimeData) {
        List<SilenceTimeInfo> oldSilenceTimeInfos = parseData(oldSilenceTimeData);
        for (SilenceTimeInfo oldSilenceTimeInfo : oldSilenceTimeInfos) {
            if (oldSilenceTimeInfo.getBeginHour()==oldSilenceTimeInfo.getEndHour()&&
                    oldSilenceTimeInfo.getBeginMin()==oldSilenceTimeInfo.getEndMin()) {
                continue;
            }
            AlarmManagerUtil.cancelAlarm(KApplication.sContext, ACTION_SILENCE_TIME, oldSilenceTimeInfo.getBeginHour() * 100 + oldSilenceTimeInfo.getBeginMin());
            Log.d("chenjp", "取消开始勿扰定时: " + oldSilenceTimeInfo.getBeginHour() + ":" + oldSilenceTimeInfo.getBeginMin());
            AlarmManagerUtil.cancelAlarm(KApplication.sContext, ACTION_SILENCE_TIME, oldSilenceTimeInfo.getEndHour() * 100 + oldSilenceTimeInfo.getEndMin());
            Log.d("chenjp", "取消结束勿扰定时: " + oldSilenceTimeInfo.getEndHour() + ":" + oldSilenceTimeInfo.getEndMin());
        }
    }

    private static void setAlarm(SilenceTimeInfo silenceTimeInfo) {
        if (silenceTimeInfo.getBeginHour() == silenceTimeInfo.getEndHour() &&
                silenceTimeInfo.getBeginMin() == silenceTimeInfo.getEndMin()) {
            Log.d("chenjp", "过滤开始结束时间相同的勿扰时段: " +
                    silenceTimeInfo.getBeginHour() + ":" + silenceTimeInfo.getBeginMin());
            return;
        }
        Intent beginIntent = new Intent(ACTION_SILENCE_TIME);
        beginIntent.putExtra(AlarmManagerUtil.ALARM_TIME_MODE, SILENCE_TIME_MODE_ON);
        beginIntent.putExtra(AlarmManagerUtil.ALARM_TIME_HOUR, silenceTimeInfo.getBeginHour());
        beginIntent.putExtra(AlarmManagerUtil.ALARM_TIME_MIN, silenceTimeInfo.getBeginMin());
        beginIntent.putExtra("id", silenceTimeInfo.getBeginHour() * 100 + silenceTimeInfo.getBeginMin());
        AlarmManagerUtil.setAlarm(silenceTimeInfo.getBeginHour(), silenceTimeInfo.getBeginMin(), beginIntent);
        Log.d("chenjp", "设置开始勿扰: " + silenceTimeInfo.getBeginHour() + ":" + silenceTimeInfo.getBeginMin());
        Intent endIntent = new Intent(ACTION_SILENCE_TIME);
        endIntent.putExtra(AlarmManagerUtil.ALARM_TIME_MODE, SILENCE_TIME_MODE_OFF);
        endIntent.putExtra(AlarmManagerUtil.ALARM_TIME_HOUR, silenceTimeInfo.getEndHour());
        endIntent.putExtra(AlarmManagerUtil.ALARM_TIME_MIN, silenceTimeInfo.getEndMin());
        endIntent.putExtra("id", silenceTimeInfo.getEndHour() * 100 + silenceTimeInfo.getEndMin());
        AlarmManagerUtil.setAlarm(silenceTimeInfo.getEndHour(), silenceTimeInfo.getEndMin(), endIntent);
        Log.d("chenjp", "设置结束勿扰: " + silenceTimeInfo.getEndHour() + ":" + silenceTimeInfo.getEndMin());
    }

    public static List<SilenceTimeInfo> parseData(String silenceTimeData) {
        List<SilenceTimeInfo> silenceTimeList = new ArrayList<>();
        if (TextUtils.isEmpty(silenceTimeData)) {
            return silenceTimeList;
        }
        try {
            String[] timeScopes = silenceTimeData.split(",");
            for (String timeScope : timeScopes) {
                String[] times = timeScope.split("-");
                String[] startTime = times[0].split(":");
                String[] endTime = times[1].split(":");
                SilenceTimeInfo silenceTimeInfo = new SilenceTimeInfo();
                silenceTimeInfo.setBeginHour(Integer.parseInt(startTime[0]));
                silenceTimeInfo.setBeginMin(Integer.parseInt(startTime[1]));
                silenceTimeInfo.setEndHour(Integer.parseInt(endTime[0]));
                silenceTimeInfo.setEndMin(Integer.parseInt(endTime[1]));
                silenceTimeList.add(silenceTimeInfo);
            }
        } catch (Exception e) {
            Log.d("chenjp", "解析勿扰数据出错:" + e);
        }
        return silenceTimeList;
    }

    public static boolean isCurrentInTimeScope(SilenceTimeInfo info) {
        return isCurrentInTimeScope(info.getBeginHour(), info.getBeginMin(), info.getEndHour(), info.getEndMin());
    }

    /**
     * 判断当前系统时间是否在指定时间的范围内
     *
     * @param beginHour 开始小时，例如22
     * @param beginMin  开始小时的分钟数，例如30
     * @param endHour   结束小时，例如 8
     * @param endMin    结束小时的分钟数，例如0
     * @return true表示在范围内，否则false
     */
    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        if (beginHour == endHour && beginMin == endMin) {
            return false;
        }
        boolean result = false;
        final long aDayInMillis = 1000 * 60 * 60 * 24;
        final long currentTimeMillis = System.currentTimeMillis();

        Time now = new Time();
        now.set(currentTimeMillis);

        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;

        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin - 1;

        if (!startTime.before(endTime)) {
            // 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            // 普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }

    public static void handleSilenceTimeMode(boolean inTimeScope) {
        if (inTimeScope) {
            Log.d("chenjp", "handleSilenceTimeMode: 启动勿扰");
            FileIOUtils.writeFileFromString(FileConstant.FUNCTION_FORBIDDEN_STATUS_PATH_FILE, "1");
        } else {
            Log.d("chenjp", "handleSilenceTimeMode: 非勿扰时段");
            FileIOUtils.writeFileFromString(FileConstant.FUNCTION_FORBIDDEN_STATUS_PATH_FILE, "0");
        }
        EventBus.getDefault().postSticky(new DisturbSwitchEvent(inTimeScope));
    }

}
