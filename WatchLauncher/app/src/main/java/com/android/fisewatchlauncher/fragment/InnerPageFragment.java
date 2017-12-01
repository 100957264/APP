package com.android.fisewatchlauncher.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.adapter.InnerPagerAdapter;
import com.android.fisewatchlauncher.event.BondStateChangedEvent;
import com.android.fisewatchlauncher.event.UpdateChildPageIndexEvent;
import com.android.fisewatchlauncher.function.view.ViewManager;
import com.android.fisewatchlauncher.manager.StaticManager;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.widget.CirclePageIndicator;
import com.android.fisewatchlauncher.widget.inner.InnerPagerSelectedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4 0004
 * @time 00:37
 */

public class InnerPageFragment extends BaseFragment implements InnerPagerSelectedListener {

    private InnerPagerAdapter mInnerPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> innerFragments = new ArrayList<>();
    private CirclePageIndicator mPageIndicator;
    private int mInnerPagerSelecteIndex = 0;
    private View.OnClickListener onInnerPagerBackBtnClickListener;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_fise_launcher_inner;
    }

    public static InnerPageFragment newInstance() {
        Bundle args = new Bundle();
        InnerPageFragment fragment = new InnerPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();

        if (StaticManager.instance().getChildPageSelectedIndex() <= 0) {//第一次才懒加载
            initPagers();
        }
        LogUtils.e("initPagers onLazyLoad");
    }

    @Override
    protected void initView() {
        super.initView();
        LogUtils.e("initPagers init");
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        initPagers();
    }

    @Override
    protected void onInVisibleToUser() {
        super.onInVisibleToUser();
    }

    private void initPagers() {
        innerFragments = ViewManager.instance().getInnerFragments();
        mInnerPagerAdapter = new InnerPagerAdapter(getActivity().getSupportFragmentManager(), innerFragments);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.inner_view_page);
        mPageIndicator = (CirclePageIndicator) mRootView.findViewById(R.id.indicator);

        mRootView.findViewById(R.id.btn_back_to_standby).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onInnerPagerBackBtnClickListener != null) {
                    onInnerPagerBackBtnClickListener.onClick(null);
                }
            }
        });
        mViewPager.setAdapter(mInnerPagerAdapter);
        mPageIndicator.setSnap(true);
        mPageIndicator.setViewPager(mViewPager, this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        mViewPager.setCurrentItem(0,false);//要用childFragmentManager跳转
    }

    @Override
    public void onPageSelected(int position) {
        mInnerPagerSelecteIndex = position;
        StaticManager.instance().setChildPageSelectedIndex(position);
        LogUtils.i("onPageSelected inner " + mInnerPagerSelecteIndex);
    }

    @Subscribe
    public void updateChildPageIndexEvent(UpdateChildPageIndexEvent event) {
        LogUtils.e("UpdateChildPageIndexEvent");
        mViewPager.setCurrentItem(event.toPage, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BondStateChangedEvent event) {
        LogUtils.i(event.toString());
        innerFragments = ViewManager.instance().getInnerFragments();
        mInnerPagerAdapter.notifyDataSetChanged();
    }

    public void setOnInnerPagerBackBtnClickListener(View.OnClickListener listener) {
        this.onInnerPagerBackBtnClickListener = listener;
    }

}
