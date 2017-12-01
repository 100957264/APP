package com.android.fisewatchlauncher.event;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/26
 * @time 15:20
 */
public class TimeUpdateEvent {
    public long curSystemTime;

    public TimeUpdateEvent(long curSystemTime) {
        this.curSystemTime = curSystemTime;
    }

    private String[] formatTime(long cur) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm E");
        sdf.setTimeZone(TimeZone.getTimeZone("gmt+8"));
        String d = sdf.format(cur);
        return d.split(" ");
    }

    public String getTime() {
        return formatTime(curSystemTime)[1];
    }

    public String getDate() {
        return formatTime(curSystemTime)[0];
    }

    public String getWeekDay() {
        return formatTime(curSystemTime)[2];
    }
}
