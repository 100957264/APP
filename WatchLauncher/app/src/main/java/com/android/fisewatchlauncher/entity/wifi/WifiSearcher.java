package com.android.fisewatchlauncher.entity.wifi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mare
 * @Description:TODO wifi扫描管理者
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4
 * @time 23:44
 */
public class WifiSearcher {
    private static final int WIFI_SEARCH_TIMEOUT = 20; //扫描WIFI的超时时间
    private Context mContext;
    private WifiManager mWifiManager;
    private WiFiScanReceiver mWifiReceiver;
    private Lock mLock;
    private Condition mCondition;
    Vector<SearchWifiListener> mSearchWifiListeners = new Vector<>();
    private boolean mIsWifiScanCompleted = false;
    private boolean mIsWifiSearching = false;
    private Object mVectorLock = new Object();

    public static enum ErrorType {
        SEARCH_WIFI_TIMEOUT, //扫描WIFI超时（一直搜不到结果）
        NO_WIFI_FOUND       //扫描WIFI结束，没有找到任何WIFI信号
    }

    //扫描结果通过该接口返回给Caller
    public interface SearchWifiListener {
        void onSearchWifiFailed(ErrorType errorType);

        void onSearchWifiSuccess(List<ScanResult> results);
    }

    private WifiSearcher() {
        mContext = KApplication.sContext;
        mLock = new ReentrantLock();
        mCondition = mLock.newCondition();
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mWifiReceiver = new WiFiScanReceiver();
    }

    private static class SingletonHolder {
        private static final WifiSearcher INSTANCE = new WifiSearcher();
    }

    public static WifiSearcher instance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(SearchWifiListener listener) {
        if (!mSearchWifiListeners.contains(listener)) {
            mSearchWifiListeners.add(listener);
        }
        search();
    }

    public void stopSearch(SearchWifiListener listener) {
        mIsWifiSearching = false;
        mSearchWifiListeners.remove(listener);
    }

    private void search() {
        if (mIsWifiSearching) {
            LogUtils.e("正在扫描wifi....");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mIsWifiSearching = true;
                //如果WIFI没有打开，则打开WIFI
                if (!mWifiManager.isWifiEnabled()) {
                    mWifiManager.setWifiEnabled(true);
                }
                //注册接收WIFI扫描结果的监听类对象
                mContext.registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                //开始扫描
                mWifiManager.startScan();
                mLock.lock();
                //阻塞等待扫描结果
                try {
                    mIsWifiScanCompleted = false;
                    mCondition.await(WIFI_SEARCH_TIMEOUT, TimeUnit.SECONDS);
                    if (!mIsWifiScanCompleted) {
                        notifySearchWifiFailed(ErrorType.SEARCH_WIFI_TIMEOUT);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mLock.unlock();
                //删除注册的监听类对象
                mContext.unregisterReceiver(mWifiReceiver);
                mIsWifiSearching = false;
            }
        }).start();
    }

    //系统WIFI扫描结果消息的接收者
    protected class WiFiScanReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent) {
            //提取扫描结果
            List<ScanResult> ssidResults = new ArrayList<>();
            List<ScanResult> scanResults = mWifiManager.getScanResults();
            sortByLevel(scanResults);
            int maxLen = Math.min(5, scanResults.size());
            for (int i = 0; i < maxLen; i++) {
                ssidResults.add(scanResults.get(i));
            }
            //检测扫描结果
            if (ssidResults.isEmpty()) {
                notifySearchWifiFailed(ErrorType.NO_WIFI_FOUND);
            } else {
                notifySearchWifiSuccess(ssidResults);
            }
            mLock.lock();
            mIsWifiScanCompleted = true;
            mCondition.signalAll();
            mLock.unlock();
        }
    }

    /**
     * 信号强度从强到时弱进行排序
     *
     * @param list 存放周围wifi热点对象的列表
     */
    private static void sortByLevel(List<ScanResult> list) {

        Collections.sort(list, new Comparator<ScanResult>() {

            @Override
            public int compare(ScanResult lhs, ScanResult rhs) {
                return rhs.level - lhs.level;
            }
        });
    }

    private void notifySearchWifiFailed(ErrorType type) {
        synchronized (mVectorLock){
            for (SearchWifiListener listener : mSearchWifiListeners) {
                if (null != listener) {
                    listener.onSearchWifiFailed(type);
                }
            }
        }
    }

    private void notifySearchWifiSuccess(List<ScanResult> ssidResults) {
        synchronized (mVectorLock){
            for (SearchWifiListener listener : mSearchWifiListeners) {
                if (null != listener) {
                    listener.onSearchWifiSuccess(ssidResults);
                }
            }
        }
    }

}