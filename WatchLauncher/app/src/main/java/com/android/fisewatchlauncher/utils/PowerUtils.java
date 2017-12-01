package com.android.fisewatchlauncher.utils;

import android.os.PowerManager;

import com.android.fisewatchlauncher.KApplication;

import static android.content.Context.POWER_SERVICE;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/19
 * @time 15:51
 */
public class PowerUtils {
    private PowerUtils() {

    }

    private static class SingletonHolder {
        private static final PowerUtils INSTANCE = new PowerUtils();
    }

    public static PowerUtils instance() {
        return PowerUtils.SingletonHolder.INSTANCE;
    }

    PowerManager.WakeLock mWakeLock = null;

    @SuppressWarnings("deprecation")
    public void acquireWakeLock() {
        if (mWakeLock == null) {
            PowerManager pm = (PowerManager) KApplication.sContext.getSystemService(POWER_SERVICE);
//            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getSimpleName());
            mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "find_phone");//亮屏
            mWakeLock.acquire();
        }
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void releaseWakeLock() {
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}
