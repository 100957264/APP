package com.android.fisewatchlauncher.event;

/**
 * @author mare
 * @Description:TODO 手表是单卡 所以不用分析双卡
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4
 * @time 11:27
 */
public class SignalStrengthChangedEvent {
    public  int mSignalStrength;

    public SignalStrengthChangedEvent(int mSignalStrength) {
        this.mSignalStrength = mSignalStrength;
    }

    @Override
    public String toString() {
        return "SignalStrengthChangedEvent{" +
                "mSignalStrength=" + mSignalStrength +
                '}';
    }
}
