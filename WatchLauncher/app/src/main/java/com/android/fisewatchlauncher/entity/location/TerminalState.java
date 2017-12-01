package com.android.fisewatchlauncher.entity.location;

import com.android.fisewatchlauncher.entity.FlagFormat;
import com.android.fisewatchlauncher.utils.LogUtils;

/**
 * @author mare
 * @Description:TODO 终端状态解析类
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4
 * @time 16:03
 */
public class TerminalState {

    public static final int BIT_COUNT = 8;//八位
    private int batteryLow;
    private int outFence;//出围栏状态
    private int inFence;
    private int takenOff;//手环戴上取下状态

    private int still;//手表运行静止状态

    private int switchSOS;//SOS 报警开关是否打开(PS 是短信报警开关)
    private int switchLow;//低电报警是否打开
    private int switchFenceOut;//出围栏报警是否打开
    private int switchFenceIn;//进围栏报警是否打开

    private int switchTakenOff;//手环拆除报警是否打开
    private int switchFall;//跌倒报警是否打开

    public void setBatteryLow(int batteryLow) {
        this.batteryLow = batteryLow << 0;
    }

    public void setBatteryLow(boolean flag) {
        int curFlag = FlagFormat.boolean2Int(flag);
        setBatteryLow(curFlag);
    }

    public void setOutFence(int outFence) {
        this.outFence = outFence << 1;
    }

    public void setOutFence(boolean flag) {
        int curFlag = FlagFormat.boolean2Int(flag);
        setOutFence(curFlag);
    }

    public void setInFence(int inFence) {
        this.inFence = inFence << 2;
    }

    public void setInFence(boolean flag) {
        int curFlag = FlagFormat.boolean2Int(flag);
        setInFence(curFlag);
    }

    public void setTakenOff(int takenOff) {
        this.takenOff = takenOff << 3;
    }

    public void setTakenOff(boolean flag) {
        int curFlag = FlagFormat.boolean2Int(flag);
        setTakenOff(curFlag);
    }

    //==============================
    public void setStill(int still) {
        this.still = still << 0;
    }

    public void setStill(boolean flag) {
        int curFlag = FlagFormat.boolean2Int(flag);
        setStill(curFlag);
    }

    //==============================
    public void setSwitchSOS(int switchSOS) {
        this.switchSOS = switchSOS << 0;
    }

    public void setSwitchSOS(boolean flag) {
        int curFlag = FlagFormat.boolean2Int(flag);
        setSwitchSOS(curFlag);
    }

    public void setSwitchLow(int switchLow) {
        this.switchLow = switchLow << 1;
    }

    public void setSwitchLow(boolean flag) {
        int curFlag = FlagFormat.boolean2Int(flag);
        setSwitchLow(curFlag);
    }

    public void setSwitchFenceOut(int switchFenceOut) {
        this.switchFenceOut = switchFenceOut << 2;
    }

    public void setSwitchFenceOut(boolean flag) {
        int curFlag = FlagFormat.boolean2Int(flag);
        setSwitchFenceOut(curFlag);
    }

    public void setSwitchFenceIn(int switchFenceIn) {
        this.switchFenceIn = switchFenceIn << 3;
    }

    public void setSwitchFenceIn(boolean flag) {
        int curFlag = FlagFormat.boolean2Int(flag);
        setSwitchFenceIn(curFlag);
    }

    //==============================
    public void setSwitchTakenOff(int switchTakenOff) {
        this.switchTakenOff = switchTakenOff << 0;
    }

    public void setSwitchTakenOff(boolean flag) {
        int curFlag = FlagFormat.boolean2Int(flag);
        setSwitchTakenOff(curFlag);
    }

    public void setSwitchFall(int switchFall) {
        this.switchFall = switchFall << 1;
    }

    public void setSwitchFall(boolean flag) {
        int curFlag = FlagFormat.boolean2Int(flag);
        setSwitchFall(curFlag);
    }

    public String format() {
        String[] bytes = new String[BIT_COUNT];
        int tmp = 0;
        for (int i = 0; i < BIT_COUNT; i++) {
            switch (i) {
                case 0:
                    tmp = batteryLow | outFence | inFence | takenOff;
                    break;
                case 1:
                    tmp = still;
                    break;
                case 4:
                    tmp = switchSOS | switchLow | switchFenceOut | switchFenceIn;
                    break;
                case 5:
                    tmp = switchTakenOff | switchFall;
                    break;
                default:
                    tmp = 0;
                    break;
            }
            bytes[i] = Integer.toHexString(tmp);
            LogUtils.e("bytes[i] " + bytes[i]);
        }
        String formatStr = contactArray(bytes);
        LogUtils.e("终端状态 formatStr " + formatStr);
        return formatStr;
    }

    private String contactArray(String[] segemnt) {
        if (null == segemnt) {
            return String.valueOf(0);
        }
        int len = segemnt.length;
        StringBuffer sb = new StringBuffer();
        for (int i = len - 1; i >= 0; i--) {
            sb.append(segemnt[i]);
        }
        return sb.toString();
    }

}
