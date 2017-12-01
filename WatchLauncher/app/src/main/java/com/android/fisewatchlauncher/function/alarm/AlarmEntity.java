package com.android.fisewatchlauncher.function.alarm;

import android.app.AlarmManager;

import com.android.fisewatchlauncher.constant.ReceiverConstant;

import static com.android.fisewatchlauncher.constant.ReceiverConstant.CONFIRMED_FREQUENCY_UPLOAD;

/**
 * @author mare
 * @Description:TODO Alarm实体类
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/13
 * @time 20:25
 */
public class AlarmEntity {

    /**
     * 闹钟 定位指令(开始定位 停止定位) 上课禁用 固定频率上传
     */
    public enum Type {
        Clock, LocateStart, LocateStop, ClassForbiden,CONFIRMED_FREQUENCY_UPLOAD
    }

    public Type type;

    public String action;

    public long firstStartTime = 0;

    public long interval;

    public boolean isRepeate;

    public int alarmManagerType = AlarmManager.RTC_WAKEUP;//休眠时会运行

    public AlarmEntity(Type type, boolean isRepeate, long interval) {
        this.type = type;
        this.isRepeate = isRepeate;
        this.interval = interval;
    }

    public AlarmEntity(Type type) {
        this.type = type;
        String action;
        switch (type) {
            case Clock:
                action = ReceiverConstant.COMMON_CLOCK;
                break;
            case LocateStart:
                action = ReceiverConstant.LOCATION_START;
                break;
            case LocateStop:
                action = ReceiverConstant.LOCATION_STOP;
                break;
            case ClassForbiden:
                action = ReceiverConstant.CLASS_FORBIDEN;
                break;
            case CONFIRMED_FREQUENCY_UPLOAD:
                action = CONFIRMED_FREQUENCY_UPLOAD;
                break;
            default:
                action = null;
                break;
        }
        this.action = action;
    }

    public Type getType() {
        return type;
    }

    public boolean isRepeate() {
        return isRepeate;
    }

    public void setRepeate(boolean repeate) {
        isRepeate = repeate;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public String getAction() {
        return action;
    }

    public long getFirstStartTime() {
        return firstStartTime;
    }

    public void setFirstStartTime(long firstStartTime) {
        this.firstStartTime = firstStartTime;
    }

    public int getAlarmManagerType() {
        return alarmManagerType;
    }

    public void setAlarmManagerType(int alarmManagerType) {
        this.alarmManagerType = alarmManagerType;
    }

    @Override
    public String toString() {
        return "AlarmEntity{" +
                "type=" + type +
                ", action='" + action + '\'' +
                ", firstStartTime=" + firstStartTime +
                ", interval=" + interval +
                ", isRepeate=" + isRepeate +
                ", alarmManagerType=" + alarmManagerType +
                '}';
    }
}
