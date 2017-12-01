package com.android.fisewatchlauncher.event;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/3 0003
 * @time 22:31
 */
public class ClientConnectStateEvent {

    public enum ClientConnectState{
        CONNECT_STATE_ING,CONNECT_STATE_DISCONNECTED,CONNECT_STATE_SUCCESS
    }

    public ClientConnectState state;

    public ClientConnectStateEvent(ClientConnectState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ClientConnectStateEvent{" +
                "state=" + state.toString() +
                '}';
    }
}
