package com.android.fisewatchlauncher.event;

import com.android.fisewatchlauncher.manager.LocationManager;

/**
 * @author mare
 * @Description:TODO CommonLocationEvent
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/13
 * @time 17:17
 */
public class LocationEvent {
    public LocationManager.LocationPickerType type;
    public String locationStr;

    public LocationEvent(LocationManager.LocationPickerType type, String locationStr) {
        this.type = type;
        this.locationStr = locationStr;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LocationEvent{");
        if (null != type) {
            sb.append("type=").append(type.toString());
        }
        sb.append(", locationStr=").append(locationStr);
        sb.append('}');
        return sb.toString();
    }
}
