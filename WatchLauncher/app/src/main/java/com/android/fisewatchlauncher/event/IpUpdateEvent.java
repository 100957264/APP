package com.android.fisewatchlauncher.event;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/31
 * @time 17:09
 */
public class IpUpdateEvent {
    public String ip;//new ip

    public IpUpdateEvent(String ip) {
        this.ip = ip;
    }
}
