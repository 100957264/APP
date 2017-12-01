package com.android.fisewatchlauncher.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.android.fisewatchlauncher.KApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hw on 16-9-26.
 */
public class DeviceInfoUtils {

    public static final int TYPE_ADS = 1;
    public static final int TYPE_BANNER = 2;

    public static final int SID = 1024;
    public static final String AID = "10018";


    /**
     * 判断设备是否具有某权限
     */
    public static boolean checkPermission(String permName) {
        PackageManager pm = KApplication.sContext.getPackageManager();
        return PackageManager.PERMISSION_GRANTED == pm.checkPermission(permName, KApplication.sContext.getPackageName());
    }


    /* 手机基础信息 start*/
    //获取device_id
    public static String getDeviceId(Context ctx) {
        String deviceId = Settings.Secure.getString(ctx.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        LogUtils.d("getDeviceId : " + deviceId);
        return deviceId;
    }

    //获取device_imei
    public static String getIMEI(Context ctx) {
        if (checkPermission(Manifest.permission.READ_PHONE_STATE)){
            try {
                TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                String imei = tm.getDeviceId();
                LogUtils.d("getIMEI : " + imei);
                return imei == null ? "" : imei;
            } catch (Exception e) {
                return "";
            }
        }
        return  "";
    }

    //获取系统软件版本号
    public static String getSoftwareVersion(Context ctx) {

        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        LogUtils.d("getSoftwareVersion : " + tm.getDeviceSoftwareVersion());
        return tm.getDeviceSoftwareVersion() == null ? "" : tm.getDeviceSoftwareVersion();
    }

    //获取系统版本号
    public static String getOsVersion() {
        LogUtils.d("getOsVersion : " + Build.VERSION.RELEASE);
        return Build.VERSION.RELEASE;
    }

    //获取 MANUFACTURER 生产厂家
    public static String getDV() {
        LogUtils.d("getDV : " + Build.MANUFACTURER);
        return Build.MANUFACTURER;
    }

    //获取 MODEL 手机型号
    public static String getDM() {
        LogUtils.d("getDM : " + Build.MODEL);
        return Build.MODEL;
    }

    //获取手机SDK版本号
    public static int getAndroidSDKVersion() {
        LogUtils.d("getDM : " + Build.VERSION.SDK_INT);
        return Build.VERSION.SDK_INT;
    }


    public static String getOPID(Context ctx) {
        String imsi = getSubscriberId(ctx);
        LogUtils.d("imsi : " + imsi);
        if (imsi != null && imsi.length() > 6) {
            LogUtils.d("getOPID : " + imsi.substring(0, 5));
            return imsi.substring(0, 5);
        }
        LogUtils.d("getOPID : " + "");
        return "";
    }

    //获取屏幕宽度
    public static String getSCreenWidth(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        LogUtils.d("getSCreenWidth : " + String.valueOf(width));
        return String.valueOf(width);
    }

    //获取屏幕高度
    public static String getScreenHeight(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        LogUtils.d("getScreenHeight : " + String.valueOf(height));
        return String.valueOf(height);
    }

    /**
     * 获取设备信息,可根据需要自行截取要使用的
     *
     * @return
     */
    public static String getDeviceInfo(Context context) {
        StringBuffer sb = new StringBuffer();
        sb.append("设备名称： " + Build.MODEL + "\n");
        sb.append("imei： " + getIMEI(context) + "\n");
        sb.append("androidid： " + getAndroidId(context) + "\n");
        sb.append("安装路径： " + context.getPackageResourcePath() + "\n");
        sb.append(" -----------------------------------------------------------------------------------------" + "\n");
        sb.append("包名： " + context.getPackageName() + "\n");
        sb.append("版本名： " + getVersionName(context) + "\n");
        sb.append("版本号： " + getVersionCode(context) + "\n");
        sb.append("分辨率： " + getSCreenWidth(context) + "*" + getScreenHeight(context) + "\n");
        sb.append("系统板本： " + Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")" + "\n");
        sb.append("签名hashcode： " + checkAPPHashCode(context) + "\n");
        sb.append("安装应用权限： " + isSystemApp(context) + "\n");
        Log.d("ConfigActivity", "getConfigInfos : " + sb.toString());

        return sb.toString();
    }

    private static String getAndroidId(Context context) {
        String m_szAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return m_szAndroidID;
    }

    private static String getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String versionName = packInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isSystemApp(Context context) {

        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getVersionCode(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String versionCode = String.valueOf(packInfo.versionCode);
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    static String checkAPPHashCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];

            int hashcode = sign.hashCode();
            return String.valueOf(hashcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "-1";
    }
/* 手机基础信息 end*/

    /* 手机网络信息 start*/
    //获取网络连接类型
    public static String getNT(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                LogUtils.d("getNT 1: " + "2");
                return "2";
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                int nt = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? 5 : 4) : 4;
                LogUtils.d("getNT 2: " + String.valueOf(nt));
                return String.valueOf(nt);
            }
            LogUtils.d("getNT 3: " + "0");
            return "0";
        } else {
            LogUtils.d("getNT 4: " + "0");
            return "0";
        }
    }

    //获取网络移动数据连接类型
    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    //获取MAC地址
    public static String getMac(Context ctx) {
        WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();
        String tmp = info.getMacAddress().toUpperCase();
        if ("02:00:00:00:00:00".equals(tmp)) {
            if (!TextUtils.isEmpty(getShellMac())){
                tmp = getShellMac().toUpperCase();
            }
        }
        tmp = tmp.toUpperCase();
        LogUtils.d("getMac : " + tmp);
        return tmp;
    }


    /**
     * 这是使用adb shell命令来获取mac地址的方式
     *
     * @return
     */
    public static String getShellMac() {
        String macSerial = null;
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * 获取当前ip地址
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        try {
            // for (Enumeration<NetworkInterface> en = NetworkInterface
            // .getNetworkInterfaces(); en.hasMoreElements();) {
            // NetworkInterface intf = en.nextElement();
            // for (Enumeration<InetAddress> enumIpAddr = intf
            // .getInetAddresses(); enumIpAddr.hasMoreElements();) {
            // InetAddress inetAddress = enumIpAddr.nextElement();
            // if (!inetAddress.isLoopbackAddress()) {
            // return inetAddress.getHostAddress().toString();
            // }
            // }
            // }
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return "";
        }
        // return null;
    }

    //WIFI是否连接
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetInfo.isConnected();
    }

    //移动网络是否连接
    public static boolean isMobileDataConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return mobNetInfo.isConnected();
    }

    //获取网络附加信息
    public static String getNetWorkExtraInfo(Context context) {
        ConnectivityManager connectionManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取网络的状态信息，有下面三种方式
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (!TextUtils.isEmpty(networkInfo.getExtraInfo())) {
            return networkInfo.getExtraInfo();
        }

        return "";
    }

    //获取连接状态
    public static NetworkInfo.State getNetWorStates(Context context) {
        ConnectivityManager connectionManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取网络的状态信息，有下面三种方式
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        return networkInfo.getState();

    }

    //Return a human-readable name describing the subtype of the network.
    public static String getNetWorkSubtypeName(Context context) {
        ConnectivityManager connectionManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取网络的状态信息，有下面三种方式
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (!TextUtils.isEmpty(networkInfo.getSubtypeName())) {
            return networkInfo.getSubtypeName();
        }

        return "";
    }

    //获取详细状态。
    public static NetworkInfo.DetailedState getNetWorkDetailedState(Context context) {
        ConnectivityManager connectionManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取网络的状态信息，有下面三种方式
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();

        return networkInfo.getDetailedState();
    }

    //获取BSSID属性 也就是路由器的mac
    public static String getNetWorkBSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (!TextUtils.isEmpty(wifiInfo.getBSSID())) {
            return wifiInfo.getBSSID();
        }

        return "";
    }

    //获取SSID 也就是wifi名称
    public static String getNetWorkSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (!TextUtils.isEmpty(wifiInfo.getSSID())) {
            return wifiInfo.getSSID();
        }

        return "";
    }
