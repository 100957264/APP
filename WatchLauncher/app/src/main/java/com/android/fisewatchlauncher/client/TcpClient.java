package com.android.fisewatchlauncher.client;

import android.text.TextUtils;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.client.helper.stickpackage.AbsStickPackageHelper;
import com.android.fisewatchlauncher.client.helper.stickpackage.BaseStickPackageHelper;
import com.android.fisewatchlauncher.client.helper.stickpackage.SpecifiedStickPackageHelper;
import com.android.fisewatchlauncher.client.helper.stickpackage.StaticLenStickPackageHelper;
import com.android.fisewatchlauncher.client.helper.stickpackage.VariableLenStickPackageHelper;
import com.android.fisewatchlauncher.client.msg.MsgInOut;
import com.android.fisewatchlauncher.client.state.ClientState;
import com.android.fisewatchlauncher.entity.TargetInfo;
import com.android.fisewatchlauncher.entity.msg.TcpMsg;
import com.android.fisewatchlauncher.listener.TcpClientListener;
import com.android.fisewatchlauncher.parser.MsgRecService;
import com.android.fisewatchlauncher.thread.HeartBeatThread;
import com.android.fisewatchlauncher.utils.CharsetUtil;
import com.android.fisewatchlauncher.utils.CrashHandler;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * tcp客户端
 */
public class TcpClient extends BaseSocket {
    public static final String TAG = "TcpClient";
    protected TargetInfo mTargetInfo;//目标ip和端口号
    protected Socket mSocket;
    protected ClientState mClientState;
    protected TcpConnConfig mTcpConnConfig;
    protected ConnectionThread mConnectionThread;
    protected SendThread mSendThread;
    protected ReceiveThread mReceiveThread;
    protected TcpClientListener mTcpClientListener;
    private LinkedBlockingQueue<TcpMsg> msgQueue;

    private TcpClient() {
        super();
        GlobalSettings.instance().setConfig(new TcpConnConfig.Builder()
                .setStickPackageHelper(StaticPackageUtils.instance().getSpecial())//特殊字符粘包
                .setIsReconnect(true)
                .create());//全局唯一避免多次创建
        mTargetInfo = new TargetInfo(GlobalSettings.instance().getIP(), GlobalSettings.instance().getPort());
        mTcpConnConfig = GlobalSettings.instance().getConfig();
        init(mTargetInfo, mTcpConnConfig);
        HeartBeatThread.instance();//主线程初始化
    }

    private static class SingletonHolder {
        private static final TcpClient INSTANCE = new TcpClient();
    }

    public static TcpClient instance() {
        return SingletonHolder.INSTANCE;
    }

    private void init(TargetInfo targetInfo, TcpConnConfig connConfig) {
        this.mTargetInfo = targetInfo;
        mClientState = ClientState.Disconnected;
        if (mTcpConnConfig == null && connConfig == null) {
            mTcpConnConfig = new TcpConnConfig.Builder().create();
        } else if (connConfig != null) {
            mTcpConnConfig = connConfig;
        }
    }

    public synchronized TcpMsg sendMsg(String message) {
        TcpMsg msg = new TcpMsg(message, MsgInOut.Send);
        msg.setTextMsg(true);
        msg.setSendCallBack(null);
        return sendMsg(msg);
    }

    public synchronized TcpMsg sendMsg(byte[] message, TcpMsg.SendCallBack callBack) {
        TcpMsg msg = new TcpMsg(message, MsgInOut.Send);
        msg.setTextMsg(false);
        msg.setSendCallBack(callBack);
        return sendMsg(msg);
    }

    public synchronized TcpMsg sendMsg(TcpMsg msg) {
        if (isDisconnected()) {
            LogUtils.d("发送消息 " + msg + "，当前没有tcp连接，先进行连接");
            connect();
        }
        boolean re = enqueueTcpMsg(msg);
        LogUtils.e("sendMsg ：" + getTcpMsgString(msg) + " IP-PORT " + GlobalSettings.instance().getIP() + " - " + GlobalSettings.instance().getPort());
        LogUtils.i("加入消息队列：" + msg.getSourceDataString() + " 成功 ? " + re);
        if (re) {
            return msg;
        }
        return null;
    }

