package com.android.fisewatchlauncher.manager;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.listener.CustomPhoneStateListener;

import static android.telephony.PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR;
import static android.telephony.PhoneStateListener.LISTEN_CALL_STATE;
import static android.telephony.PhoneStateListener.LISTEN_CELL_LOCATION;
import static android.telephony.PhoneStateListener.LISTEN_DATA_ACTIVITY;
import static android.telephony.PhoneStateListener.LISTEN_DATA_CONNECTION_STATE;
import static android.telephony.PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR;
import static android.telephony.PhoneStateListener.LISTEN_SERVICE_STATE;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/9
 * @time 9:34
 */
public class NetManager {

    private NetManager() {
    }

    private static class SingletonHolder {
        private static final NetManager INSTANCE = new NetManager();
    }

    public static NetManager instance() {
        return SingletonHolder.INSTANCE;
    }

    private void cacheIP(Context ctx) {
        String settingIp = GlobalSettings.instance().getIP();
        if (!TextUtils.isEmpty(settingIp)) {
            return;
        }

        //=============智慧魔方域名获取IP=============================
//        if (TextUtils.isEmpty(settingIp) && NetworkUtil.isNetworkAvailable(KApplication.sContext)) {
//            NetworkUtil.domain2IP(GlobalSettings.TEST_DOMAIN_NAME, new IPSubscribe<String>(ctx, null));
//        }
        //==================智慧魔方域名获取IP========================
    }

    public void init(Context ctx) {
        cacheIP(ctx);
        cacheTmData(ctx);
    }

    //设置监听器方法
    CustomPhoneStateListener phoneStateListener;

    private void cacheTmData(Context ctx) {
        //注册监听器，设定不同的监听类型
        if (null == phoneStateListener) {
            phoneStateListener = new CustomPhoneStateListener(ctx);
            TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
                    | LISTEN_SERVICE_STATE | LISTEN_MESSAGE_WAITING_INDICATOR | LISTEN_CALL_FORWARDING_INDICATOR | LISTEN_CELL_LOCATION
                    | LISTEN_CALL_STATE | LISTEN_DATA_CONNECTION_STATE | LISTEN_DATA_ACTIVITY);
        }
    }

}

