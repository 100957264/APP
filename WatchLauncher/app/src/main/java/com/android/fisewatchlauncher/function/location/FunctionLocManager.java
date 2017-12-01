package com.android.fisewatchlauncher.function.location;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.client.msg.MsgType;
import com.android.fisewatchlauncher.entity.location.CdmaID;
import com.android.fisewatchlauncher.entity.location.GSMNeighboringCellInfo;
import com.android.fisewatchlauncher.entity.location.SCell;
import com.android.fisewatchlauncher.entity.wifi.NeiborWifi;
import com.android.fisewatchlauncher.entity.wifi.NeiborWifiInfo;
import com.android.fisewatchlauncher.entity.wifi.WifiSearcher;
import com.android.fisewatchlauncher.event.CdmaBaseStationEvent;
import com.android.fisewatchlauncher.function.gps.GPSResponsListener;
import com.android.fisewatchlauncher.function.gps.GpsBean;
import com.android.fisewatchlauncher.function.gps.GpsErrorType;
import com.android.fisewatchlauncher.function.gps.GpsManager;
import com.android.fisewatchlauncher.manager.StaticManager;
import com.android.fisewatchlauncher.parser.MsgParser;
import com.android.fisewatchlauncher.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author mare
 * @Description TODO: 负责开启结束定位
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/2
 * @time 17:03
 */
public class FunctionLocManager implements WifiSearcher.SearchWifiListener, GPSResponsListener {

    @Override
    public void onSearchWifiFailed(WifiSearcher.ErrorType errorType) {
    }

    @Override
    public void onSearchWifiSuccess(List<ScanResult> scanList) {
        NeiborWifiInfo neiborWifiInfo;
        List<NeiborWifiInfo> neiborWifiInfos = new ArrayList<>();
        for (ScanResult scanResult : scanList) {
            //对扫描结果的操作
            String ssid = scanResult.SSID; //获取服务集标识（路由器名字）
            String mac = scanResult.BSSID; //获取AP的mac地址
            int rssi = scanResult.level; //wifi信号强度，单位是dbm
            WifiManager.calculateSignalLevel(scanResult.level, 5);
            neiborWifiInfo = new NeiborWifiInfo(ssid, mac, rssi);
            neiborWifiInfos.add(neiborWifiInfo);
        }
        NeiborWifi neiborWifi = new NeiborWifi(neiborWifiInfos.size(), neiborWifiInfos);
        StaticManager.instance().setNeiborWifi(neiborWifi);

    }

    private FunctionLocManager() {
    }

    private static class SingletonHolder {
        private static final FunctionLocManager INSTANCE = new FunctionLocManager();
    }

    public static FunctionLocManager instance() {
        return SingletonHolder.INSTANCE;
    }

    private void preLocate() {
        WifiSearcher searcher = WifiSearcher.instance();
        searcher.init(this);
        startParseBaseStation();
    }

    public void start() {
        LogUtils.i("satrtSingleLoacte 准备定位..");
        preLocate();
        //TODO GPS Listener
        GpsManager.instance().start(this);
    }

