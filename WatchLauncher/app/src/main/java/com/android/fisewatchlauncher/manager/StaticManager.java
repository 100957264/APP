package com.android.fisewatchlauncher.manager;

import android.text.TextUtils;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.constant.FileConstant;
import com.android.fisewatchlauncher.entity.dao.StepHistory;
import com.android.fisewatchlauncher.entity.location.CdmaID;
import com.android.fisewatchlauncher.entity.location.SCell;
import com.android.fisewatchlauncher.entity.msg.TcpMsg;
import com.android.fisewatchlauncher.entity.wifi.NeiborWifi;
import com.android.fisewatchlauncher.event.BatteryLow;
import com.android.fisewatchlauncher.event.BatteryOkay;
import com.android.fisewatchlauncher.event.BatteryStatus;
import com.android.fisewatchlauncher.event.CdmaBaseStationEvent;
import com.android.fisewatchlauncher.event.DataActivityUpdateEvent;
import com.android.fisewatchlauncher.event.DataConnectionStateChangedEvent;
import com.android.fisewatchlauncher.event.LocationEvent;
import com.android.fisewatchlauncher.event.NetworkTypeEvent;
import com.android.fisewatchlauncher.event.PowerConnection;
import com.android.fisewatchlauncher.event.SignalStrengthChangedEvent;
import com.android.fisewatchlauncher.event.StepUpdateEvent;
import com.android.fisewatchlauncher.event.SwitchLowBatEvent;
import com.android.fisewatchlauncher.event.SwitchSmsSosEvent;
import com.android.fisewatchlauncher.event.TcpServerConnectedEvent;
import com.android.fisewatchlauncher.event.TimeUpdateEvent;
import com.android.fisewatchlauncher.event.TurnOverEvent;
import com.android.fisewatchlauncher.event.WeatherUpdateEvent;
import com.android.fisewatchlauncher.event.WhiteListEvent;
import com.android.fisewatchlauncher.event.WifiStateChangedEvent;
import com.android.fisewatchlauncher.function.DaoFlagUtils;
import com.android.fisewatchlauncher.function.alert.AlertManager;
import com.android.fisewatchlauncher.prenster.dao.CenterSettingsUtils;
import com.android.fisewatchlauncher.prenster.dao.LocationDaoUtils;
import com.android.fisewatchlauncher.prenster.dao.PhoneBookUtils;
import com.android.fisewatchlauncher.utils.FileUtils;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.MediaManager;
import com.android.fisewatchlauncher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.android.fisewatchlauncher.utils.RegexUtils.getMatchsCount;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4 0004
 * @time 00:55
 */
public class StaticManager {
    private StaticManager() {
    }

    private static class SingletonHolder {
        private static final StaticManager INSTANCE = new StaticManager();
    }

    public static StaticManager instance() {
        return SingletonHolder.INSTANCE;
    }

    public void init() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    private int childPageSelectedIndex = -1;

    private int parentPageSelectedIndex = -1;

    public int getChildPageSelectedIndex() {
        return childPageSelectedIndex;
    }

    public void setChildPageSelectedIndex(int childPageSelectedIndex) {
        this.childPageSelectedIndex = childPageSelectedIndex;
    }

    public int getParentPageSelectedIndex() {
        return parentPageSelectedIndex;
    }

    public void setParentPageSelectedIndex(int parentPageSelectedIndex) {
        this.parentPageSelectedIndex = parentPageSelectedIndex;
    }

    private BatteryStatus batteryStatus;

    public BatteryStatus getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(BatteryStatus batteryStatus) {
        this.batteryStatus = batteryStatus;
        EventBus.getDefault().post(batteryStatus);
    }

    private long stepCount;

    public long getStepCount() {
        return stepCount;
    }

    @Subscribe
    public void onEvent(StepUpdateEvent event) {
        LogUtils.i("StepUpdateEvent " + event.toString());
        StepHistory history = event.history;
        if (null != history) {
            this.stepCount = event.history.getStep_count();
        }
    }

