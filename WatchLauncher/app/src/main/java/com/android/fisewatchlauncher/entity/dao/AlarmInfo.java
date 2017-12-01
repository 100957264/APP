package com.android.fisewatchlauncher.entity.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by ChenJP on 2017/9/16.
 */
@Entity
public class AlarmInfo {
    @Id(autoincrement = true)
    public long id;
    private String time;
    private boolean isOpen;
    private int mode;
    public String repeatDayByWeek;
    private long triggerAtMillis;

    @Generated(hash = 1393103175)
    public AlarmInfo(long id, String time, boolean isOpen, int mode,
            String repeatDayByWeek, long triggerAtMillis) {
        this.id = id;
        this.time = time;
        this.isOpen = isOpen;
        this.mode = mode;
        this.repeatDayByWeek = repeatDayByWeek;
        this.triggerAtMillis = triggerAtMillis;
    }

    @Generated(hash = 212221696)
    public AlarmInfo() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getRepeatDayByWeek() {
        return repeatDayByWeek;
    }

    public void setRepeatDayByWeek(String repeatDayByWeek) {
        this.repeatDayByWeek = repeatDayByWeek;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsOpen() {
        return this.isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    @Override
    public String toString() {
        return "AlarmInfo{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", isOpen=" + isOpen +
                ", mode=" + mode +
                ", repeatDayByWeek='" + repeatDayByWeek + '\'' +
                ", triggerAtMillis='" + triggerAtMillis + '\'' +
                '}';
    }

    public long getTriggerAtMillis() {
        return this.triggerAtMillis;
    }

    public void setTriggerAtMillis(long triggerAtMillis) {
        this.triggerAtMillis = triggerAtMillis;
    }
}
