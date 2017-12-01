package com.android.fisewatchlauncher.function.phone;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.constant.ReceiverConstant;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/14
 * @time 11:19
 */
public class PhoneCallUtils {
    /**
     * 电话已接通广播
     */
    public static final String ACTION_CALL_CONNECTED = "com.fise.intent.ACTION_CALL_CONNECTED";
    /***
     * 关机键长按五秒SOS广播
     */
    public static final String ACTION_CALL_SOS = "fise.intent.action.SOS_CALL";

    /***
     * 挂断原因(主叫方挂断 被叫方挂断 无人接听)
     */
    public static final String ACTION_CALL_HANGUP_CAUSE = "com.fise.intent.ACTION_HANGUP_CAUSE";
    public static final String EXTRA_KEY_HANGUP_CAUSE = "hangupcause";
    public static final int EXTRA_FLAG_CALLER_HANGUP = 3;//主叫方挂断
    public static final int EXTRA_FLAG_NOANSWER_HANGUP = 36;//被叫方挂断 无人接听挂断

    public static void endCall() {
        try {
            com.android.internal.telephony.ITelephony.Stub.asInterface((IBinder) Class.forName("android.os.ServiceManager").getMethod("getService", new Class[]{
                    String.class
            }).invoke(null, new Object[]{
                    "phone"
            })).endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void answerRingingCall() {
        try {
            com.android.internal.telephony.ITelephony.Stub.asInterface((IBinder) Class.forName("android.os.ServiceManager").getMethod("getService", new Class[]{
                    String.class
            }).invoke(null, new Object[]{
                    "phone"
            })).answerRingingCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ITelephony getITelephony(TelephonyManager telephony) throws Exception {
        Method getITelephonyMethod = telephony.getClass().getDeclaredMethod("getITelephony");
        getITelephonyMethod.setAccessible(true);
        return (ITelephony) getITelephonyMethod.invoke(telephony);
    }

    /**
     * 无界面回拨号码监听
     *
     * @param content
     */
    public static void monitorCall(String content) {
        TelephonyManager tm = (TelephonyManager)KApplication.sContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (TelephonyManager.CALL_STATE_IDLE !=tm.getCallState()) {
            return;
        }
        boolean isNullContent = TextUtils.isEmpty(content);
        String targetNum;
        if (isNullContent) {
            targetNum = GlobalSettings.instance().getCenterNum();
        }else{
            targetNum = content;
        }
        if (TextUtils.isEmpty(targetNum)) {
            LogUtils.e("监听号码不能为空 ");
            return;
        }
        Intent callIntent = new Intent(ReceiverConstant.ACTION_HIDE_INCALL_UI);
        callIntent.putExtra("number",targetNum);
        KApplication.sContext.sendBroadcast(callIntent);
        //PhoneUtils.call(targetNum);
    }
}
