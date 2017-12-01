package com.android.fisewatchlauncher.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.android.fisewatchlauncher.constant.ReceiverConstant;
import com.android.fisewatchlauncher.utils.LogUtils;

/**
 * @author mare
 * @Description:TODO 短信相关的广播
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/10
 * @time 20:14
 */
public class SMSReceiver extends BroadcastReceiver {

    public SMSReceiver() {
        LogUtils.i("SMSReceiver inited .....");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ReceiverConstant.ACTION_SMS_RELATIVE)) {
            int flag = intent.getIntExtra(ReceiverConstant.EXTRA_SMS_FLAG, 0);
            int resultCode = getResultCode();
            switch (flag) {
                case ReceiverConstant.EXTRA_SMS_SEND_FLAG:
                    String phoneNum = intent.getStringExtra(ReceiverConstant.EXTRA_SMS_SEND_NUM);
                    switch (resultCode) {
                        case Activity.RESULT_OK:
                            LogUtils.e("Send Message to " + phoneNum + " success!");
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                        default:
                            LogUtils.e("Send Message to " + phoneNum + " fail!");
//                            ToastUtils.showShort("Send Message to " + phoneNum + " fail!");
                            break;
                    }
                    break;

                case ReceiverConstant.EXTRA_SMS_DELIVERY_FLAG:
                    String deliveryNum = intent.getStringExtra(ReceiverConstant.EXTRA_SMS_SEND_NUM);
                    switch (resultCode) {
                        case Activity.RESULT_OK:
                            LogUtils.e("短信已成功送达 " + deliveryNum + "!");
                            break;
                        case Activity.RESULT_CANCELED:
                        default:
                            LogUtils.e("短信送达 " + deliveryNum + " 失败!");
//                            ToastUtils.showShort("短信送达 " + deliveryNum + " 失败!");
                            break;
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
