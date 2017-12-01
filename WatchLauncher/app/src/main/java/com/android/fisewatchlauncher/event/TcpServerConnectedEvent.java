package com.android.fisewatchlauncher.event;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/24
 * @time 21:33
 */
public class TcpServerConnectedEvent {
    public  boolean isConnected2Server;

    public TcpServerConnectedEvent(boolean isConnected2Server) {
        this.isConnected2Server = isConnected2Server;
    }

    @Override
    public String toString() {
        return "TcpServerConnectedEvent{" +
                "isConnected2Server=" + isConnected2Server +
                '}';
    }
}
