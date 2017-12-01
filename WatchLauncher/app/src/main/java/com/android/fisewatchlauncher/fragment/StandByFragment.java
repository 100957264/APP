package com.android.fisewatchlauncher.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.android.fisewatchlauncher.BuildConfig;
import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.acty.SelectBlueFoxBackgroundActivity;
import com.android.fisewatchlauncher.client.msg.MsgType;
import com.android.fisewatchlauncher.entity.weather.Weather;
import com.android.fisewatchlauncher.event.AlarmStateEvent;
import com.android.fisewatchlauncher.event.BatteryLow;
import com.android.fisewatchlauncher.event.BatteryOkay;
import com.android.fisewatchlauncher.event.BatteryStatus;
import com.android.fisewatchlauncher.event.BindUnBindEvent;
import com.android.fisewatchlauncher.event.DataConnectionStateChangedEvent;
import com.android.fisewatchlauncher.event.NetworkTypeEvent;
import com.android.fisewatchlauncher.event.SignalStrengthChangedEvent;
import com.android.fisewatchlauncher.event.TcpServerConnectedEvent;
import com.android.fisewatchlauncher.event.TimeUpdateEvent;
import com.android.fisewatchlauncher.event.TimeZoneUpdateEvent;
import com.android.fisewatchlauncher.event.VolteStatusEvent;
import com.android.fisewatchlauncher.event.WeatherUpdateEvent;
import com.android.fisewatchlauncher.event.WifiStateChangedEvent;
import com.android.fisewatchlauncher.function.weather.WeatherManager;
import com.android.fisewatchlauncher.manager.LocationManager;
import com.android.fisewatchlauncher.manager.NetorkManager;
import com.android.fisewatchlauncher.manager.PreferControler;
import com.android.fisewatchlauncher.manager.StaticManager;
import com.android.fisewatchlauncher.parser.MsgParser;
import com.android.fisewatchlauncher.utils.ActivityUtils;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/1
 * @time 17:58
 */
public class StandByFragment extends BaseFragment {
    private static final String KEY_CURENT_TIME_MILLIS = "key_curent_time_millis";
    private static final String KEY_WEATHER_STATUS = "key_weather_status";
    private static final String KEY_TEMPERATURE = "key_temperature";
    View standBy;
    @Bind(R.id.fise_clock_time)
    TextClock mTime;
    @Bind(R.id.fise_clock_date)
    TextClock mDate;
    @Bind(R.id.fise_clock_week)
    TextClock mWeek;

    @Bind(R.id.fise_weather_temperature)
    TextView mCurTempartue;

    @Bind(R.id.iv_sysiui_bluetooth)
    ImageView mBluetoothState;
    @Bind(R.id.iv_sysiui_battery)
    ImageView mBatteryView;
    @Bind(R.id.iv_sysiui_battery_charging)
    ImageView mBatteryCharging;

    @Bind(R.id.fise_weather_pic)
    ImageView mWeatherPic;

    ClipDrawable mClipDrawable;
    LayerDrawable mLayerDrawable;

    private WeatherManager weatherManager;
    private GestureDetector detector;

    @Bind(R.id.iv_sysiui_signal)
    ImageView      mSignalView;
    @Bind(R.id.tv_sysiui_signal)
    TextView       mSignalText;
    @Bind(R.id.iv_sysiui_wifi)
    ImageView      mWifiView;
    @Bind(R.id.iv_sysiui_clock)
    ImageView      mClockView;
    @Bind(R.id.iv_mobile_traffic_down)
    ImageView      mMobileDownView;
    @Bind(R.id.iv_sysiui_volte_network_hd)
    ImageView      mVolteNetwork;
    @Bind(R.id.sysiui_signal_container)
    RelativeLayout mSysiuiSignalContainer;
    public BroadcastReceiver mReceiver;


    @Override
    protected int setLayoutResouceId() {
        if ("BLUE_FOX".equals(BuildConfig.FLAVOR)) {
            return R.layout.blue_fox_fise_standby;
        }
        return R.layout.fise_standby;
    }

