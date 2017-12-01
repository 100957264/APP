package com.android.fisewatchlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.fisewatchlauncher.acty.VideoEaseCallActivity;
import com.android.fisewatchlauncher.acty.VoiceEaseCallActivity;
import com.android.fisewatchlauncher.function.wetchat.EaseMobManager;
import com.android.fisewatchlauncher.utils.LogUtils;

/**
 * @author mare
 * @Description:TODO 环信语音视频通话广播
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/6
 * @time 14:55
 */
public class EaseCallReceiver extends BroadcastReceiver {

    public EaseCallReceiver() {
        LogUtils.e("EaseCallReceiver init...");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!EaseMobManager.instance().isLoggedIn())//没登陆就不启动
            return;
        //username
        String from = intent.getStringExtra("from");
        //call type
        String type = intent.getStringExtra("type");
        if ("video".equals(type)) { //video call
            context.startActivity(new Intent(context, VideoEaseCallActivity.class).
                    putExtra("username", from).putExtra("isComingCall", true).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else { //voice call
            context.startActivity(new Intent(context, VoiceEaseCallActivity.class).
                    putExtra("username", from).putExtra("isComingCall", true).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
