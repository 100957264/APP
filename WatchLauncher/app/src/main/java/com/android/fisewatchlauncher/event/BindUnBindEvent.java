package com.android.fisewatchlauncher.event;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/3 0003
 * @time 22:31
 */
public class BindUnBindEvent {

    public boolean deviceBond;

    public BindUnBindEvent(boolean state) {
        this.deviceBond = state;
    }

    @Override
    public String toString() {
        return "ClientBindState{" +
                "deviceBond=" + deviceBond +
                '}';
    }
}
