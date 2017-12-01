package com.android.fisewatchlauncher.thread;

import android.os.CountDownTimer;

import com.android.fisewatchlauncher.client.TcpClient;
import com.android.fisewatchlauncher.client.msg.MsgType;
import com.android.fisewatchlauncher.entity.msg.TcpMsg;
import com.android.fisewatchlauncher.parser.MsgParser;
import com.android.fisewatchlauncher.utils.LogUtils;


/**
 * @author mare
 * @Description:心跳处理方式一
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/9
 * @time 17:28
 */
public class HeartBeatThread implements TcpMsg.SendCallBack {
    static final String TAG = HeartBeatThread.class.getSimpleName();

    private static final long HEART_BEAT_LINK_INTERVAL = 5 * 60 * 1000;
    private static final long HEART_BEAT_RECONNECT_INTERVAL = 60 * 1000;

    private static final int MAX_HEART_BEAT_COUNT = 5;
    private int lastReconnectCount = 0;
    private boolean isReConnectRunning = false;
    private boolean isRunning = false;
    private boolean isConnected = false;

    private HeartBeatThread() {
    }

    @Override
    public void onSuccessSend(TcpMsg msg) {

    }

    @Override
    public void onErrorSend(TcpMsg msg) {

    }

    private static class SingletonHolder {
        private static final HeartBeatThread INSTANCE = new HeartBeatThread();
    }

    public static HeartBeatThread instance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 第一次连接时候
     */
    public void onResume() {
        if (!isRunning) {
            startThreadKeepLink();
            isRunning = true;
        }
    }

    public void onStop() {
        if (isRunning) {
            stopThreadKeepLink();
            isRunning = false;
        }
    }

    public void onErrorConnected() {
        LogUtils.e("onErrorConnected isReConnectRunning = " + isReConnectRunning + " ,isConnected " + isConnected);
        if (!isReConnectRunning && !isConnected) {
            LogUtils.e("tryConnect");
            tryConnect();
            startThreadReconnect();
            isReConnectRunning = true;
        }
        isConnected = false;
    }

    private void tryConnect() {
        LogUtils.e("心跳 重新连接: 已连接次数 " + lastReconnectCount);
        if (lastReconnectCount < 5) {//之前失败< 5
            lastReconnectCount++;
            reConnectClient();
        } else {
            lastReconnectCount = 0;
            reStartApp();
        }
    }

    public void onSuccessConnected() {
        lastReconnectCount = 0;
        isConnected = true;
        MsgParser.instance().sendMsgByType(MsgType.LK);//连接成功发一次LK
        queryBondState();//检测连接时候的绑定状态
        stopThreadReconnect();//关掉一分钟一次的心跳
        LogUtils.e("onSuccessConnected");
    }

    private void reConnectClient() {
        LogUtils.e("reConnectClient");
        TcpClient.instance().connect();
    }

    public void queryBondState(){
        MsgParser.instance().sendMsgByType(MsgType.PING);//心跳查询绑定
    }

    private void sendLink() {
        LogUtils.e("sendLink");
        MsgParser.instance().sendMsgByType(MsgType.LK, null, this);
        queryBondState();//没连接时候查询下绑定状态
    }

    private void reStartApp() {
        LogUtils.e("reStartClient");
        //重启client
        TcpClient.instance().stopClient();
        TcpClient.instance().startClient();
    }

    private void startThreadKeepLink() {
        link.start();
    }

    private void stopThreadKeepLink() {
        LogUtils.e("stopThreadKeepLink");
        link.cancel();
        if (isReConnectRunning) {
            stopThreadReconnect();
        }
    }

    private void startThreadReconnect() {
        reConnect.start();
    }

    private void stopThreadReconnect() {
        LogUtils.e("stopThreadReconnect");
        reConnect.cancel();
        isReConnectRunning = false;
    }

    CountDownTimer link = new CountDownTimer(HEART_BEAT_LINK_INTERVAL * MAX_HEART_BEAT_COUNT,
            HEART_BEAT_LINK_INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {
            sendLink();
        }

        @Override
        public void onFinish() {
            sendLink();
            start();
        }
    };

    CountDownTimer reConnect = new CountDownTimer(HEART_BEAT_RECONNECT_INTERVAL * MAX_HEART_BEAT_COUNT,
            HEART_BEAT_RECONNECT_INTERVAL) {

        @Override
        public void onTick(long millisUntilFinished) {
            tryConnect();
        }

        @Override
        public void onFinish() {
            reStartApp();
        }
    };

}
