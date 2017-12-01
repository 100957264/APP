package com.android.fisewatchlauncher.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.android.fisewatchlauncher.BuildConfig;
import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.client.TcpClient;
import com.android.fisewatchlauncher.client.msg.MsgType;
import com.android.fisewatchlauncher.entity.msg.TcpMsg;
import com.android.fisewatchlauncher.event.TcpServerConnectedEvent;
import com.android.fisewatchlauncher.function.alarm.AlarmEntity;
import com.android.fisewatchlauncher.function.alarm.AlarmTimer;
import com.android.fisewatchlauncher.listener.TcpClientListener;
import com.android.fisewatchlauncher.manager.LocationManager;
import com.android.fisewatchlauncher.manager.StaticManager;
import com.android.fisewatchlauncher.parser.MsgParser;
import com.android.fisewatchlauncher.receiver.BatteryStatusReceiver;
import com.android.fisewatchlauncher.receiver.ScreenStatusReceiver;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

public class FiseService extends Service implements TcpClientListener {
    public static final int CURRENT_STEP = 0;
    public static boolean NEED_SHOW_ANALOG_CLOCK = false;

    private BatteryStatusReceiver batteryStatusReceiver;
    private ScreenStatusReceiver screenStatusReceiver;

    public static void pull(Context context) {
        LogUtils.i("isInited " + StaticManager.instance().isInited);
        if (!StaticManager.instance().isInited) {//还没初始化完全
            return;
        }
        context.startService(new Intent(context, FiseService.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        screenStatusReceiver = new ScreenStatusReceiver();
        IntentFilter screenFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenStatusReceiver, screenFilter);

        batteryStatusReceiver = new BatteryStatusReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);
        registerReceiver(batteryStatusReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TcpClient client = TcpClient.instance();
        client.addTcpClientListener(this);
        client.startClient();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(batteryStatusReceiver);
        unregisterReceiver(screenStatusReceiver);
        TcpClient.instance().removeTcpClientListener(this);//解绑监听
        TcpClient.instance().stopClient();
        startService(new Intent(this, FiseService.class));
    }

    private void updateConnected2ServerIcon(boolean isConnected2Server) {
        TcpServerConnectedEvent event = new TcpServerConnectedEvent(isConnected2Server);
        StaticManager.instance().tcpServerConnectedEvent = event;
        EventBus.getDefault().post(event);
    }

    @Override
    public void onConnected(TcpClient client) {
        LogUtils.e("onConnected");
        if (BuildConfig.LOG_DEBUG) {
            ToastUtils.showShortSafe("已经连上服务器O(∩_∩)O~");
        }
        MsgParser.instance().sendMsgByType(MsgType.EASE);//请求环信账号和密码
        LogUtils.e("请求环信账号和密码.. ");
        updateConnected2ServerIcon(true);
        LocationManager.instance().startLocate();;//定位一下获取最新位置信息
        AlarmTimer.startConfirmedFrequencyUpload(KApplication.sContext);//开启固定频率上传
        if (StaticManager.instance().isBatteryLow()) {//连上服务器检测一次低电情况
            LocationManager.instance().sendAlert();//低电就上报信息
        }

    }

    @Override
    public void onSended(TcpClient client, TcpMsg tcpMsg) {
        LogUtils.e("onSended " + TcpClient.getTcpMsgString(tcpMsg));
    }

    @Override
    public void onDisconnected(TcpClient client, String msg, Exception e) {
        LogUtils.e("onDisconnected " + e);
        if (BuildConfig.LOG_DEBUG) {
            ToastUtils.showShortSafe("服务器断开连接┭┮﹏┭┮");
        }
        updateConnected2ServerIcon(false);
        //断开连接情况取消固定频率上传
        AlarmTimer.cancelAlarmTimer(KApplication.sContext, new AlarmEntity(AlarmEntity.Type.CONFIRMED_FREQUENCY_UPLOAD));
//        EaseMobManager.instance().loginOut();//注销环信
    }

    @Override
    public void onReceive(TcpClient client, TcpMsg tcpMsg) {
        LogUtils.e("onReceive TcpMsg " + TcpClient.getTcpMsgString(tcpMsg));
        // MsgRecService.instance().handleRecvMsg(this, tcpMsg);
    }

    @Override
    public void onValidationFail(TcpClient client, TcpMsg tcpMsg) {

    }
}
