package com.android.fisewatchlauncher.entity.schedule;

/**
 * Created by ChenJP on 2017/9/20.
 */

public class SilenceTimeInfo {
    private int beginHour;
    private int beginMin;
    private int endHour;
    private int endMin;

    public int getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    public int getBeginMin() {
        return beginMin;
    }

    public void setBeginMin(int beginMin) {
        this.beginMin = beginMin;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMin() {
        return endMin;
    }

    public void setEndMin(int endMin) {
        this.endMin = endMin;
    }
}
