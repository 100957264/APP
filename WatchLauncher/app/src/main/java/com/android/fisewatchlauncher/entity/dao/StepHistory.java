package com.android.fisewatchlauncher.entity.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/16
 * @time 17:34
 */
@Entity
public class StepHistory extends CenterSettingBase {
    @Id(autoincrement = true)
    private long id;

    public String imei;//主键

    private long time;

    private long step_count;

    private long limit;

    private long step_energy;//消耗的能量

    private String completeDate;

    public StepHistory(long step_count,long time) {
        this.step_count = step_count;
        this.time = time;
    }

    @Generated(hash = 1435858099)
    public StepHistory() {
    }

    @Generated(hash = 924415319)
    public StepHistory(long id, String imei, long time, long step_count, long limit,
            long step_energy, String completeDate) {
        this.id = id;
        this.imei = imei;
        this.time = time;
        this.step_count = step_count;
        this.limit = limit;
        this.step_energy = step_energy;
        this.completeDate = completeDate;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public long getStep_count() {
        return this.step_count;
    }

    public void setStep_count(long step_count) {
        this.step_count = step_count;
    }

    public long getStep_energy() {
        return this.step_energy;
    }

    public void setStep_energy(long step_energy) {
        this.step_energy = step_energy;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCompleteDate() {
        return this.completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public long getLimit() {
        return this.limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "StepHistory{" +
                "id=" + id +
                ", imei='" + imei + '\'' +
                ", time=" + time +
                ", step_count=" + step_count +
                ", limit=" + limit +
                ", step_energy=" + step_energy +
                ", completeDate='" + completeDate + '\'' +
                '}';
    }
}
