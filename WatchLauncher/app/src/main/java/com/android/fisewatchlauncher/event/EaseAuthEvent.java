package com.android.fisewatchlauncher.event;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/17
 * @time 14:53
 */
public class EaseAuthEvent {
    public boolean isGranted;//0 表示可正常通话， -1 表示余额不足， 不能通话

    public EaseAuthEvent(boolean isGranted) {
        this.isGranted = isGranted;
    }

    @Override
    public String toString() {
        return "EaseAuthEvent{" +
                "isGranted=" + isGranted +
                '}';
    }
}
