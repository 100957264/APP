package com.android.fisewatchlauncher.event;

/**
 * project : WatchLauncher
 * author : ChenJP
 * date : 2017/10/25  10:44
 * describe : 闹钟状态
 */

public class AlarmStateEvent {
    private boolean hasAlarm ;

    public AlarmStateEvent(boolean hasAlarm){
        this.hasAlarm = hasAlarm;
    }

    public boolean isHasAlarm() {
        return hasAlarm;
    }

    public void hasAlarm(boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }
}