    private boolean isPowerConnected;

    public boolean isPowerConnected() {
        return isPowerConnected;
    }

    public void setPowerConnected(boolean powerConnected) {
        isPowerConnected = powerConnected;
    }

    @Subscribe
    public void onEvent(PowerConnection event) {
        LogUtils.i("StepUpdateEvent " + event.toString());
        this.isPowerConnected = event.plug;
    }

    private long turnOverCount = 0;

    public long getTurnOverCount() {
        return turnOverCount;
    }

    public void setTurnOverCount(long turnOverCount) {
        this.turnOverCount = turnOverCount;
    }

    @Subscribe
    public void onEvent(TurnOverEvent event) {
        LogUtils.i("TurnOverEvent " + event.toString());
        this.turnOverCount = event.overCount;
    }

    private int signalStrength;

    public int getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }

    @Subscribe
    public void onEvent(SignalStrengthChangedEvent event) {
        LogUtils.i("SignalStrengthChangedEvent " + event.toString());
        this.signalStrength = event.mSignalStrength;
    }

    private CdmaID cdmaID;

    public CdmaID getCdmaID() {
        return cdmaID;
    }

    public void setCdmaID(CdmaID cdmaID) {
        this.cdmaID = cdmaID;
    }

    @Subscribe
    public void onEvent(CdmaBaseStationEvent event) {
        LogUtils.i("CdmaBaseStationEvent " + event.toString());
        this.cdmaID = event.cdmaID;
    }

    private SCell sCell;//联通移动基站信息

    public SCell getsCell() {
        return sCell;
    }

    public void setsCell(SCell sCell) {
        this.sCell = sCell;
    }

    private int mobileDataActivity;

    public int getMobileDataActivity() {
        return mobileDataActivity;
    }

    public void setMobileDataActivity(int mobileDataActivity) {
        this.mobileDataActivity = mobileDataActivity;
    }

    @Subscribe
    public void onEvent(DataActivityUpdateEvent event) {
        LogUtils.i("DataActivityUpdateEvent " + event.toString());
        this.mobileDataActivity = event.direction;
    }

    private int mobileDataConnectState;

    public int getMobileDataConnectState() {
        return mobileDataConnectState;
    }

    public void setMobileDataConnectState(int mobileDataConnectState) {
        this.mobileDataConnectState = mobileDataConnectState;
    }

    @Subscribe
    public void onEvent(DataConnectionStateChangedEvent event) {
        LogUtils.i("DataConnectionStateChangedEvent " + event.toString());
        this.mobileDataConnectState = event.state;
    }

    private NeiborWifi neiborWifi;
    private long neiborWifiLastTime;//上次扫描的wifi结果

    public NeiborWifi getNeiborWifi() {
        return neiborWifi;
    }

    public void setNeiborWifi(NeiborWifi neiborWifi) {
        this.neiborWifi = neiborWifi;
        this.neiborWifiLastTime = System.currentTimeMillis();
    }

    public long getNeiborWifiLastTime() {
        return neiborWifiLastTime;
    }

    private long curSystemTime;

    public long getCurSystemTime() {
        return curSystemTime;
    }

    public void setCurSystemTime(long curSystemTime) {
        this.curSystemTime = curSystemTime;
    }

    @Subscribe
    public void onEvent(TimeUpdateEvent event) {
        LogUtils.i("TimeUpdateEvent " + event.toString());
        this.curSystemTime = event.curSystemTime;
    }

    private boolean isSmsSosSwitchOn;

    public boolean isSmsSosSwitchOn() {
        return isSmsSosSwitchOn;
    }

    public void setSmsSosSwitchOn(boolean smsSosSwitchOn) {
        isSmsSosSwitchOn = smsSosSwitchOn;
    }

    @Subscribe
    public void onEvent(SwitchSmsSosEvent event) {
        LogUtils.i("SwitchSmsSosEvent " + event.toString());
        this.isSmsSosSwitchOn = event.isSwitchOn;
    }

    @Subscribe
    public void onEvent(SwitchLowBatEvent event) {
        LogUtils.i("SwitchLowBatEvent " + event.toString());
    }

    private boolean isBatteryLow = false;

    public void setBatteryLow(boolean batteryLow) {
        isBatteryLow = batteryLow;
        LogUtils.e(batteryLow ? "低电了！！！！！！！" : "充饱了~~~~~~");
        if (isBatteryLow) {
            MediaManager.findDevice(R.raw.duola, false);
            EventBus.getDefault().post(new BatteryLow());
            boolean isLowBatterySwitchOn = CenterSettingsUtils.instance().queryLowBatterySwitch();
            String centerNum = GlobalSettings.instance().getCenterNum();
            LogUtils.e("低电了 ......");
            LocationManager.instance().sendAlert();//低电就上报信息
            AlertManager.instance().sendBatteryLowSMS();
        } else {
            EventBus.getDefault().post(new BatteryOkay());
        }
    }

    public boolean isBatteryLow() {
        return isBatteryLow;
    }

    private  boolean isInFence;//是否是进围栏状态

    public boolean isInFence() {
        return isInFence;
    }

    public void setInFence(boolean inFence) {
        isInFence = inFence;
    }

    private  boolean isOutFence;//是否是进围栏状态

    public boolean isOutFence() {
        return isOutFence;
    }

    public void setOutFence(boolean outFence) {
        isOutFence = outFence;
    }
    private boolean isTakenOff;//是否是取下状态

    public boolean isTakenOff() {
        return isTakenOff;
    }

    public void setTakenOff(boolean takenOff) {
        isTakenOff = takenOff;
    }
    private boolean isStill;

    public boolean isStill() {
        return isStill;
    }

    public void setStill(boolean still) {
        isStill = still;
    }

    /**
     * 白名单1,2
     **/
    public String whiteListStr1, whiteListStr2;

    public String getWhiteListStr1() {
        return whiteListStr1;
    }

    public void setWhiteListStr1(String whiteListStr1) {
        this.whiteListStr1 = whiteListStr1;
    }

    public String getWhiteListStr2() {
        return whiteListStr2;
    }

    public void setWhiteListStr2(String whiteListStr2) {
        this.whiteListStr2 = whiteListStr2;
    }

    @Subscribe
    public void onEvent(WhiteListEvent event) {//白名单设置1
        LogUtils.i(event.toString());
        WhiteListEvent.Type type = event.type;
        String content = event.whiteListStr;
        if (TextUtils.isEmpty(content)) return;
        int count = getMatchsCount(GlobalSettings.MSG_CONTENT_SEPERATOR, content);
        if (count < 4) {
            LogUtils.e("白名单" + type.toString() + "格式不对 必须五个");
            return;
        }
        StringBuffer sb = new StringBuffer();
        String whiteListStr = null;
        switch (type) {
            case WHITELIST1:
                this.whiteListStr1 = content;
                sb.append(whiteListStr1);
                whiteListStr = sb.toString();
                break;
            case WHITELIST2:
                this.whiteListStr2 = content;
                sb.append(whiteListStr1).append(GlobalSettings.MSG_CONTENT_SEPERATOR).append(whiteListStr2);
                whiteListStr = sb.toString();
                break;
        }
        boolean isSuccess = FileUtils.writeFile(FileConstant.WHITE_LIST_FILE_PATH, whiteListStr);
        LogUtils.i("更新 白名单" + "数据到sdacrd" + DaoFlagUtils.insertSuccessOr(isSuccess));
        PhoneBookUtils.instance().updateWhiteList(whiteListStr);//写到数据库
    }

    public int mNoAnswerCount = 0;
    public boolean isOutgoingConnected = false;

    public int mCurrentOutgoingIndex = 0;
    public int sMaxNoAnswerCount = 2;
    public List<String> outgoingCalls = new ArrayList<>();
    public boolean isSosReady;//是否长按关机键五秒了
    public boolean isSosStart = false;//是否开始紧急呼叫
    private String locationBeanStr;//地理位置上传信息临时保存
    private LocationEvent locationEvent;//地理位置上传信息临时保存

    public LocationEvent getLocationEvent() {
        return locationEvent;
    }

    public void setLocationEvent(LocationEvent event) {
        if (TextUtils.isEmpty(event.locationStr)) {
            return;
        }
        this.locationEvent = event;
    }

    /***
     * 动态获取最新的历史地理上传信息
     * @return
     */
    public String getLocationBeanStr() {
        if (TextUtils.isEmpty(locationBeanStr)) {
            locationBeanStr = LocationDaoUtils.instance().queryLocationStr();
        }
        return locationBeanStr;
    }

    public void setLocationBeanStr(String locationBeanStr) {
        if (!TextUtils.isEmpty(locationBeanStr)) {//临时保存一个有效的地理位置信息
            this.locationBeanStr = locationBeanStr;
        }

    }

    public WifiStateChangedEvent wifiStateChangedEvent;
    public NetworkTypeEvent networkTypeEvent;

    public WeatherUpdateEvent weatherUpdateEvent;

    public SignalStrengthChangedEvent signalStrengthChangedEvent;

    public boolean isInited = false;

    /**
     * 要发送或者已经发送等待回复的对讲信息
     ***/
    private List<TcpMsg> cachedTKSending = Collections.synchronizedList(new ArrayList());

    /***
     * 已经发送失败的对讲消息
     * @param tcpMsg
     */
    public void cacheTKSendingData(TcpMsg tcpMsg) {
        if (!cachedTKSending.contains(tcpMsg)) {
            cachedTKSending.add(tcpMsg);

        } else {
            ToastUtils.showShort("该消息已经在缓存队列了....");
        }
    }

    /***
     * 删除缓存的失败的对讲消息
     * @param tcpMsg
     */
    public void removeCachedSendingData(TcpMsg tcpMsg) {
        cachedTKSending.remove(tcpMsg);
    }

    public TcpMsg getCachedTKSendingById(long id) {
        TcpMsg pollData = null;
        for (TcpMsg msg : cachedTKSending) {
            if (msg.getId() == id) {
                return msg;
            }
        }
        return pollData;
    }

    public void rmCachedTKSendingById(long id) {
        TcpMsg pollData = null;
        for (TcpMsg msg : cachedTKSending) {
            if (msg.getId() == id) {
                pollData = msg;
                break;
            }
        }
        cachedTKSending.remove(pollData);
    }

    /***TCP 是否连上了服务器事件***/
    public TcpServerConnectedEvent tcpServerConnectedEvent;

    public HashMap<String, Integer> homeBages = new HashMap<>();

    public HashMap<String, Integer> getHomeBages() {
        return homeBages;
    }

    public void putHomeBage(String fragmentTitle) {
        homeBages.put(fragmentTitle, getHomeBageCount(fragmentTitle) + 1);
    }

    public int getHomeBageCount(String fragmentTitle) {
        if (!homeBages.containsKey(fragmentTitle)) {
            return 0;
        }
        return homeBages.get(fragmentTitle);
    }

    public boolean getHomeBageVis(String fragmentTitle) {
        if (!homeBages.containsKey(fragmentTitle)) {
            return false;
        }
        return homeBages.get(fragmentTitle) <= 0;
    }

    /**
     * 去掉红点
     *
     * @param fragmentTitle
     */
    public void removeHomeBage(String fragmentTitle) {
        if (homeBages.containsKey(fragmentTitle)) {
            homeBages.remove(fragmentTitle);
        }
    }
}
