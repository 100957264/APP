package com.android.fisewatchlauncher.fragment;

import android.os.Bundle;
import android.view.View;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.acty.TcpclientActivity;
import com.android.fisewatchlauncher.event.TimeZoneUpdateEvent;
import com.android.fisewatchlauncher.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/1
 * @time 17:58
 */
public class ClientTestFragment extends BaseFragment {

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fise_clienttest;
    }

    public static ClientTestFragment newInstance() {
        Bundle args = new Bundle();
        ClientTestFragment fragment = new ClientTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        ButterKnife.bind(this, mRootView);
    }

    @OnClick({R.id.iv_function_client_test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_function_client_test:
                TcpclientActivity.startLocationActivity(getActivity());
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initData(Bundle arguments) {
        super.initData(arguments);
    }

    @Subscribe
    public void onEventMainThread(TimeZoneUpdateEvent event) {
        LogUtils.i("TimeZoneUpdateEvent");
    }
}