    public static String getTcpMsgString(TcpMsg msg) {
        if (null == msg) return null;
        String sourceDataString = msg.getSourceDataString();
        boolean isDataStringNull = TextUtils.isEmpty(sourceDataString);
        if (!isDataStringNull) {
            return sourceDataString;
        } else {
            return Arrays.toString(msg.getSourceDataBytes());
        }
    }

    public synchronized boolean cancelMsg(TcpMsg msg) {
        return getSendThread().cancel(msg);
    }

    public synchronized boolean cancelMsg(int msgId) {
        return getSendThread().cancel(msgId);
    }

    public synchronized void connect() {
       String imei =  GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            LogUtils.d("还没写IMEI号 跳过连接...");
            return;
        }
        if (!isDisconnected()) {
            LogUtils.d("已经连接了或正在连接");
            return;
        }
        LogUtils.d("tcp connecting");
        setClientState(ClientState.Connecting);//正在连接
        getConnectionThread().start();
    }

    public synchronized Socket getSocket() {
        if (mSocket == null || isDisconnected() || !mSocket.isConnected()) {
            mSocket = new Socket();
            try {
                mSocket.setSoTimeout((int) mTcpConnConfig.getReceiveTimeout());
            } catch (SocketException e) {
                LogUtils.e(e);
            }
        }
        return mSocket;
    }

    public synchronized void disconnect() {
        disconnect("手动关闭tcpclient", null);
    }

    protected synchronized void onErrorDisConnect(String msg, Exception e) {
        if (isDisconnected()) {
            return;
        }
        disconnect(msg, e);
//        if (mTcpConnConfig.isReconnect()) {
//            connect();
//        }
        HeartBeatThread.instance().onErrorConnected();
    }

    protected synchronized void disconnect(String msg, Exception e) {
        if (isDisconnected()) {
            return;
        }
        closeSocket();
        getConnectionThread().interrupt();
        getSendThread().interrupt();
        getReceiveThread().interrupt();
        setClientState(ClientState.Disconnected);
        notifyDisconnected(msg, e);
        LogUtils.d("tcp closed");
    }

    private synchronized boolean closeSocket() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
        return true;
    }

    //连接已经连接，接下来的流程，创建发送和接受消息的线程
    private synchronized void onConnectSuccess() {
        LogUtils.d("tcp connect 建立成功");
        setClientState(ClientState.Connected);//标记为已连接
        HeartBeatThread.instance().onSuccessConnected();
        try {
            getSendThread().start();
            getReceiveThread().start();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("启动发送接收线程异常 " + e);
            collectErrorConnected(e);
        }
    }

    /**
     * tcp连接线程
     */
    private class ConnectionThread extends Thread {
        @Override
        public void run() {
            try {
                int localPort = mTcpConnConfig.getLocalPort();
                if (localPort > 0) {
                    if (!getSocket().isBound()) {
                        getSocket().bind(new InetSocketAddress(localPort));
                    }
                }
                getSocket().connect(new InetSocketAddress(mTargetInfo.getIp(), mTargetInfo.getPort()),
                        (int) mTcpConnConfig.getConnTimeout());
                LogUtils.d("创建连接成功,target=" + mTargetInfo + ",localport=" + localPort);
                LogUtils.e("登录成功");
            } catch (Exception e) {
                LogUtils.d("创建连接失败,target=" + mTargetInfo + "," + e);
                collectErrorConnected(e);
                onErrorDisConnect("创建连接失败", e);
                return;
            }
            notifyConnected();
            onConnectSuccess();
        }
    }

    public boolean enqueueTcpMsg(final TcpMsg tcpMsg) {
        if (tcpMsg == null || getMsgQueue().contains(tcpMsg)) {
            return false;
        }
        try {
            getMsgQueue().put(tcpMsg);
            return true;
        } catch (InterruptedException e) {
            LogUtils.e(e);
        }
        return false;
    }

    protected LinkedBlockingQueue<TcpMsg> getMsgQueue() {
        if (msgQueue == null) {
            msgQueue = new LinkedBlockingQueue<>();
        }
        return msgQueue;
    }

    private class SendThread extends Thread {
        private TcpMsg sendingTcpMsg;

        protected SendThread setSendingTcpMsg(TcpMsg sendingTcpMsg) {
            this.sendingTcpMsg = sendingTcpMsg;
            return this;
        }

        public TcpMsg getSendingTcpMsg() {
            return this.sendingTcpMsg;
        }

        public boolean cancel(TcpMsg packet) {
            return getMsgQueue().remove(packet);
        }

        public boolean cancel(int tcpMsgID) {
            return getMsgQueue().remove(new TcpMsg(tcpMsgID));
        }

        @Override
        public void run() {
            TcpMsg msg;
            try {
                while (isConnected() && !Thread.interrupted() && (msg = getMsgQueue().take()) != null) {
                    setSendingTcpMsg(msg);//设置正在发送的
                    TcpMsg.SendCallBack callBack = msg.getSendCallBack();
                    LogUtils.d("tcp sending msg=" + msg);
                    boolean isTextMsg = msg.isTextMsg();
                    LogUtils.e("isTextMsg " + isTextMsg);
                    byte[] data;
                    if (isTextMsg) {//根据编码转换字符串消息
                        data = CharsetUtil.stringToData(msg.getSourceDataString(), mTcpConnConfig.getCharsetName());
                    } else {
                        data = msg.getSourceDataBytes();
                    }

                    if (data != null && data.length > 0) {
                        try {
                            getSocket().getOutputStream().write(data);
                            getSocket().getOutputStream().flush();
                            msg.setTime();
                            notifySended(msg);
                            if (null != callBack) {
                                callBack.onSuccessSend(msg);
                                LogUtils.d("msg发送成功 " + getTcpMsgString(msg));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            LogUtils.d("发送消息失败 " + getTcpMsgString(msg));
                            if (null != callBack) {
                                callBack.onErrorSend(msg);
                            }
                            onErrorDisConnect("发送消息失败", e);
                            return;
                        }
                    }
                }
            } catch (InterruptedException e) {
                collectErrorConnected(e);
                LogUtils.d("tcp send  error  " + e);
                onErrorDisConnect("发送消息错误", e);
            }
        }
    }

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            try {
                InputStream is = getSocket().getInputStream();
                while (isConnected() && !Thread.interrupted()) {
                    AbsStickPackageHelper helper = mTcpConnConfig.getStickPackageHelper();
                    String helperTag = "";
                    if (helper instanceof SpecifiedStickPackageHelper) {
                        helperTag = "SpecifiedStickPackageHelper";
                    } else if (helper instanceof VariableLenStickPackageHelper) {
                        helperTag = "VariableLenStickPackageHelper";
                    } else if (helper instanceof StaticLenStickPackageHelper) {
                        helperTag = "StaticLenStickPackageHelper";
                    } else if (helper instanceof BaseStickPackageHelper) {// BaseStickPackageHelper
                        helperTag = "BaseStickPackageHelper";
                    } else {
                        helperTag = "NONE Helper";
                    }
                    LogUtils.d(helperTag);
                    byte[] result = mTcpConnConfig.getStickPackageHelper().execute(is);//粘包处理
                    if (result == null) {//报错
                        LogUtils.d("tcp Receive 粘包处理失败 " + Arrays.toString(result));
                        //onErrorDisConnect("粘包处理中发送错误", null);
                        break;
                    }
                    LogUtils.d("tcp Receive 解决粘包之后的数据 " + Arrays.toString(result));
                    String data = CharsetUtil.dataToString(result, mTcpConnConfig.getCharsetName());
                    LogUtils.e("tcp Receive data " + data);
                    TcpMsg tcpMsg = new TcpMsg(data, MsgInOut.Receive);
                    tcpMsg.setSourceDataBytes(result);//存储收到的字节数组数据
                    tcpMsg.setTime();
                    tcpMsg.setSourceDataString(data);
                    boolean va = mTcpConnConfig.getValidationHelper().execute(result);
                    if (!va) {
                        LogUtils.d("tcp Receive 数据验证失败 ");
                        notifyValidationFail(tcpMsg);//验证失败
                        continue;
                    }
                    byte[][] decodebytes = mTcpConnConfig.getDecodeHelper().execute(result, mTargetInfo, mTcpConnConfig);
                    tcpMsg.setEndDecodeData(decodebytes);
                    //LogUtils.d("tcp Receive  succ msg= " + tcpMsg);
                    MsgRecService.instance().handleRecvMsg(KApplication.sContext, tcpMsg);
                    notifyReceive(tcpMsg);//notify listener
                }
            } catch (Exception e) {
                LogUtils.d("tcp Receive  error  " + e);
                onErrorDisConnect("接受消息错误", e);
                collectErrorConnected(e);
            }
        }
    }

    private void collectErrorConnected(Exception e) {
        Thread thread = Thread.currentThread();
        CrashHandler.instance().uncaughtException(thread, e);
    }

    protected ReceiveThread getReceiveThread() {
        if (mReceiveThread == null || !mReceiveThread.isAlive()) {
            mReceiveThread = new ReceiveThread();
        }
        return mReceiveThread;
    }

    protected SendThread getSendThread() {
        if (mSendThread == null || !mSendThread.isAlive()) {
            mSendThread = new SendThread();
        }
        return mSendThread;
    }

    protected ConnectionThread getConnectionThread() {
        if (mConnectionThread == null || !mConnectionThread.isAlive() || mConnectionThread.isInterrupted()) {
            mConnectionThread = new ConnectionThread();
        }
        return mConnectionThread;
    }

    public ClientState getClientState() {
        return mClientState;
    }

    protected void setClientState(ClientState state) {
        if (mClientState != state) {
            mClientState = state;
        }
    }

    public boolean isDisconnected() {
        return getClientState() == ClientState.Disconnected;
    }

    public boolean isConnected() {
        return getClientState() == ClientState.Connected;
    }

    private void notifyConnected() {
        if (null != mTcpClientListener) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mTcpClientListener.onConnected(TcpClient.this);
//                }
//            });
            mTcpClientListener.onConnected(TcpClient.this);
        }
    }

    private void notifyDisconnected(final String msg, final Exception e) {
        if (null != mTcpClientListener) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mTcpClientListener.onDisconnected(TcpClient.this, msg, e);
//                }
//            });
            mTcpClientListener.onDisconnected(TcpClient.this, msg, e);
        }
    }


    private synchronized void notifyReceive(final TcpMsg tcpMsg) {
        if (null != mTcpClientListener) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTcpClientListener.onReceive(TcpClient.this, tcpMsg);
                }
            });
        }
    }

    private synchronized void notifySended(final TcpMsg tcpMsg) {
        if (null != mTcpClientListener) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTcpClientListener.onSended(TcpClient.this, tcpMsg);
                }
            });
        }
    }

    private synchronized void notifyValidationFail(final TcpMsg tcpMsg) {
        if (null != mTcpClientListener) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTcpClientListener.onValidationFail(TcpClient.this, tcpMsg);
                }
            });
        }
    }

    public TargetInfo getTargetInfo() {
        return mTargetInfo;
    }

    public synchronized void addTcpClientListener(TcpClientListener listener) {
        if (null != mTcpClientListener && mTcpClientListener == listener) {
            return;
        }
        mTcpClientListener = listener;
    }

    public synchronized void removeTcpClientListener(TcpClientListener listener) {
        mTcpClientListener = null;
    }

    @Override
    public String toString() {
        return "TcpClient{" +
                "mTargetInfo=" + mTargetInfo + ",state=" + mClientState + ",isconnect=" + isConnected() +
                '}';
    }

    public void stopClient() {
        HeartBeatThread.instance().onStop();
        disconnect();
    }

    public void startClient() {
        if (isDisconnected()) {
            connect();
            HeartBeatThread.instance().onResume();
        }
    }
}
