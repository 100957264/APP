package com.android.fisewatchlauncher.acty;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.event.OnInitialized;
import com.android.fisewatchlauncher.manager.Initializer;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import rx.functions.Action1;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/30
 * @time 11:46
 */
public class SplashActivity extends BaseActivity {
    private final static String TAG = "SplashActivity";
    private final static int MIN_SPLASH_DURATION = 1000;
    private boolean minDurationElapsed;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable startMainRunnable = new Runnable() {
        @Override
        public void run() {
            minDurationElapsed = true;
            if (Initializer.instance().isInitialized()) {
                startActivity(new Intent(SplashActivity.this, FiseLauncherActivity.class));
            }
        }
    };

    private static final String[] PERMISSION = new String[]{
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
    };

    private void getPermission(final Context ctx) {
        RxPermissions.getInstance(this)
                .request(PERMISSION[0], PERMISSION[1], PERMISSION[2], PERMISSION[3])
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            // 已经获取权限
                            GlobalSettings.instance().saveImei(ctx);
                        } else {
                            // 未获取权限
                            Toast.makeText(ctx, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_splash);
        getPermission(this);
        startActivity(new Intent(this, FiseLauncherActivity.class));
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        minDurationElapsed = false;
        handler.postDelayed(startMainRunnable, MIN_SPLASH_DURATION);
        Initializer.instance().initialize(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        handler.removeCallbacks(startMainRunnable);
        finish();
    }

    @Subscribe
    public void onEvent(OnInitialized event) {
        Log.d(TAG, "OnInitialized ....");
        if (minDurationElapsed) {
            startActivity(new Intent(SplashActivity.this, FiseLauncherActivity.class));
        }
    }
}
