package com.android.fisewatchlauncher.entity.dao;


import com.android.fisewatchlauncher.utils.DaoStringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

/**
 * @author mare
 * @Description:计时 计步 功能相关设置
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/10
 * @time 17:20
 */
@Entity
public class CenterCounter extends CenterSettingBase {
    @Id(autoincrement = true)
    private long id;

    public String imei;//主键

    private boolean stepSwitch;//计步功能开关 (1打开,0关闭)

    @Convert(columnType = String.class,converter = DaoStringConverter.class)
    private List<String> step_time;//计步时间段设置
    
    @Convert(columnType = String.class,converter = DaoStringConverter.class)
    private List<String> flip_check_time;//翻转检测时间段设置

    @Convert(columnType = String.class,converter = DaoStringConverter.class)
    private List<String> dnd_times;//免打扰时间段设置

    @Generated(hash = 80937100)
    public CenterCounter(long id, String imei, boolean stepSwitch,
            List<String> step_time, List<String> flip_check_time,
            List<String> dnd_times) {
        this.id = id;
        this.imei = imei;
        this.stepSwitch = stepSwitch;
        this.step_time = step_time;
        this.flip_check_time = flip_check_time;
        this.dnd_times = dnd_times;
    }

    @Generated(hash = 800542722)
    public CenterCounter() {
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

    public boolean getStepSwitch() {
        return this.stepSwitch;
    }

    public void setStepSwitch(boolean stepSwitch) {
        this.stepSwitch = stepSwitch;
    }

    public List<String> getStep_time() {
        return this.step_time;
    }

    public void setStep_time(List<String> step_time) {
        this.step_time = step_time;
    }

    public List<String> getFlip_check_time() {
        return this.flip_check_time;
    }

    public void setFlip_check_time(List<String> flip_check_time) {
        this.flip_check_time = flip_check_time;
    }

    public List<String> getDnd_times() {
        return this.dnd_times;
    }

    public void setDnd_times(List<String> dnd_times) {
        this.dnd_times = dnd_times;
    }

}
