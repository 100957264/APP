package com.android.fisewatchlauncher.acty;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.fisewatchlauncher.BuildConfig;
import com.android.fisewatchlauncher.FlavorDiff;
import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.adapter.CommonFragmentPagerAdapter;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.event.DisturbSwitchEvent;
import com.android.fisewatchlauncher.event.ScreenOffEvent;
import com.android.fisewatchlauncher.event.UpdateChildPageIndexEvent;
import com.android.fisewatchlauncher.fragment.InnerPageFragment;
import com.android.fisewatchlauncher.fragment.PagerFragment;
import com.android.fisewatchlauncher.function.alarm.RemindUtils;
import com.android.fisewatchlauncher.function.alert.AlertManager;
import com.android.fisewatchlauncher.function.view.ViewManager;
import com.android.fisewatchlauncher.holder.ActivityTarget;
import com.android.fisewatchlauncher.manager.DBManager;
import com.android.fisewatchlauncher.manager.LocationManager;
import com.android.fisewatchlauncher.manager.NetManager;
import com.android.fisewatchlauncher.manager.StaticManager;
import com.android.fisewatchlauncher.net.subscriber.IPSubscribe;
import com.android.fisewatchlauncher.service.FiseService;
import com.android.fisewatchlauncher.utils.CrashHandler;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.PhoneUtils;
import com.android.fisewatchlauncher.utils.SilenceTimeUtils;
import com.android.fisewatchlauncher.utils.ToastUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CHANGE_NETWORK_STATE;
import static android.Manifest.permission.PROCESS_OUTGOING_CALLS;
import static android.Manifest.permission.RECEIVE_MMS;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FiseLauncherActivity extends BaseActivity implements IPSubscribe.IPCallBack {
    private ViewPager mOuterViewPager;
    private CommonFragmentPagerAdapter mOuterPageAdapter;
    public static Context mContext;
    private boolean isFiseLauncherRunning = true;

    private ViewManager mViewManager;

    private int mCurrentPageIndex = -1;

    private static final String[] PERMISSION = new String[]{
            Manifest.permission.READ_PHONE_STATE, WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, SEND_SMS, RECEIVE_MMS,
            PROCESS_OUTGOING_CALLS, CALL_PHONE, CHANGE_NETWORK_STATE,
            ACCESS_NETWORK_STATE, Manifest.permission.RECORD_AUDIO
    };

    private List<Fragment> mOuterPages = new ArrayList<>();
    public View mViewCover;

    Handler handler = new Handler();

    private void getPermission(final Context ctx) {
        RxPermissions.getInstance(this)
                .request(PERMISSION[0], PERMISSION[1], PERMISSION[2], PERMISSION[3], PERMISSION[4],
                        PERMISSION[5], PERMISSION[6], PERMISSION[7], PERMISSION[8], PERMISSION[9], PERMISSION[10], PERMISSION[11])
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            ((KApplication) getApplicationContext()).initLocationSDK();//初始化地图
                            CrashHandler.instance().init();
                            // 已经获取权限
                            DBManager.instance().initDao();//要获取读写权限
                            GlobalSettings.instance().saveImei(ctx);
                            NetManager.instance().init(FiseLauncherActivity.this);//注册电话状态监听
                            StaticManager.instance().init();//存储所有状态
                            StaticManager.instance().isInited = true;
                            RemindUtils.setNearAlarm();//恢复闹钟
                            SilenceTimeUtils.initSilenceTime(null, SilenceTimeUtils.getSilenceTimeContent()); //恢复勿扰时段
                            FiseService.pull(ctx);
                            AlertManager.instance().init(handler);//初始化预警管理
                        } else {
                            // 未获取权限
                            Toast.makeText(ctx, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_fise_launcher);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        boolean hasCard = PhoneUtils.hasIccCard(tm);
        String hasCardStr = hasCard ? "已" : "未";
        ToastUtils.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 60);
        ToastUtils.showShort("当前" + hasCardStr + "插卡");
        if (!BuildConfig.FLAVOR.equals("FISE_WSD")) {
            getPermission(this);
        }
        initView();
        mContext = this;
    }

    private void initView() {
        initCover();
        mViewManager = ViewManager.instance();
        mViewManager.init();
        mOuterPages = mViewManager.getOuterFragments();
        initOuter();
        EventBus.builder().throwSubscriberException(BuildConfig.DEBUG);
        EventBus.getDefault().register(this);
        if (null != mViewManager) {
            mViewManager.onStart();
        }
    }

    private void initData() {
        FiseService.pull(this);
    }

    private void initOuter() {
        mOuterViewPager = (ViewPager) findViewById(R.id.outer_view_page);
        mOuterPageAdapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), mOuterPages);
        mOuterViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPageIndex = position - 1;
                LogUtils.d("onPageSelected outer " + mCurrentPageIndex);
                if (position < 0) {
                    StaticManager.instance().setChildPageSelectedIndex(0);
                }
                StaticManager.instance().setParentPageSelectedIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mOuterViewPager.setAdapter(mOuterPageAdapter);
        ((InnerPageFragment) mOuterPages.get(1)).setOnInnerPagerBackBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOuterViewPager.setCurrentItem(0, true);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        getPermission(this);
        LocationManager.instance().stopLocate();
        LogUtils.e("onRestartonRestartonRestart");
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        FiseService.NEED_SHOW_ANALOG_CLOCK = true;
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        isFiseLauncherRunning = true;
        initData();
        if ("BLUE_FOX" == BuildConfig.FLAVOR) {
            mOuterViewPager.setBackground(getResources().getDrawable(FlavorDiff.getBlueFoxBackground()));
        } else if (("FISE_WSD" == BuildConfig.FLAVOR)) {
            mOuterViewPager.setBackground(null);
        } else {
            mOuterViewPager.setBackgroundColor(Color.BLACK);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        StaticManager staticManager = StaticManager.instance();
        int childIndex = staticManager.getChildPageSelectedIndex();
        int outIndex = staticManager.getParentPageSelectedIndex();
        LogUtils.d("keydebug: keycode =" + keyCode + ",childIndex =" + childIndex + ",outIndex =" + outIndex);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mCurrentPageIndex >= 0) {
                EventBus.getDefault().post(new UpdateChildPageIndexEvent(0, true));
                mOuterViewPager.setCurrentItem(0, true);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            if (childIndex > -1) {
                ActivityTarget[] targets = FlavorDiff.getActivityTargets();
                Fragment fragment = ViewManager.instance().getInnerFragments().get(childIndex);
                LogUtils.d("keydebug: fragment =" + fragment.getClass().getSimpleName());
                if (fragment instanceof PagerFragment) {
                    PagerFragment currentFragment = (PagerFragment) fragment;
                    if (outIndex > -1) {
                        currentFragment.startTargetActivity(targets[childIndex]);
                    }
                }
            }
            return true;
        }
        boolean handled = onNumberKeyDown(keyCode, event);
        LogUtils.d("handled" + handled);
        return super.onKeyDown(keyCode, event);
    }

    public void startDialpad(int keyCode) {
        Intent intent = new Intent("android.intent.action.DIAL");
        intent.setData(Uri.fromParts("tel", String.valueOf((keyCode -KEY_BASE)), null));
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFiseLauncherRunning = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //do nothing
    }

    @Override
    public void getIP(String ip) {

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroyonDestroy");
        StaticManager.instance().onDestroy();
        EventBus.getDefault().unregister(this);
        AlertManager.instance().onDestroy();
        if (null != mViewManager) {
            mViewManager.onStop();
        }
    }

    @Subscribe
    public void onEventMainThread(ScreenOffEvent event) {
        LogUtils.i("ScreenOffEvent");
        mOuterViewPager.setCurrentItem(0, false);
        mCurrentPageIndex = -1;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(SilenceTimeUtils.IS_SILENCE_TIME)) {
            boolean isSilenceTime = intent.getBooleanExtra(SilenceTimeUtils.IS_SILENCE_TIME, false);
            if (isSilenceTime) {
                mViewCover.setVisibility(View.VISIBLE);
                EventBus.getDefault().post(new UpdateChildPageIndexEvent(0, true));
                mOuterViewPager.setCurrentItem(0, true);
            } else {
                mViewCover.setVisibility(View.GONE);
            }
            Log.d("chenjp", "onNewIntent | Silence Time :" + isSilenceTime);
        }
    }

    private void initCover() {
        mViewCover = findViewById(R.id.view_cover);
        mViewCover.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Toast.makeText(KApplication.sContext, "禁用时段", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onDisturbSwitch(DisturbSwitchEvent event) {
        LogUtils.i("DisturbSwitchEvent " + event.toString());
        final boolean inTimeScope = event.inTimeScope;
        ToastUtils.showShort(inTimeScope ? "启动勿扰" : "非勿扰时段");
        if (inTimeScope) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            home.putExtra(SilenceTimeUtils.IS_SILENCE_TIME, inTimeScope);
            startActivity(home);
        } else {
            if (StaticManager.instance().isInited) {
                mViewCover.setVisibility(View.GONE);
            }
        }
        EventBus.getDefault().removeStickyEvent(event);
    }

    public final int KEY_BASE = 7;
    public boolean onNumberKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                startDialpad(keyCode);
                return true;
            case KeyEvent.KEYCODE_1:
                startDialpad(keyCode);
                return true;
            case KeyEvent.KEYCODE_2:
                startDialpad(keyCode);
                return true;
            case KeyEvent.KEYCODE_3:
                startDialpad(keyCode);
                return true;
            case KeyEvent.KEYCODE_4:
                startDialpad(keyCode);
                return true;
            case KeyEvent.KEYCODE_5:
                startDialpad(keyCode);
                return true;
            case KeyEvent.KEYCODE_6:
                startDialpad(keyCode);
                return true;
            case KeyEvent.KEYCODE_7:
                startDialpad(keyCode);
                return true;
            case KeyEvent.KEYCODE_8:
                startDialpad(keyCode);
                return true;
            case KeyEvent.KEYCODE_9:
                startDialpad(keyCode);
                return true;
            default:
                break;
        }
        return false;
    }


}
