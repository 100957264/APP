package com.android.fisewatchlauncher.function.sms;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.TextView;

/**
 * 原理:
 * Android收到短信后系统会发送一个android.provider.Telephony.SMS_RECEIVED广播。
 * 把它放在Bundle（intent.Extras）中，Bundle可以理解为一个Map，短信采用"pdus"作为键，
 * pdus应该是protocol description units的简写,也就是一组短信。
 * Android不是一接收到短信就立刻发出广播,会有一定的延迟,
 * 所以就有可能有多条短信,所以才会用数组来存放。
 *
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/12
 * @time 20:45
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private String address = null; //需要监听的号码
    private TextView text;

    public SmsBroadcastReceiver(String address, TextView text) {
        this.address = address;
        this.text = text;
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle bundle = intent.getExtras();
        SmsMessage[] smsMessages = null;
        Object[] pdus = null;
        if (bundle != null) {
            pdus = (Object[]) bundle.get("pdus");
        }
        if (pdus != null) {
            smsMessages = new SmsMessage[pdus.length];
            String sender = null;
            String content = null;


            for (int i = 0; i < pdus.length; i++) {
                smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sender = smsMessages[i].getOriginatingAddress(); // 获取短信的发送者
                content = smsMessages[i].getMessageBody(); // 获取短信的内容

                if (sender.equals(address)) {          //如果收到信息的号码和指定的号码相同
                    text.setText(content);                 //返回信息内容
                    break;
                }
            }
        }
    }

}