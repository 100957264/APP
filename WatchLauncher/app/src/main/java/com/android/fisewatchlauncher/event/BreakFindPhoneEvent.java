package com.android.fisewatchlauncher.event;

/**
 * @author mare
 * @Description:TODO 打断找设备事件
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/15
 * @time 21:14
 */
public class BreakFindPhoneEvent {
    public boolean isPause;//是否打断找设备音乐(true===打断,false===恢复)

    public BreakFindPhoneEvent(boolean pause) {
        this.isPause = pause;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BreakFindPhoneEvent{");
        sb.append("isPause=").append(isPause);
        sb.append('}');
        return sb.toString();
    }
}
