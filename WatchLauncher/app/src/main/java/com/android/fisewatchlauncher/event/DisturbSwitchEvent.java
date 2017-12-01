package com.android.fisewatchlauncher.event;

/**
 * @author mare
 * @Description:TODO 勿扰状态改变更新UI事件
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/9
 * @time 16:38
 */
public class DisturbSwitchEvent {
    public boolean inTimeScope;

    public DisturbSwitchEvent(boolean inTimeScope) {
        this.inTimeScope = inTimeScope;
    }

    @Override
    public String toString() {
        return "DisturbSwitchEvent{" +
                "inTimeScope=" + inTimeScope +
                '}';
    }
}
