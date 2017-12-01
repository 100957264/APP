package com.android.fisewatchlauncher.listener;


import com.android.fisewatchlauncher.client.TcpClient;
import com.android.fisewatchlauncher.entity.msg.TcpMsg;

/**
 */
public interface TcpClientListener {
    void onConnected(TcpClient client);

    void onSended(TcpClient client, TcpMsg tcpMsg);

    void onDisconnected(TcpClient client, String msg, Exception e);

    void onReceive(TcpClient client, TcpMsg tcpMsg);

    void onValidationFail(TcpClient client, TcpMsg tcpMsg);

    class SimpleTcpClientListener implements TcpClientListener {

        @Override
        public void onConnected(TcpClient client) {

        }

        @Override
        public void onSended(TcpClient client, TcpMsg tcpMsg) {

        }

        @Override
        public void onDisconnected(TcpClient client, String msg, Exception e) {

        }

        @Override
        public void onReceive(TcpClient client, TcpMsg tcpMsg) {

        }

        @Override
        public void onValidationFail(TcpClient client, TcpMsg tcpMsg) {

        }

    }
}
