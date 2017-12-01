package com.android.fisewatchlauncher.event;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/12
 * @time 17:32
 */
public class SwitchLowBatEvent {
    public boolean isSwitchOn;

    public SwitchLowBatEvent(boolean isSwitchOn) {
        this.isSwitchOn = isSwitchOn;
    }

    @Override
    public String toString() {
        return "SwitchLowBatEvent{" +
                "isSwitchOn=" + isSwitchOn +
                '}';
    }
}
