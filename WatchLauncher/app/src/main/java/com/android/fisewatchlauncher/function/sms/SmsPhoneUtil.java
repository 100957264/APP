package com.android.fisewatchlauncher.function.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.widget.TextView;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/12
 * @time 20:48
 */
public class SmsPhoneUtil {

    static SmsContentObserver content;

    /**
     * <uses-permission android:name="android.permission.RECEIVE_SMS" />
     * 供其它组件调用 注册短信变化监听
     *
     * @param context
     */
    public static void registerSmsContentObserver(Activity context, String number, TextView text) {
        if (content != null) {
            return;
        }
        content = new SmsContentObserver(new Handler(), context, number, text);
        //注册短信变化监听
        context.getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, content);
    }

    /**
     * 供其它组件调用 关闭数据库监听
     *
     * @param context
     */
    public static void unregisterSmsContentObserver(Activity context) {
        if (content != null) {
            //关闭数据库监听
            context.getContentResolver().unregisterContentObserver(content);
        }
    }

    static BroadcastReceiver receiver;
    /**
     * 供其它组件调用 注册短信变化监听
     * @param context
     */
    public static void registerSmsBroadcastReceiver(Activity context,String number,TextView text){
        if (receiver!=null){
            return;
        }
        receiver= new SmsBroadcastReceiver(number,text);
        //注册短信变化监听
        context.registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

    }

    /**
     * 供其它组件调用 关闭数据库监听
     * @param context
     */
    public static void unregisterSmsBroadcastReceiver(Activity context){
        if (receiver!=null) {
            //关闭数据库监听
            context.unregisterReceiver(receiver);
        }
    }
}
