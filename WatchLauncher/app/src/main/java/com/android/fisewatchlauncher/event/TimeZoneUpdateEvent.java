package com.android.fisewatchlauncher.event;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/2 0002
 * @time 03:56
 */

public class TimeZoneUpdateEvent {
   public String timeZone;

    public TimeZoneUpdateEvent(String timeZone) {
        this.timeZone = timeZone;
    }
}
