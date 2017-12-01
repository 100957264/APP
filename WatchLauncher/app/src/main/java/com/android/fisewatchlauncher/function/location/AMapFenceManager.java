package com.android.fisewatchlauncher.function.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.DPoint;
import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mare
 * @Description:TODO 高德电子栅栏管理者
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/19
 * @time 15:11
 */
public class AMapFenceManager implements GeoFenceListener {
    private AMapFenceManager() {
    }

    private static class SingletonHolder {
        private static final AMapFenceManager INSTANCE = new AMapFenceManager();
    }

    public static AMapFenceManager instance() {
        return SingletonHolder.INSTANCE;
    }

    private GeoFenceClient fenceClient = null;// 地理围栏客户端
    private static final String GEOFENCE_BROADCAST_ACTION = "com.example.geofence.round";// 地理围栏的广播action
    // 触发地理围栏的行为，默认为进入提醒
    private int activatesAction = GeoFenceClient.GEOFENCE_IN;
    /*当前已经添加的围栏集合*/
    List<GeoFence> fenceList = new ArrayList<>();

    public void init() {
        IntentFilter filter = new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
        KApplication.sContext.registerReceiver(mGeoFenceReceiver, filter);
        if (null == fenceClient) {
            fenceClient = new GeoFenceClient(KApplication.sContext);
        }
        fenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        fenceClient.setGeoFenceListener(this);
        /*设置地理围栏的触发行为,默认为进入*/
        fenceClient.setActivateAction(GeoFenceClient.GEOFENCE_IN | GeoFenceClient.GEOFENCE_OUT | GeoFenceClient.GEOFENCE_STAYED);
    }

    /**
     * 接收触发围栏后的广播,当添加围栏成功之后，会立即对所有围栏状态进行一次侦测，如果当前状态与用户设置的触发行为相符将会立即触发一次围栏广播；
     * 只有当触发围栏之后才会收到广播,对于同一触发行为只会发送一次广播不会重复发送，除非位置和围栏的关系再次发生了改变。
     */
    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                Bundle bundle = intent.getExtras();
                String customId = bundle
                        .getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                String fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID);
                //status标识的是当前的围栏状态，不是围栏行为
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                StringBuffer sb = new StringBuffer();
                switch (status) {
                    case GeoFence.STATUS_LOCFAIL:
                        sb.append("定位失败");
                        break;
                    case GeoFence.STATUS_IN:
                        sb.append("进入围栏 ");
                        break;
                    case GeoFence.STATUS_OUT:
                        sb.append("离开围栏 ");
                        break;
                    case GeoFence.STATUS_STAYED:
                        sb.append("停留在围栏内 ");
                        break;
                    default:
                        break;
                }
                if (status != GeoFence.STATUS_LOCFAIL) {
                    if (!TextUtils.isEmpty(customId)) {
                        sb.append(" customId: " + customId);
                    }
                    sb.append(" fenceId: " + fenceId);
                }
                String str = sb.toString();
            }
        }
    };

    @Override
    public void onGeoFenceCreateFinished(List<GeoFence> geoFenceList, int errorCode, String fenceId) {
        if (errorCode == GeoFence.ADDGEOFENCE_SUCCESS) {
            LogUtils.i("添加围栏成功... 栅栏ID: " + fenceId);
            fenceList = geoFenceList;
        } else {
            LogUtils.i("添加围栏失败... 栅栏ID: " + fenceId);
        }
    }

    /**
     * 添加服务器端传过来的栅栏
     *
     * @param dPoint
     * @param radius
     * @param customId
     */
    public void addRoundFence(DPoint dPoint, float radius, String customId) {
        if (null == fenceClient) {
            LogUtils.e("高德栅栏还没初始化····");
            return;
        }
        fenceClient.addGeoFence(dPoint, radius, customId);
    }

    /**
     * 删除服务器传过来的栅栏
     *
     * @param customId
     */
    public void removeRoundFence(String customId) {
        if (null == fenceClient) {
            LogUtils.e("高德栅栏还没初始化····");
            return;
        }
        List<GeoFence> allFences = fenceClient.getAllGeoFence();
        for (GeoFence fence : allFences) {
            if (null != fence) {
                if (TextUtils.equals(fence.getCustomId(), customId)) {
                    fenceClient.removeGeoFence(fence);
                    LogUtils.e("已经删掉了栅栏 customId: " + customId);
                    break;
                }
            }
        }
    }


}
