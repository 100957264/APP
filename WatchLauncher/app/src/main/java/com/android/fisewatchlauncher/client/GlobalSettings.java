package com.android.fisewatchlauncher.client;

import android.content.Context;
import android.text.TextUtils;

import com.android.fisewatchlauncher.BuildConfig;
import com.android.fisewatchlauncher.entity.dao.CenterSettings;
import com.android.fisewatchlauncher.event.QrUpdateEvent;
import com.android.fisewatchlauncher.manager.PreferControler;
import com.android.fisewatchlauncher.prenster.dao.CenterSettingsUtils;
import com.android.fisewatchlauncher.utils.DeviceInfoUtils;
import com.android.fisewatchlauncher.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.nio.charset.Charset;

/**
 * Created by fanyang on 2017/8/4.
 */
public class GlobalSettings {

    /**
     * 厂商
     */
    protected final static String product = BuildConfig.PRODUCT;
    /**
     * 设备ID
     */
    public final static String MSG_HEADER_SEPERATOR = "*";
    public final static String MSG_HEADER_SEPERATOR_PARSER = "\\*";// * 属于特殊字符 解析时候必须\\转义
    public final static String MSG_CONTENT_SEPERATOR = ",";
    public final static String MSG_PREFIX = "[";
    public final static String MSG_SUFFIX = "]";
    public final static String MSG_PREFIX_ESCAPE = "[";
    public final static String MSG_SUFFIX_ESCAPE = "]";
    public final static String TEST_DOMAIN_NAME = "api.xcloudtech.com";

    public static int PORT = BuildConfig.PORT;
    public static String IP = BuildConfig.IP;
    //    public static String IP = "192.168.11.162";
    private String imei = "";

    private final static String privateKey = BuildConfig.PRIVATE_KEY;

//    "218.17.161.66" //大巷北斗
//    PORT = " 10300"

    private TcpConnConfig config;

    private GlobalSettings() {
        LogUtils.e("getProduct "+product);
        LogUtils.e("isSmartProtocol "+BuildConfig.isSmartProtocol);
    }

    private static class SingletonHolder {
        private static final GlobalSettings INSTANCE = new GlobalSettings();
    }

    public static GlobalSettings instance() {
        return SingletonHolder.INSTANCE;
    }

    public String getProduct() {
        return product;
    }

    public String getIP() {
        if (!TextUtils.isEmpty(GlobalSettings.IP)) return GlobalSettings.IP;
        return CenterSettingsUtils.instance().getIP();
    }

    public void setIP(String ip) {
        if (TextUtils.isEmpty(ip)) {
            return;
        }
        GlobalSettings.IP = ip;
        CenterSettings settings = new CenterSettings(GlobalSettings.instance().getImei());
        settings.setCenter_ip(ip);
        CenterSettingsUtils.instance().updateIp(settings);
    }

    public void setPort(int port) {
        if (port <= 0) {
            return;
        }
        GlobalSettings.PORT = port;
        CenterSettingsUtils.instance().updatePort(port);
    }

    public int getPort() {
        if (GlobalSettings.PORT > 0) return GlobalSettings.PORT;
        return CenterSettingsUtils.instance().queryPort();
    }

    public TcpConnConfig getConfig() {
        return config;
    }

    public void setConfig(TcpConnConfig config) {
        this.config = config;
    }

    public void saveImei(Context ctx) {
        String id = DeviceInfoUtils.getIccid(ctx);
        LogUtils.i("id = " + id);
        String imei = DeviceInfoUtils.getIMEI(ctx);
        LogUtils.i("imei = " + imei);
        GlobalSettings.instance().setImei(imei);
        QrUpdateEvent event = new QrUpdateEvent(QrUpdateEvent.Type.UPDATE_QR, GlobalSettings.instance().getImei());
        EventBus.getDefault().post(event);
    }

    public String getImei() {
        if (!TextUtils.isEmpty(imei)) return imei;
        String cachedImei = PreferControler.instance().getImei();
        if (!TextUtils.isEmpty(cachedImei)) {
            this.imei = cachedImei;
        }
        return cachedImei;
    }

    public void setImei(String imei) {
        this.imei = imei;
        PreferControler.instance().setImei(imei);
    }

    public String getPrivateKey() {
        return privateKey;
    }

    private String centerNum;

    public String getCenterNum() {
        if (!TextUtils.isEmpty(centerNum)) return centerNum;
        return CenterSettingsUtils.instance().queryCenterPhoneNum();
    }

    public void setCenterNum(String centerNum) {
        if (TextUtils.isEmpty(centerNum)) {
            return;
        }
        this.centerNum = centerNum;
        CenterSettingsUtils.instance().updateCenterPhoneNum(centerNum);
    }

    /**
     * 获得编码(要在setConfig之后)
     * @return
     */
    public Charset getChaset(){
       return Charset.forName(config.getCharsetName());
    }
}
