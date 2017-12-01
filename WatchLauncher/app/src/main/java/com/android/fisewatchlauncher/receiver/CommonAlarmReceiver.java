package com.android.fisewatchlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.android.fisewatchlauncher.BuildConfig;
import com.android.fisewatchlauncher.constant.ReceiverConstant;
import com.android.fisewatchlauncher.function.alarm.AlarmEntity;
import com.android.fisewatchlauncher.function.alarm.AlarmTimer;
import com.android.fisewatchlauncher.manager.LocationManager;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.ToastUtils;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/13
 * @time 14:29
 */
public class CommonAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String logTxt = " CommonAlarmReceiver " + action;
        LogUtils.e(logTxt);
        switch (action) {
            case ReceiverConstant.LOCATION_START://定位指令开始广播
                LocationManager.instance().startLocate();//开始定位
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // >= 4.4就再更新下闹钟
                    AlarmTimer.setLocationAlarmStart(context);//设置重复的闹钟
                } else {
                    // do nothing 重复的闹钟自动生效
                }
                break;
            case ReceiverConstant.LOCATION_STOP://定位指令停止广播
                AlarmTimer.cancelAlarmTimer(context, new AlarmEntity(AlarmEntity.Type.LocateStart));
                break;
            case ReceiverConstant.COMMON_CLOCK:
                break;
            case ReceiverConstant.CLASS_FORBIDEN:
                break;
            case ReceiverConstant.CONFIRMED_FREQUENCY_UPLOAD://固定频率上传位置信息广播
                if (BuildConfig.LOG_DEBUG) {
                    ToastUtils.showShort("固定频率上传中");
                }
                LocationManager.instance().startLocate();//开始定位
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    AlarmTimer.startConfirmedFrequencyUpload(context);//开启下一个闹钟
                }
                break;
            default:
                break;
        }
    }
}