/* 手机网络信息 end*/

/* 手机电话信息 start*/

    /**
     * 获取手机号 取出MSISDN，很可能为空
     *
     * @return
     */
    public static String getPhoneNumber(Context ctx) {
        if (checkPermission(Manifest.permission.READ_SMS)){
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getLine1Number();
        }
        return "";
    }

    /**
     * ICCID:ICC identity集成电路卡标识，这个是唯一标识一张卡片物理号码的
     *
     * @return
     */
    public static String getIccid(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (isSimReady(ctx)) {
            return tm.getSimSerialNumber();
        }
        return "";
    }

    /**
     * IMSI 全称为 International Mobile Subscriber Identity，中文翻译为国际移动用户识别码。
     * 它是在公众陆地移动电话网（PLMN）中用于唯一识别移动用户的一个号码。在GSM网络，这个号码通常被存放在SIM卡中
     *
     * @return
     */
    public static String getSubscriberId(Context ctx) {

        if (checkPermission(Manifest.permission.READ_PHONE_STATE)) {
            try {
                TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                if (isSimReady(ctx)) {
                    return tm.getSubscriberId();
                }
            } catch (Exception e) {
            }
        }
        return "";
    }

    //获取MCC 移动国家号码，由3位数字组成
    public static String getMCC(Context ctx) {
        String imsi = getSubscriberId(ctx);
        if (imsi != null && !imsi.equals("")) {
            return imsi.substring(0, 2);
        }
        return "";
    }

    //获取MCC 移动国家号码，由3位数字组成
    public static String getMSN(Context ctx) {
        String imsi = getSubscriberId(ctx);
        if (imsi != null && !imsi.equals("")) {
            return imsi.substring(3, 4);
        }
        return "";
    }

    /**
     * 判断SIM卡是否准备好
     *
     * @param context
     * @return
     */
    public static boolean isSimReady(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            int simState = tm.getSimState();
            if (simState == TelephonyManager.SIM_STATE_READY) {
                return true;
            }
        } catch (Exception e) {
            Log.w("PhoneHelper", "021:" + e.toString());
        }
        return false;
    }

    /**
     * 返回ISO标准的国家码，即国际长途区号
     *
     * @return
     */
    public static String getNetworkCountrylso(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (!TextUtils.isEmpty(tm.getNetworkCountryIso())) {
            return tm.getNetworkCountryIso();
        }
        return "";
    }

    /**
     * 返回MCC+MNC代码 (网络运营商国家代码和运营商网络代码)
     *
     * @return
     */
    public static String getNetworkOperator(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (!TextUtils.isEmpty(tm.getNetworkOperator())) {
            return tm.getNetworkOperator();
        }
        return "";
    }

    /**
     * 返回移动网络运营商的名字(SPN)
     *
     * @return
     */
    public static String getNetworkOperatorName(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (!TextUtils.isEmpty(tm.getNetworkOperatorName())) {
            return tm.getNetworkOperatorName();
        }
        return "";
    }

    /**
     * 返回SIM卡提供商的国家代码
     *
     * @return
     */
    public static String getSimCountryIso(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (!TextUtils.isEmpty(tm.getSimCountryIso())) {
            return tm.getSimCountryIso();
        }
        return "";
    }

    /**
     * 返回MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)(IMSI)
     *
     * @return
     */
    public static String getSimOperator(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (!TextUtils.isEmpty(tm.getSimOperator())) {
            return tm.getSimOperator();
        }
        return "";
    }

    /**
     * 返回SIM卡网络运营商的名字(SPN)
     *
     * @return
     */
    public static String getSimOperatorName(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (!TextUtils.isEmpty(tm.getSimOperatorName())) {
            return tm.getSimOperatorName();
        }
        return "";
    }

    /**
     * 返回SIM卡的序列号(IMEI)
     *
     * @return
     */
    public static String getSimSerialNumber(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (!TextUtils.isEmpty(tm.getSimSerialNumber())) {
            return tm.getSimSerialNumber();
        }
        return "";
    }

    /**
     * 返回SIM卡的序列号(IMEI)
     *
     * @return
     */
    public static String getVoiceMailNumber(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (!TextUtils.isEmpty(tm.getVoiceMailNumber())) {
            return tm.getVoiceMailNumber();
        }
        return "";
    }

    /**
     * 返回语音邮件号码
     *
     * @return
     */
    public static String getVoiceMailAlphaTag(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (!TextUtils.isEmpty(tm.getVoiceMailAlphaTag())) {
            return tm.getVoiceMailAlphaTag();
        }
        return "";
    }

    /**
     * 返回移动终端的软件版本，例如：GSM手机的IMEI/SV码。
     *
     * @return
     */
    public static String getDeviceSoftwareVersion(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (!TextUtils.isEmpty(tm.getDeviceSoftwareVersion())) {
            return tm.getDeviceSoftwareVersion();
        }
        return "";
    }

    //MD5加密
    public static String Md5Encode(String string) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("MD5");
            sha.reset();
            sha.update(string.getBytes());

            byte[] md = sha.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            String code = new String(str).toLowerCase();
            LogUtils.d("Md5Encode : " + code);
            return code;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 从assets 文件夹中读取图片
     */
    public static Drawable loadFolderImageFromAsserts(final Context ctx, String fileName) {
        try {
            InputStream is = ctx.getResources().getAssets().open(fileName);
            return Drawable.createFromStream(is, null);
        } catch (IOException e) {
            if (e != null) {
                e.printStackTrace();
            }
        } catch (OutOfMemoryError e) {
            if (e != null) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isInstallShuangkaihezi(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            if (packageInfo != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

}
