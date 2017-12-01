package com.android.fisewatchlauncher.entity.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * project : WatchLauncher
 * author : ChenJP
 * date : 2017/11/2  17:30
 * describe : 健康数据
 */
@Entity
public class HealthInfo {
    @Transient
    public static final int TYPE_HEARTRATE = 0;
    @Transient
    public static final int TYPE_SBP       = 1;
    @Transient
    public static final int TYPE_DBP       = 2;
    @Transient
    public static final int TYPE_LTH       = 3;//Leave the hand

    @Id(autoincrement = true)
    public long id;
    public int type;
    public int value;
    public long date;
    @Generated(hash = 1599099639)
    public HealthInfo(long id, int type, int value, long date) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.date = date;
    }
    @Generated(hash = 203853863)
    public HealthInfo() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getValue() {
        return this.value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public long getDate() {
        return this.date;
    }
    public void setDate(long date) {
        this.date = date;
    }
}
