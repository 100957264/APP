package com.android.fisewatchlauncher.function.view;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.android.fisewatchlauncher.BuildConfig;
import com.android.fisewatchlauncher.FlavorDiff;
import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.fragment.InnerPageFragment;
import com.android.fisewatchlauncher.fragment.KeyguardFragment;
import com.android.fisewatchlauncher.fragment.PagerFragment;
import com.android.fisewatchlauncher.fragment.QrCodeFragment;
import com.android.fisewatchlauncher.fragment.StandByFragment;
import com.android.fisewatchlauncher.holder.ActivityTarget;
import com.android.fisewatchlauncher.holder.PageHolder;
import com.android.fisewatchlauncher.listener.BaseViewListener;
import com.android.fisewatchlauncher.manager.PreferControler;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/26
 * @time 13:14
 */
public class ViewManager {

    private ViewManager() {
    }

    private static class SingletonHolder {
        private static final ViewManager INSTANCE = new ViewManager();
    }

    public static ViewManager instance() {
        return SingletonHolder.INSTANCE;
    }

    private List<BaseViewListener> listeners = new ArrayList<>();
    private List<Fragment> innerFragments = new ArrayList<>();
    private List<Fragment> outerFragments = new ArrayList<>();

    private void initInnerPagers() {
        innerFragments.clear();
        List<Integer> innerPagesIcons = Arrays.asList(FlavorDiff.getHomePageIcons());
        List<Integer> innerPagesTitles = Arrays.asList(FlavorDiff.getHomePageTitles());
        List<ActivityTarget> innerPagesActivity = Arrays.asList(FlavorDiff.getActivityTargets());
        int len = innerPagesTitles.size();
        PageHolder holder;
        int titleID;
        for (int i = 0; i < len; i++) {
            titleID = innerPagesTitles.get(i);
            if (isContainFilter(titleID)) { //过滤哪些不要的
                continue;
            }
            holder = new PageHolder(innerPagesIcons.get(i), innerPagesTitles.get(i), innerPagesActivity.get(i));
            innerFragments.add(PagerFragment.newInstance(holder));
        }
        LogUtils.e("initInnerPagers size " + innerFragments.size());
        if (isDeviceBond() ||BuildConfig.FLAVOR.equals("FISE_WSD")) {
            LogUtils.d("hide QRCodeFragment while");
        } else {
            innerFragments.add(QrCodeFragment.newInstance());
        }
    }

    private boolean isContainFilter(int titleID) {
        boolean isShouldFilter = false;
        switch (titleID) {
            case R.string.function_img:
            case R.string.function_story:
            case R.string.function_english_study:
            case R.string.function_heart_rate:
            case R.string.function_recorder:
            case R.string.function_video:
                isShouldFilter = true;
                break;
            case R.string.function_phonebook:
            case R.string.function_wetchat:
                if (!isDeviceBond()) {
                    isShouldFilter = true;
                } else {
                    isShouldFilter = false;
                }
                break;
            default:
                isShouldFilter = false;
                break;
        }
        LogUtils.d("isContainFilter:titleID =" + titleID + " ,isShouldFilter =" + isShouldFilter);

        return isShouldFilter;
    }

    private boolean isImeiNull() {
        return TextUtils.isEmpty(GlobalSettings.instance().getImei());
    }

    private boolean isDeviceBond() {
        return PreferControler.instance().getDeviceBondState();
    }


    /**
     * 用于过滤未绑定页面或者没写imei情况
     *
     * @return
     */
    public boolean isFiliter() {
        if (!isImeiNull() && isDeviceBond())
            return false;
        return true;
    }

    private void initOuterPagers() {
        outerFragments.clear();
        if(BuildConfig.FLAVOR.equals("FISE_WSD")) {
            outerFragments.add(KeyguardFragment.newInstance());
        }else {
          outerFragments.add(StandByFragment.newInstance());
        }
        outerFragments.add(InnerPageFragment.newInstance());
    }

    public void init() {
        listeners.clear();
        initOuterPagers();
        initInnerPagers();
    }


    public void setBaseViewListener(BaseViewListener listener) {
        listeners.add(listener);
    }

    public void onStart() {
        for (BaseViewListener listener : listeners) {
            listener.onStart();
        }
    }

    public void onStop() {
        for (BaseViewListener listener : listeners) {
            listener.onStop();
        }
    }

    public List<Fragment> getOuterFragments() {
        return outerFragments;
    }

    public List<Fragment> getInnerFragments() {
        return innerFragments;
    }

    public void updateInnerPagers() {
        initInnerPagers();
        //        EventBus.getDefault().postSticky(new BondStateChangedEvent());
    }
}
