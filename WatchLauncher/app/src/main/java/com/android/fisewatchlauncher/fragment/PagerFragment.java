package com.android.fisewatchlauncher.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.fisewatchlauncher.BuildConfig;
import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.event.HomeBageEvent;
import com.android.fisewatchlauncher.holder.ActivityTarget;
import com.android.fisewatchlauncher.holder.PageHolder;
import com.android.fisewatchlauncher.manager.StaticManager;
import com.android.fisewatchlauncher.thread.HeartBeatThread;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.widget.BadgeView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/1
 * @time 17:58
 */
public class PagerFragment extends BaseFragment {
    private static final String KEY_DATA = "page_holder";
    private PageHolder holder;
    private String title;

    @Bind(R.id.btn_function)
    public ImageButton function;
    @Bind(R.id.tv_function_title)
    public TextView functionName;
    @Bind(R.id.tv_home_bage)
    public BadgeView bage;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fise_function_innerpage;
    }

    public static PagerFragment newInstance(PageHolder holder) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_DATA, holder);
        PagerFragment fragment = new PagerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initData(Bundle arguments) {
        super.initData(arguments);
        PageHolder holder = (PageHolder) arguments.getSerializable(KEY_DATA);
        if (null != holder) {
            PagerFragment.this.holder = holder;
        } else {
            LogUtils.e("PagerFragment arguments = null");
        }
    }

    @Override
    protected void initView() {
        super.initView();
        ButterKnife.bind(this, mRootView);
        if (null != holder) {
            if ("BLUE_FOX".equals(BuildConfig.FLAVOR)) {
                functionName.setVisibility(View.GONE);

            } else {
                functionName.setText(holder.getPageTitle());
            }
            title = getResources().getString(holder.getPageTitle());
            functionName.setText(title);
            int iconId = holder.getIconId(), bgId = holder.getBgId();
            if (0 != iconId) {
                function.setImageResource(iconId);
            }
            function.setFocusable(true);
            function.setFocusableInTouchMode(true);
            function.requestFocus();
//            LogUtils.e(holder.toString());
            if (0 != bgId) {
                mRootView.setBackground(getActivity().getDrawable(bgId));
            }
            updateBageView(title);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        HeartBeatThread.instance().queryBondState();//心跳查询绑定
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick({R.id.btn_function})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_function:
                ActivityTarget target = holder.getTarget();
                startTargetActivity(target);
                break;
        }
    }
    public void startTargetActivity(ActivityTarget target){
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(target.getTargetAction())) {
            LogUtils.d("target.getTargetAction()==" + target.getTargetAction());
            if(target.getTargetAction().contains("action")){
                intent.setAction(target.getTargetAction());
            }else {
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(target.getTargetAction());
            }
        }
        if (!TextUtils.isEmpty(target.getTargetPackage()) && !TextUtils.isEmpty(target.getTargetActivityName())) {
            ComponentName name = new ComponentName(target.getTargetPackage(), target.getTargetActivityName());
            intent.setComponent(name);
        }
        if (target.getFlag() != 0) {
            intent.setFlags(target.getFlag());
        }
        if (null != intent) {
            try {
                if (intent.toString().contains("fise.action.ACTION_TOGGLE_RECENTS")) {
                    LogUtils.d("start recents activity");
                    KApplication.sContext.sendBroadcast(intent);
                } else {
                    LogUtils.d("start ...intent=" + intent);
                    startActivity(intent);
                }
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBageChangedEvent(HomeBageEvent event) {

        updateBageView(event.pagerTitle);
    }

    private void updateBageView(String pageTitle) {
        if (null == holder) return;
//        long cur = System.currentTimeMillis();
//        bage = (BadgeView) mRootView.findViewById(R.id.tv_home_bage);
        int bageCount = StaticManager.instance().getHomeBageCount(pageTitle);
        bage.setBadgeCount(bageCount);
        boolean bageVis = StaticManager.instance().getHomeBageVis(pageTitle);
        LogUtils.e(pageTitle + " ,bageVis " + bageVis + " ,count " + bageCount);
        bage.setVisibility(bageVis ? View.VISIBLE : View.GONE);
//        LogUtils.e("bage耗时 "+ (System.currentTimeMillis() - cur));
    }
}
