package com.android.fisewatchlauncher.client;

import android.os.Handler;
import android.os.Looper;

/**
 */
public abstract class BaseSocket {
    public Handler mUIHandler;
    protected Object lock;

    public BaseSocket() {
        mUIHandler = new Handler(Looper.getMainLooper());
        lock = new Object();
    }

    protected void runOnUiThread(Runnable runnable) {
        mUIHandler.post(runnable);
    }
}