    private void startParseBaseStation() {
        TelephonyManager tm = (TelephonyManager) KApplication.sContext.getSystemService(Context.TELEPHONY_SERVICE);
        // 返回值MCC + MNC
        String operator = tm.getNetworkOperator();
        LogUtils.e("operator " + operator);
        if (TextUtils.isEmpty(operator)) return;
        if (operator == null || operator.length() < 5) {
            LogUtils.i("mare " + "获取基站信息有问题,可能是手机没插sim卡");
            return;
        }
        String mcc = operator.substring(0, 3);
        String mnc = operator.substring(3);
        LogUtils.i("mcc - mnc : " + mcc + " - " + mnc);

        CellLocation cellLocation = tm.getCellLocation();
        if (cellLocation == null) {
            LogUtils.i("mare " + "手机没插sim卡吧");
            return;
        }

        // 中国移动和中国联通获取LAC、CID的方式
        boolean isGsm = tm.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM;
        boolean isCdma = tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA;
        int cid, lac;
        int nid = 0, bid, sid;
        if (isCdma) {
            // 中国电信获取LAC、CID的方式
            LogUtils.i("mare " + "现在是cdma基站");

            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation)
                    tm.getCellLocation();
            if (null == cdmaCellLocation) {
                LogUtils.e("获取电信基站信息为空.....");
                return;
            }
            nid = cdmaCellLocation.getNetworkId(); //获取cdma网络编号NID return cdma network identification number, -1 if unknown
            bid = cdmaCellLocation.getBaseStationId(); //获取cdma基站识别标号 BID
            sid = cdmaCellLocation.getSystemId(); //用谷歌API的话cdma网络的mnc要用这个getSystemId()取得→SID
            CdmaID cdmaID = new CdmaID(nid, bid, sid);

            StaticManager.instance().setCdmaID(cdmaID);
            LogUtils.d("电信基站信息： " + cdmaID.toString());
            EventBus.getDefault().post(new CdmaBaseStationEvent(cdmaID));
        } else if (isGsm) {
            LogUtils.i("mare " + "当前是gsm基站");
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
            lac = gsmCellLocation.getLac(); //获取gsm网络编号
            cid = gsmCellLocation.getCid(); //获取gsm基站识别标号
            LogUtils.i("mare " + " MCC移动国家代码 = " + mcc + "\t MNC移动网络号码 = "
                    + mnc + "\t LAC位置区域码 = " + lac + "\t CID基站编号 = " + cid);
            SCell sCell = new SCell(mcc, mnc, lac, cid);
            int neighboringLac;
            int neighboringCid;
            int neighboringBsss;
            int neighboringMnc;
            int neighboringMcc;
            if (null == gsmCellLocation) {
                LogUtils.e("获取联通移动基站信息为空.....");
                return;
            }
//            if (Build.VERSION.SDK_INT < 17) {//低版的android系统使用getNeighboringCellInfo方法
//            }else{
//
//            }
            List<NeighboringCellInfo> infos = tm.getNeighboringCellInfo();
            List<GSMNeighboringCellInfo> gsmNeighboringCellInfos = new ArrayList<>();
            List<GSMNeighboringCellInfo> gsmNeighboringResult = new ArrayList<>();//存放排好序的基站信息
            GSMNeighboringCellInfo gsmNeighboringCellInfo;
            for (NeighboringCellInfo info1 : infos) { // 根据邻区总数进行循环
                neighboringLac = info1.getLac(); // 取出当前邻区的LAC
                neighboringCid = info1.getCid(); // 取出当前邻区的CID
                neighboringBsss = -113 + 2 * info1.getRssi(); // 获取邻区基站信号强度
                gsmNeighboringCellInfo = new GSMNeighboringCellInfo(neighboringLac, neighboringCid, neighboringBsss);
                gsmNeighboringCellInfos.add(gsmNeighboringCellInfo);
            }
            sortStationByLevel(gsmNeighboringCellInfos);
            int maxLen = Math.min(7, gsmNeighboringCellInfos.size());//最多7个基站信息
            for (int i = 0; i < maxLen; i++) {
                gsmNeighboringResult.add(gsmNeighboringCellInfos.get(i));
            }
            int baseStationCount = gsmNeighboringResult.size();
            sCell.setBaseStationCount(baseStationCount);
            if (baseStationCount > 0) {
                sCell.setNeighboringCellInfos(gsmNeighboringResult);
            }
            StaticManager.instance().setsCell(sCell);
            LogUtils.d("基站个数： " + infos.size());
        } else {
            LogUtils.i("mare " + "现在不知道是什么鬼基站信息");
            return;
        }

    }

    /**
     * 信号强度从强到时弱进行排序
     *
     * @param list 存放周围基站信息对象的列表
     */
    private static void sortStationByLevel(List<GSMNeighboringCellInfo> list) {

        Collections.sort(list, new Comparator<GSMNeighboringCellInfo>() {

            @Override
            public int compare(GSMNeighboringCellInfo lhs, GSMNeighboringCellInfo rhs) {
                return rhs.neighboringBiss - lhs.neighboringBiss;
            }
        });
    }

    /**
     * 停止定位
     */
    public void stop() {
//        AMapLocationManager.instance().stop();
        //TODO 停止GPS定位

    }

    /**
     * 获取手机信号强度，需添加权限 android.permission.ACCESS_COARSE_LOCATION <br>
     * API要求不低于17 <br>
     *
     * @return 当前手机主卡信号强度, 单位 dBm（-1是默认值，表示获取失败）
     */
    public int getMobileDbm(Context context) {
        int dbm = -1;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        List<CellInfo> cellInfoList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            cellInfoList = tm.getAllCellInfo();
            if (null != cellInfoList) {
                for (CellInfo cellInfo : cellInfoList) {
                    if (cellInfo instanceof CellInfoGsm) {
                        CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm) cellInfo).getCellSignalStrength();
                        dbm = cellSignalStrengthGsm.getDbm();
                    } else if (cellInfo instanceof CellInfoCdma) {
                        CellSignalStrengthCdma cellSignalStrengthCdma =
                                ((CellInfoCdma) cellInfo).getCellSignalStrength();
                        dbm = cellSignalStrengthCdma.getDbm();
                    } else if (cellInfo instanceof CellInfoWcdma) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            CellSignalStrengthWcdma cellSignalStrengthWcdma =
                                    ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                            dbm = cellSignalStrengthWcdma.getDbm();
                        }
                    } else if (cellInfo instanceof CellInfoLte) {
                        CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte) cellInfo).getCellSignalStrength();
                        dbm = cellSignalStrengthLte.getDbm();
                    }
                }
            }
        }
        return dbm;
    }

    /**
     * 当前定位类型
     */
    public static final String LOCATION_TYPE_BASE = "BASE";
    public static final String LOCATION_TYPE_WIFI = "WIFI";

    @Override
    public void onLocateFailed(GpsErrorType errorType) {
        switch (errorType) {
            case LOCATE_TIMEOUT:
            case LOCATE_ERROR:
                //TODO 发送WG指令让服务器获取经纬度
                String noGpsDataStr = StaticManager.instance().getLocationBeanStr();
                MsgParser.instance().sendMsgByType(MsgType.WG, noGpsDataStr);
                break;
        }
    }

    @Override
    public void onLocateSuccess(GpsBean result) {
        // TODO 存到StaticManager里面
        upload();
    }

    public void upload() {

    }
}
