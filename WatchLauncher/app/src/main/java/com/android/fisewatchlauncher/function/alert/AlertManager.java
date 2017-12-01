package com.android.fisewatchlauncher.function.alert;

import android.os.Handler;
import android.text.TextUtils;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.client.msg.MsgType;
import com.android.fisewatchlauncher.entity.location.TerminalState;
import com.android.fisewatchlauncher.entity.msg.TcpMsg;
import com.android.fisewatchlauncher.event.BatteryStatus;
import com.android.fisewatchlauncher.manager.LocationManager;
import com.android.fisewatchlauncher.manager.StaticManager;
import com.android.fisewatchlauncher.parser.MsgParser;
import com.android.fisewatchlauncher.prenster.dao.CenterSettingsUtils;
import com.android.fisewatchlauncher.utils.ListUtils;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.PhoneUtils;
import com.android.fisewatchlauncher.utils.UnicodeUtils;

import java.util.List;

/**
 * @author mare
 * @Description:TODO 预警上报(要监听上报失败情况)
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/13
 * @time 17:41
 */
public class AlertManager implements TcpMsg.SendCallBack {
    private AlertManager() {
    }

    private static class SingletonHolder {
        private static final AlertManager INSTANCE = new AlertManager();
    }

    public static AlertManager instance() {
        return AlertManager.SingletonHolder.INSTANCE;
    }

    private Handler alertHandler;

    public void init(Handler handler) {
        this.alertHandler = handler;
    }

    private static final long DEFAULT_SEND_INTERVAL = 60 * 1000;

    /**
     * 功能机预警
     */
    public void sendFunctionAlert() {
        MsgParser.instance().sendMsgByType(MsgType.AL, StaticManager.instance().getLocationBeanStr(), this);//SOS上传警情信息
    }

    /**
     * 智能机预警
     */
    public void sendSmartAlert() {
        String locationStr = StaticManager.instance().getLocationBeanStr();
        if (TextUtils.isEmpty(locationStr)) {
            LogUtils.e("没有地理警情预警信息...");
            return;
        }

        //TODO 解析终端状态
        TerminalState state = getTerminalState();
        String terminalState = state.format();
        StringBuffer sb = new StringBuffer();
        sb.append(locationStr)
                .append(GlobalSettings.MSG_CONTENT_SEPERATOR)
                .append(terminalState);
        String content = sb.toString();
        LogUtils.e("sendSmartAlarm " + content);
        content = UnicodeUtils.str2UnicodeNo0xu(content).toUpperCase();
        LogUtils.e("sendSmartAlarm after " + content);
        MsgParser.instance().sendMsgByType(MsgType.ALARM, content);//SOS上传警情信息
    }

    /**
     * 根据状态(电量 戴摘 运动静止 跌倒 围栏进出)获取终端状态
     *
     * @return
     */
    public static TerminalState getTerminalState() {
        TerminalState state = new TerminalState();
        state.setBatteryLow(StaticManager.instance().isBatteryLow());
        state.setOutFence(StaticManager.instance().isInFence());//进出围栏暂时用不上
        state.setTakenOff(StaticManager.instance().isTakenOff());//进出围栏暂时用不上
        state.setStill(StaticManager.instance().isStill());
        state.setSwitchSOS(CenterSettingsUtils.instance().querySMSSwitch());//短信报警开关
        state.setSwitchLow(CenterSettingsUtils.instance().queryLowBatterySwitch());
//        state.setSwitchFenceOut( CenterSettingsUtils.instance().）;
//        state.setSwitchFenceIn( CenterSettingsUtils.instance().);//进出围栏暂时用不上
        state.setSwitchTakenOff(CenterSettingsUtils.instance().queryTakenOffSwitch());//查询摘掉开关
        state.setSwitchFall(CenterSettingsUtils.instance().queryFallDownSwitch());//查询跌倒报警开关

        return state;
    }

    public void sendBatteryLowSMS() {
        boolean isSwitchOn = CenterSettingsUtils.instance().querySMSSwitch();
        if (!isSwitchOn) {
            LogUtils.e("sendBatteryLowSMS 短信报警开关没打开...");
            return;
        }
        BatteryStatus batteryStatus = StaticManager.instance().getBatteryStatus();
        float curLevel = 0;
        if (null != batteryStatus) {
            curLevel = batteryStatus.level;
        }
        String lowStr = KApplication.sContext.getResources().getString(R.string.alert_low_battery);
        String formatStr = String.format(lowStr, curLevel);
        String centerNum = CenterSettingsUtils.instance().queryCenterPhoneNum();
        if (TextUtils.isEmpty(centerNum)) {
            LogUtils.e("sendBatteryLowSMS 中心号码还没设置...");
            return;
        }
        PhoneUtils.sendSmsSilent(centerNum, formatStr);
    }

    /**
     * SOS呼叫时候发送紧急短信
     */
    public void sendSOSSMS() {
        boolean isSwitchOn = CenterSettingsUtils.instance().querySMSSwitch();
        if (!isSwitchOn) {
            LogUtils.e("sendSOSSMS 短信报警开关没打开...");
            return;
        }
        List<String> sosList = CenterSettingsUtils.instance().querySOSList();
        if (ListUtils.isEmpty(sosList)) {
            LogUtils.e("sendSOSSMS 紧急联系人为空...");
            return;
        }
        String sosStr = KApplication.sContext.getResources().getString(R.string.alert_soscall);
        for (String num : sosList) {
            if (TextUtils.isEmpty(num)) {
                LogUtils.e("sendSOSSMS 当前紧急联系人号码为空...");
                continue;
            }
            PhoneUtils.sendSmsSilent(num, sosStr);
        }
    }

    Runnable alertRunnable = new Runnable() {
        @Override
        public void run() {
            LocationManager.instance().sendAlert();
        }
    };

    @Override
    public void onSuccessSend(TcpMsg msg) {
        if (null != alertHandler) {
            alertHandler.removeCallbacksAndMessages(null);//防止内存泄漏
        }
    }

    @Override
    public void onErrorSend(TcpMsg msg) {
        if (null != alertHandler) {
            alertHandler.postDelayed(alertRunnable, DEFAULT_SEND_INTERVAL);
        }
    }

    //防止内存泄漏
    public void onDestroy(){
        onSuccessSend(null);
    }
}
