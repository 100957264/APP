package com.android.fisewatchlauncher.function.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.entity.wifi.WifiDataType;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by qingfeng on 2017/11/2.
 */

public class WifiConnectUtil {
    WifiManager mWifiManager;

    private static class SingletonHolder {
        private static final WifiConnectUtil INSTANCE = new WifiConnectUtil();
    }

    public static WifiConnectUtil instance() {
        return SingletonHolder.INSTANCE;
    }

    private WifiConnectUtil() {
        getWifiManager();
    }

    /**
     * forget one wifi password
     */
    public boolean forgetWifiPassword(String ssid) {
        boolean isDisconnectWifi = false;
        WifiInfo info = getCurrentWifiInfo();
        String connetedWifi = info.getSSID();
        if (TextUtils.equals(ssid, connetedWifi)) {
            mWifiManager.disableNetwork(info.getNetworkId());
            mWifiManager.disconnect();
        }
        List<WifiConfiguration> wifiConfigurationList = mWifiManager.getConfiguredNetworks();
        for (int i = 0; i < wifiConfigurationList.size(); i++) {
            LogUtils.e("you`ll be forgetting wifi: SSID = " + wifiConfigurationList.get(i).SSID + ",ssid =" + ssid);
            if (wifiConfigurationList.get(i).SSID.contains(ssid)) {
                mWifiManager.removeNetwork(wifiConfigurationList.get(i).networkId);
                mWifiManager.saveConfiguration();
                LogUtils.e("forgetWifiPassword: " + ssid + " remove successfully..");
                isDisconnectWifi = true;
            }
        }
        return isDisconnectWifi;
    }

    /**
     * forget all wifi password
     */
    public boolean forgetWifiPassword() {
        boolean isDisconnectWifi = false;
        mWifiManager.disconnect();
        List<WifiConfiguration> wifiConfigurationList = mWifiManager.getConfiguredNetworks();
        for (int i = 0; i < wifiConfigurationList.size(); i++) {
            LogUtils.e("you have forgot wifi: ssid = " + wifiConfigurationList.get(i).SSID);
            mWifiManager.removeNetwork(wifiConfigurationList.get(i).networkId);
            mWifiManager.saveConfiguration();
            isDisconnectWifi = true;
        }
        return isDisconnectWifi;
    }

    private WifiManager getWifiManager() {
        if (null == mWifiManager) {
            mWifiManager = (WifiManager) KApplication.sContext.getSystemService(WIFI_SERVICE);
        }
        return mWifiManager;
    }

    public boolean connectWifi(String ssid, String password, WifiDataType type) {
        mWifiManager = getWifiManager();
        //如果WIFI没有打开，则打开WIFI
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
        boolean isConnectedSuccess = false;
        boolean flag = false;
        mWifiManager.disconnect();
        boolean addSucess = addWifiNetwork(createWifiInfo(ssid, password, type));
        if (addSucess) {
            while (!flag && !isConnectedSuccess) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                String currSSID = getCurrentWifiInfo().getSSID();
                if (currSSID != null)
                    currSSID = currSSID.replace("\"", "");
                int currIp = getCurrentWifiInfo().getIpAddress();
                if (currSSID != null && currSSID.equals(ssid) && currIp != 0) {
                    isConnectedSuccess = true;
                } else {
                    flag = true;
                }
            }
        }
        return isConnectedSuccess;
    }

    public WifiInfo getCurrentWifiInfo() {
        return mWifiManager.getConnectionInfo();
    }

    public boolean isWifiConnected() {
        boolean isWifiConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) KApplication.sContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        NetworkInfo.State wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (info != null && info.isAvailable() && wifi == NetworkInfo.State.CONNECTED) {
            isWifiConnected = true;
        }
        return isWifiConnected;
    }

    public boolean addWifiNetwork(WifiConfiguration wcg) {
        int wcgId = mWifiManager.addNetwork(wcg);
        return mWifiManager.enableNetwork(wcgId, true);
    }

    public WifiConfiguration createWifiInfo(String ssid, String password, WifiDataType type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";

        if (type == WifiDataType.WIFI_CIPHER_NOPASS) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (type == WifiDataType.WIFI_CIPHER_WEP) {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (type == WifiDataType.WIFI_CIPHER_WPA) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.status = WifiConfiguration.Status.ENABLED;
        } else if (type == WifiDataType.WIFI_CIPHER_WPA2) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        return config;
    }

    private WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager
                .getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    /**
     * TODO 获取当前连接的wifi名称
     *
     * @return
     */
    public String getConnectWifiSsid() {
        WifiManager wifiManager = (WifiManager) KApplication.sContext.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        LogUtils.d("wifiInfo", wifiInfo.toString());
        LogUtils.d("SSID", wifiInfo.getSSID());
        return wifiInfo.getSSID();
    }

}