    public static StandByFragment newInstance() {
        Bundle args = new Bundle();
        StandByFragment fragment = new StandByFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        standBy = mRootView;
        if ("BLUE_FOX".equals(BuildConfig.FLAVOR)) {
            standBy.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ActivityUtils.startActivity(SelectBlueFoxBackgroundActivity.class);
                    return true;
                }
            });
        }
        ButterKnife.bind(this, standBy);
        PreferControler controler = PreferControler.instance();
        String weather = controler.getWeather();
        mLayerDrawable = (LayerDrawable) mBatteryView.getDrawable();
        mClipDrawable = (ClipDrawable) mLayerDrawable.findDrawableByLayerId(R.id.clip_drawable);
        NetorkManager.instance().registPhoneStateListener();
        MsgParser.instance().sendMsgByType(MsgType.Time);
        MsgParser.instance().sendMsgByType(MsgType.LGZONE);
        initBluetoothIcon();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        long lastTime = PreferencesUtils.getLong(getContext(), KEY_CURENT_TIME_MILLIS);

        if (System.currentTimeMillis() - lastTime > 2 * 60 * 60 * 1000 || mCurTempartue.getVisibility() != View.VISIBLE) {
            LocationManager.instance().requestWeather();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(mReceiver);
        ButterKnife.unbind(this);
    }

    @Override
    protected void initData(Bundle arguments) {
        super.initData(arguments);
    }

    @Override
    protected void onLazyLoad() {//第一次进来所有状态都是最新的
        super.onLazyLoad();
        WifiStateChangedEvent wifiStateChangedEvent = StaticManager.instance().wifiStateChangedEvent;
        if (null != wifiStateChangedEvent) {
            setWifiIcon(wifiStateChangedEvent);
        }
        NetworkTypeEvent networkTypeEvent = StaticManager.instance().networkTypeEvent;
        if (networkTypeEvent != null) {
            setNetworkType(networkTypeEvent);
        }
        updateBattery(StaticManager.instance().getBatteryStatus());
        updateWeather(StaticManager.instance().weatherUpdateEvent);
        updateServerConnectedState(StaticManager.instance().tcpServerConnectedEvent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWeatherUpdate(WeatherUpdateEvent event) {
        updateWeather(event);
    }

    private void updateWeather(WeatherUpdateEvent event) {
        if (null == event) return;
        LogUtils.i(event.toString());
        Weather weather = event.weather;
        double curTem = weather.curTemperature;
        String tempature = getResources().getString(R.string.unit_standby_weather).replace("%", String.valueOf(curTem));
        mCurTempartue.setText(tempature);
        mCurTempartue.setVisibility(View.VISIBLE);
        int weatherCode = weather.getWeatherCode();
        setWeatherState(weatherCode);
        PreferencesUtils.putLong(getContext(), KEY_CURENT_TIME_MILLIS, System.currentTimeMillis());

    }

    private void setWeatherState(int weatherCode) {
        // 天气编号：0——晴 1——阴 2——雨 3——雪
        int weatherStateImageSrc = R.drawable.ic_weather_unknow;
        switch (weatherCode) {
            case 0:
                weatherStateImageSrc = R.drawable.ic_weather_sun;
                break;
            case 1:
                weatherStateImageSrc = R.drawable.ic_weather_cloudy;
                break;
            case 2:
                weatherStateImageSrc = R.drawable.ic_weather_rain;
                break;
            case 3:
                weatherStateImageSrc = R.drawable.ic_weather_snow;
                break;
        }
        mWeatherPic.setImageResource(weatherStateImageSrc);
    }

    @Subscribe
    public void onEventMainThread(TimeZoneUpdateEvent event) {
        LogUtils.i("TimeZoneUpdateEvent");
//        mTime.setTimeZone(event.timeZone);
//        mDate.setTimeZone(event.timeZone);
//        mWeek.setTimeZone(event.timeZone);
    }

    @Subscribe
    public void onEventMainThread(TimeUpdateEvent event) {
        LogUtils.i("TimeUpdateEvent");
//        mTime.setText(event.getTime());
//        mDate.setText(event.getDate());
//        mWeek.set(event.getWeekDay());
    }


    @Subscribe
    public void onEventMainThread(DataConnectionStateChangedEvent event) {
        LogUtils.i("TimeZoneUpdateEvent");
        if (isVisible()) {

        }
    }

    @Subscribe
    public void onEventMainThread(BindUnBindEvent event) {
        LogUtils.i("BindUnBindEvent " + event.toString());
        boolean isBond = event.deviceBond;
        if (isBond) {

        } else {
            // show unBond
        }
    }

    @Subscribe
    public void onEventMainThread(BatteryLow event) {
        LogUtils.i("BatteryLow " + event.toString());

    }

    @Subscribe
    public void onEvent(BatteryStatus event) {
        updateBattery(event);
    }

    private void updateBattery(BatteryStatus event) {
        if (null == event) {
            return;
        }
        if(event.level <=15){
            mBatteryView.setImageResource(R.drawable.battery_low_15);
        }else if(event.level > 15 && event.level <= 40){
            mBatteryView.setImageResource(R.drawable.battery_low_40);
        }else {
            mBatteryView.setImageResource(R.drawable.battery);
        }
        mLayerDrawable = (LayerDrawable) mBatteryView.getDrawable();
        mClipDrawable = (ClipDrawable) mLayerDrawable.findDrawableByLayerId(R.id.clip_drawable);
        mClipDrawable.setLevel((event.level * 100 / event.scale) * 100);
        if (event.status == BatteryManager.BATTERY_STATUS_CHARGING) {
            mBatteryCharging.setVisibility(View.VISIBLE);
        } else {
            mBatteryCharging.setVisibility(View.INVISIBLE);
        }
        LogUtils.i("BatteryStatus " + event.toString());
    }

    @Subscribe
    public void onEvent(BatteryOkay event) {
        LogUtils.i("BatteryOkay " + event.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSignalUpdate(SignalStrengthChangedEvent event) {
        if (NetorkManager.instance().hasInsertSIM()) {
            mSignalView.setImageResource(NetorkManager.SIGNAL_ICON[event.mSignalStrength]);
            LogUtils.i("SignalDebug: mSignalStrength =" + event.mSignalStrength);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkTypeUpdate(NetworkTypeEvent event) {

        setNetworkType(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWifiStateChanged(WifiStateChangedEvent event) {
        setWifiIcon(event);
        LogUtils.i("WifiDebug: state= " + event.state + ",connected=" + event.connected + ",wifiStrength =" + event.wifiStrength);
    }

    private void setNetworkType(NetworkTypeEvent event) {
        LogUtils.i("fengqing: setNetworkType : type =" + event.networkType);
        boolean isSimAbsent = event.isSimAbsent;
        if (!isSimAbsent || NetorkManager.instance().hasInsertSIM()) {
            String networkType = NetorkManager.instance().getNetworkType(event.networkType);
            if (!networkType.isEmpty()) {
                mSignalText.setText(networkType);
            } else {
                mSignalText.setText("未知运营商");
            }
        } else {
            mSignalText.setText("未插卡");
            mSignalView.setImageResource(NetorkManager.SIGNAL_ICON[0]);
        }
    }

    private void setWifiIcon(WifiStateChangedEvent event) {
        if (event.state == 1) {
            mWifiView.setVisibility(View.INVISIBLE);
        } else if (event.connected) {
            mWifiView.setVisibility(View.VISIBLE);
            mWifiView.setImageResource(NetorkManager.WIFI_ICON[event.wifiStrength]);
        } else {
            mWifiView.setVisibility(View.VISIBLE);
            mWifiView.setImageResource(NetorkManager.WIFI_ICON[event.wifiStrength]);
        }
    }

    private void initBluetoothIcon() {
        BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothState.setVisibility(blueadapter.isEnabled() ? View.VISIBLE : View.GONE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case BluetoothAdapter.ACTION_STATE_CHANGED:
                        int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                        switch (blueState) {
                            case BluetoothAdapter.STATE_TURNING_ON:
                            case BluetoothAdapter.STATE_ON:
                                mBluetoothState.setVisibility(View.VISIBLE);
                                break;
                            case BluetoothAdapter.STATE_TURNING_OFF:
                            case BluetoothAdapter.STATE_OFF:
                                mBluetoothState.setVisibility(View.GONE);
                                break;
                        }
                        break;
                }
            }
        };
        getContext().registerReceiver(mReceiver, filter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVolteStateChanged(VolteStatusEvent event) {
        mVolteNetwork.setVisibility(event.volteStatus == 0 ? View.VISIBLE : View.GONE);
        LogUtils.d("onVolteStateChanged event.volteStatus =" + event.volteStatus);
    }

    private void updateServerConnectedState(TcpServerConnectedEvent event) {
        if (null != event) {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTcpServerConnectedStateChanged(TcpServerConnectedEvent event) {
        updateServerConnectedState(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN ,sticky = true)
    public void onAlarmStateChanged(AlarmStateEvent event) {
        mClockView.setVisibility(event.isHasAlarm()?View.VISIBLE : View.GONE);
        EventBus.getDefault().removeStickyEvent(event);
    }
}
