package com.android.fisewatchlauncher.function.wifi;

import android.net.wifi.ScanResult;
import android.text.TextUtils;

import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.entity.wifi.WifiDataType;
import com.android.fisewatchlauncher.entity.wifi.WifiSearcher;
import com.android.fisewatchlauncher.entity.wifi.WifiSyncBean;
import com.android.fisewatchlauncher.utils.ToastUtils;

import java.util.List;

/**
 * @author mare
 * @Description:TODO 同步Wifi
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/3
 * @time 11:28
 */
public class WifiSyncManager implements WifiSearcher.SearchWifiListener {
    private WifiSyncManager() {
    }

    private static class SingletonHolder {
        private static final WifiSyncManager INSTANCE = new WifiSyncManager();
    }

    public static WifiSyncManager instance() {
        return SingletonHolder.INSTANCE;
    }

    public interface WifiSyncListener {
        public void onSyncSuccess();

        public void onSyncFaliure();//同步失败
    }

    /**
     * TODO 开始同步wifi功能
     *
     * @param syncBean
     */
    public void startSync(WifiSyncBean syncBean) {
        WifiConnectUtil util = WifiConnectUtil.instance();
        boolean isWifiPreConnected = util.isWifiConnected();
        String connectedName = null;
        if (isWifiPreConnected) {//wifi打开状态 连接失败之后要立即连上此wifi
            connectedName = util.getConnectWifiSsid();
        }

        if (syncBean.isNeedResult) {///仅扫描附近wifi并发送结果
            //TODO 扫描附近的wifi列表
            WifiSearcher.instance().init(this);
        } else {//仅连接指定的wifi
            boolean isSuccess = util.connectWifi(syncBean.ssid, syncBean.pwd, syncBean.type);//开始连接wifi
            if (!isSuccess){//假如连接失败了
                if (isWifiPreConnected){
                    util.connectWifi(connectedName,null, WifiDataType.WIFI_CIPHER_NOPASS);
                }
            }
        }

    }


    @Override
    public void onSearchWifiFailed(WifiSearcher.ErrorType errorType) {
        //TODO 发送附近的wifi列表到server
    }

    @Override
    public void onSearchWifiSuccess(List<ScanResult> results) {
        //TODO 发送附近的wifi列表到server
    }

    public void parseWifiSync(String content){
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShortSafe("wifi账号密码为空....");
            return;
        }
        String[] ssidPwd = TextUtils.split(content,GlobalSettings.MSG_CONTENT_SEPERATOR);
        WifiSyncBean bean = new WifiSyncBean(ssidPwd[0], ssidPwd[1]);
        startSync(bean);//开始同步Wifi
    }
}
