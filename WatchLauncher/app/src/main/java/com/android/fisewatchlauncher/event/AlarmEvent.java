package com.android.fisewatchlauncher.event;

import com.android.fisewatchlauncher.function.alarm.AlarmEntity;

/**
 * @author mare
 * @Description:TODO 闹钟统一事件
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/13
 * @time 20:33
 */
public class AlarmEvent {

    public AlarmEntity alarmEntity;

    public AlarmEvent(AlarmEntity alarmEntity) {
        this.alarmEntity = alarmEntity;
    }

    @Override
    public String toString() {
        return "AlarmEvent{" +
                "alarmEntity=" + alarmEntity +
                '}';
    }
}
