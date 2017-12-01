package com.android.fisewatchlauncher;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.android.fisewatchlauncher.acty.CameraPreviewWindow;
import com.android.fisewatchlauncher.function.wetchat.EaseMobManager;
import com.android.fisewatchlauncher.manager.LocationManager;
import com.android.fisewatchlauncher.net.HttpAPI;
import com.android.fisewatchlauncher.utils.Utils;

import java.util.Iterator;
import java.util.List;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/24
 * @time 14:32
 */
public class KApplication extends Application {
    public static Context sContext;

    // 记录是否已经初始化
    private boolean isInit = false;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        Utils.init(this);
        if(!(BuildConfig.FLAVOR.equals("FISE_WSD"))){
           CameraPreviewWindow.show(this);
        }
        /**** 初始化OKGO网络库**/
        HttpAPI.instance().init(this);
        iniThirdSdk();
    }

    private void iniThirdSdk() {
        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        /**
         * 如果app启用了远程的service，此application:onCreate会被调用2次
         * 为了防止SDK被初始化2次，加此判断会保证SDK被初始化1次
         * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
         */
        if (processAppName == null || !processAppName.equalsIgnoreCase(sContext.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return;
        }
        if (isInit) {
            return;
        }

        /**** 初始化融云sdk **/
//        RongCloundManager.init(this);
        /**** 初始化环信sdk **/
        EaseMobManager.instance().init(this);

        // 设置初始化已经完成
        isInit = true;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @param pid 进程的id
     * @return 返回进程的名字
     */
    private String getAppName(int pid) {
        String processName = null;
        ActivityManager activityManager = (ActivityManager) sContext.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName;
                    // 返回当前进程名
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }

    public void initLocationSDK(){
        LocationManager.instance().initLocationSDK();
    }
}
