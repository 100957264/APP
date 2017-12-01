package com.android.fisewatchlauncher.event;

/**
 * @author mare
 * @Description:TODO 手机翻转次数事件
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4
 * @time 11:22
 */
public class TurnOverEvent {
    public long overCount;

    public TurnOverEvent(long overCount) {
        this.overCount = overCount;
    }
}
