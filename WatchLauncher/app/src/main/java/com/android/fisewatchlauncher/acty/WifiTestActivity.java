package com.android.fisewatchlauncher.acty;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.entity.wifi.WifiSearcher;
import com.android.fisewatchlauncher.entity.wifi.WifiSyncBean;
import com.android.fisewatchlauncher.function.wifi.WifiConnectUtil;
import com.android.fisewatchlauncher.function.wifi.WifiSyncManager;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

/**
 * @author mare
 * @Description:TODO Wifi测试界面
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/5
 * @time 16:59
 */
public class WifiTestActivity extends BaseActivity implements RecyclerArrayAdapter.OnItemClickListener, WifiSearcher.SearchWifiListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

    }

    public void scanWifi(View v) {
        WifiSearcher.instance().init(this);
    }

    public void linkWifi(View v) {
        WifiSyncBean bean = new WifiSyncBean("9D_3F", "12345678");
        WifiSyncManager.instance().startSync(bean);

    }

    public void forgetWifi(View v) {
        try {
            WifiConnectUtil.instance().forgetWifiPassword("9D_3F");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("forgetWifi " +e);
        }
    }

    @Override
    public void onSearchWifiFailed(WifiSearcher.ErrorType errorType) {
        LogUtils.e("搜索wifi失败.....");
    }

    @Override
    public void onSearchWifiSuccess(List<ScanResult> results) {
        LogUtils.e("搜索wifi成功.... " + results.toString());
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    protected void onDestroy() {
        WifiSearcher.instance().stopSearch(this);//解除绑定
        super.onDestroy();
    }
}
