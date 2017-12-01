package com.android.fisewatchlauncher.manager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Process;
import android.util.Log;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.event.OnInitialized;
import com.android.fisewatchlauncher.service.FiseService;

import org.greenrobot.eventbus.EventBus;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/30
 * @time 14:44
 */
public class Initializer {
    private final static String TAG = "Initializer";
    private boolean initialized = false;
    private static Initializer sInstance;
    private Context appCtx;
    private Initializer() {}
    public static Initializer instance() {
        if (sInstance == null) {
            sInstance = new Initializer();
        }
        return sInstance;
    }
    public void initialize(Context context) {
        if (!initialized) {
            appCtx = KApplication.sContext;
            new InitializeTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            EventBus.getDefault().post(new OnInitialized());
        }
    }
    public boolean isInitialized() {
        return initialized;
    }

    private void doInitializeSync() {
        // TODO: add initialize in main thread
       // BatteryStatusMonitor.instance().init(appCtx);

    }
    private void doInitializeAsync() {
        // TODO: Add time-cost initialize here
        appCtx.startService(new Intent(appCtx, FiseService.class));
        PreferControler.instance();
        DBManager.instance().getDaoSession();
        NetManager.instance();
        initialized = true;
    }

    private class InitializeTask extends AsyncTask<Void, Void, Void> {
        private InitializeTask() {}
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doInitializeSync();
            Log.d(TAG, "InitializeTask onPreExecute");
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            Log.d(TAG, "InitializeTask doInBackground");
            doInitializeAsync();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG, "InitializeTask onPostExecute result:" + result);
            EventBus.getDefault().post(new OnInitialized());
        }
    }
}
