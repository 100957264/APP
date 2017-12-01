package com.android.fisewatchlauncher.event;

import com.android.fisewatchlauncher.entity.location.CdmaID;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4
 * @time 13:59
 */
public class CdmaBaseStationEvent {
    public CdmaID cdmaID;

    public CdmaBaseStationEvent(CdmaID cdmaID) {
        this.cdmaID = cdmaID;
    }

    @Override
    public String toString() {
        return  cdmaID.toString() ;
    }
}
