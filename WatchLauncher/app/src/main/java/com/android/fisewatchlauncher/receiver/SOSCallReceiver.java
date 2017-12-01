package com.android.fisewatchlauncher.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.function.alert.AlertManager;
import com.android.fisewatchlauncher.function.phone.PhoneCallUtils;
import com.android.fisewatchlauncher.manager.LocationManager;
import com.android.fisewatchlauncher.manager.StaticManager;
import com.android.fisewatchlauncher.prenster.dao.CenterSettingsUtils;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.PhoneUtils;
import com.android.fisewatchlauncher.utils.ToastUtils;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * @author mare
 * @Description:TODO 紧急呼叫
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/13
 * @time 14:29
 */
public class SOSCallReceiver extends BroadcastReceiver {
    private static final String TAG = "CommonAlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果是拨打电话
        String action = intent.getAction();
        LogUtils.e(action);
        LogUtils.e("SOSCallReceiver isSosStart " + StaticManager.instance().isSosStart);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (action.equals(PhoneCallUtils.ACTION_CALL_SOS)) {
            boolean isIdelState = tm.getCallState() == TelephonyManager.CALL_STATE_IDLE;
            if (!isIdelState) {
                ToastUtils.showShortSafe("请稍后，上次通话尚未结束...");
                return;
            }
            resetState();
            resetSoSState();
            StaticManager.instance().outgoingCalls = CenterSettingsUtils.instance().querySOSList();
            int size = StaticManager.instance().outgoingCalls.size();
            if (size <= 0) return;
            String num;
            Vibrator vibrator = (Vibrator) KApplication.sContext.getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{100, 50, 500, 100, 200}, -1);
            LocationManager.instance().sendAlert();
            AlertManager.instance().sendSOSSMS();//发送短信预警

            String firstNum = "";
            boolean isCallded = false;
            for (int i = 0; i < size; i++) {
                num = StaticManager.instance().outgoingCalls.get(i);
                if (TextUtils.isEmpty(num)) continue;//跳过
                if (!isCallded) {
                    firstNum = num;
                    isCallded = true;
                }
            }
            if (TextUtils.isEmpty(firstNum)) {
                LogUtils.e("SOS号码不能为空 SOS呼叫失败....");
                ToastUtils.showShortSafe("SOS号码不能为空 SOS呼叫失败");
                return;
            }
            StaticManager.instance().isSosReady = true;
            ToastUtils.showShortSafe("SOS开始~~~");
            PhoneUtils.call(firstNum);
        } else if (PhoneCallUtils.ACTION_CALL_CONNECTED.equals(action)) {
            StaticManager.instance().isOutgoingConnected = true;
            LogUtils.e("SOS接通了 ");
        } else if (PhoneCallUtils.ACTION_CALL_HANGUP_CAUSE.equals(action)) {
            int hangupCause = intent.getIntExtra(PhoneCallUtils.EXTRA_KEY_HANGUP_CAUSE, 0);
            LogUtils.e("挂断原因是 " + hangupCause);
            if (hangupCause == PhoneCallUtils.EXTRA_FLAG_CALLER_HANGUP) {//是主动挂掉的
                resetSoSState();
                resetState();
            }
        } else if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            LogUtils.e(TAG, "SOSCallReceiver call OUT:" + phoneNumber);
            StaticManager.instance().isSosStart = StaticManager.instance().isSosReady;//SOS开始
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    LogUtils.e(TAG + " 去电 " + "RINGING");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    LogUtils.e(TAG + " 去电 " + "OFFHOOK");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    LogUtils.e(TAG + " 去电 " + "IDLE");
                    break;
            }
//            LogUtils.e(TAG + " 去电 " + state);
        } else {// 如果是来电
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    LogUtils.e(TAG + " 来电 " + "RINGING");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    LogUtils.e(TAG + " 来电 " + "OFFHOOK");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    LogUtils.e(TAG + " 来电 " + "IDLE");
                    if (StaticManager.instance().isSosStart) {//是SOS报警状态
                        if (StaticManager.instance().isOutgoingConnected) {//接通了
                            LogUtils.e("已经接通过了");
                            resetState();
                            resetSoSState();
                        } else {//没接通
                            LogUtils.e("size " + StaticManager.instance().outgoingCalls.size() + "-- 第" + (StaticManager.instance().mCurrentOutgoingIndex + 1) +
                                    "次 -- 第" + (StaticManager.instance().mNoAnswerCount + 1) + "次循环");
                            StaticManager.instance().mCurrentOutgoingIndex++;//
                            if (StaticManager.instance().mCurrentOutgoingIndex <
                                    StaticManager.instance().outgoingCalls.size()) {
                                LogUtils.e("这轮还没打完接着打");
                            } else {
                                LogUtils.e("这轮打完了接着打下轮...");
                                StaticManager.instance().mNoAnswerCount++;
                                StaticManager.instance().mCurrentOutgoingIndex = 0;
                            }
                            LogUtils.e(StaticManager.instance().outgoingCalls.size() + " -- " + (StaticManager.instance().mCurrentOutgoingIndex + 1) + " -- " + (StaticManager.instance().mNoAnswerCount + 1));
                            if (StaticManager.instance().mNoAnswerCount < StaticManager.instance().sMaxNoAnswerCount) {
                                String nextNum = StaticManager.instance().outgoingCalls.get(StaticManager.instance().mCurrentOutgoingIndex);
                                LogUtils.e("SOS还没结束 开始拨打 " + nextNum + ".....");
                                PhoneUtils.call(nextNum);
                            } else {
                                LogUtils.e("打了两遍还没人接听");
                                resetState();
                                resetSoSState();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void resetState() {
        StaticManager.instance().mNoAnswerCount = 0;
        StaticManager.instance().mCurrentOutgoingIndex = 0;
        StaticManager.instance().outgoingCalls.clear();
    }

    private void resetSoSState() {
        StaticManager.instance().isOutgoingConnected = false;//重置为false
        StaticManager.instance().isSosReady = false;//置开关为false
        StaticManager.instance().isSosStart = false;
    }

}
