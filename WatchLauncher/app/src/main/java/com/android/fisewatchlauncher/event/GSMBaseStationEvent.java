package com.android.fisewatchlauncher.event;

import com.android.fisewatchlauncher.entity.location.SCell;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4
 * @time 13:57
 */
public class GSMBaseStationEvent {
   public SCell sCell;

    public GSMBaseStationEvent(SCell sCell) {
        this.sCell = sCell;
    }

    @Override
    public String toString() {
        return "GSMBaseStationEvent{" +
                "sCell=" + sCell +
                '}';
    }

}
