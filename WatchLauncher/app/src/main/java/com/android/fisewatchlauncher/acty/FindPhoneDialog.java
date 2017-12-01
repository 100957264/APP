package com.android.fisewatchlauncher.acty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.event.BreakFindPhoneEvent;
import com.android.fisewatchlauncher.utils.AppUtils;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.MediaManager;
import com.android.fisewatchlauncher.utils.PowerUtils;
import com.android.fisewatchlauncher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.android.fisewatchlauncher.utils.MediaManager.stopFindDevice;

/**
 * @author mare
 * @Description:TODO 查找设备
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/19
 * @time 20:22
 */
public class FindPhoneDialog extends BaseActivity implements View.OnTouchListener {
    TelephonyManager tm;

    public static void pull(Context context) {
        boolean isForeGround = AppUtils.isForeground(context, FindPhoneDialog.class.getName());
        LogUtils.e("isForeGround " + isForeGround);
        if (!isForeGround) {
            context.startActivity(new Intent(context, FindPhoneDialog.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.find_phone);
        LogUtils.e("onCreate savedInstanceState " + savedInstanceState);
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        findViewById(R.id.main_find).setOnTouchListener(this);
        EventBus.getDefault().register(this);
        boolean isInCall = tm.getCallState() != TelephonyManager.CALL_STATE_IDLE;
        if (isInCall) {
            ToastUtils.showShort("Audio focus is busying");
        } else {
            stopFind();
            startFind();
        }
    }

    private void startFind() {
        LogUtils.e("startFind ");
        PowerUtils.instance().acquireWakeLock();
        MediaManager.findDevice(R.raw.find, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void stopFind() {
        LogUtils.e("stopFind ");
        PowerUtils.instance().releaseWakeLock();
        stopFindDevice();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        stopFind();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopFind();
        this.finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        stopFind();
        finish();
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFindBreak(BreakFindPhoneEvent event) {
        LogUtils.i(event.toString());
        boolean isPause = event.isPause;
        if (isPause) {//中断找设备
            stopFind();
        } else {
            startFind();
        }
    }
}
