package com.android.fisewatchlauncher.function.weather;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * @author mare
 * @Description:TODO 天气动画管理
 * @date 2017/9/10 0010
 * @time 23:15
 */
public class WeatherManager {
    public static final int UNKNOWN = 0;
    public static final int Sun = 1;
    public static final int Cloudy = 2;
    public static final int Overcast = 3;
    public static final int Shower = 4;
    public static final int SmallRain = 5;
    public static final int Snow = 6;
    public static final int BigRain = 7;
    public static final int Thundershower = 8;
    public static final int Haze = 9;

    public enum WeatherType {
        UNKNOWN, Sun, BigRain, Cloudy, Overcast, Shower, SmallRain, Snow, Thundershower, Haze
    }

    public WeatherType int2Type(int state) {
        WeatherType type = null;
        switch (state) {
            case Sun:
                type = WeatherType.Sun;
                break;
            case Cloudy:
                type = WeatherType.Cloudy;
                break;
            case Overcast:
                type = WeatherType.Overcast;
                break;
            case Shower:
                type = WeatherType.Shower;
                break;
            case SmallRain:
                type = WeatherType.SmallRain;
                break;
            case Snow:
                type = WeatherType.Snow;
                break;
            case BigRain:
                type = WeatherType.BigRain;
                break;
            case Thundershower:
                type = WeatherType.Thundershower;
                break;
            case Haze:
                type = WeatherType.Haze;
                break;
            default:
                type = WeatherType.UNKNOWN;
                break;
        }
        return type;
    }

    public int type2Int(WeatherType type) {
        int state = UNKNOWN;
        switch (type) {
            case Sun:
                state = Sun;
                break;
            case BigRain:
                state = BigRain;
                break;
            case Cloudy:
                state = Cloudy;
                break;
            case Overcast:
                state = Overcast;
                break;
            case Shower:
                state = Shower;
                break;

            case SmallRain:
                state = SmallRain;
                break;
            case Snow:
                state = Snow;
                break;
            case Thundershower:
                state = Thundershower;
                break;
            case Haze:
                state = Haze;
                break;
            default:
                state = UNKNOWN;
                break;
        }
        return state;
    }

    public boolean isShowing;

    public WeatherManager(Context ctx, ViewGroup root) {
        this.mContext = ctx;
        this.mRoot = root;
        init();
    }

    private WeatherType mType;
    private int mState = UNKNOWN;
    private ViewGroup mRoot;
    private Context mContext;

    public void show(WeatherType type) {

        if (mType != type) {
            mRoot.removeAllViews();
            View child = generateView(type);
            boolean vis = null != child;
            if (vis) {
                mRoot.addView(child);
            }
            mRoot.setVisibility(vis ? View.VISIBLE : View.GONE);
            mType = type;
            mState = type2Int(type);
        }

    }

    public void showNext() {
        int len = WeatherType.values().length;
        mState = (mState + 1) % len;
        if (mState <= 0) {
            mState = 1;
        }
        show(int2Type(mState));
    }

    public void showPrevious() {
        int len = WeatherType.values().length;
        mState = (mState - 1) % len;
        if (mState <= 0) {
            mState = len - 1;
        }
        show(int2Type(mState));
    }


    private static int measuredW, measuredH;

    private void init() {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int w = metrics.widthPixels;
        int h = metrics.heightPixels;
        this.measuredW = w / 3;
        this.measuredH = h / 3;
    }

    public static int getMeasuredW() {
        return measuredW;
    }

    public static int getMeasuredH() {
        return measuredH;
    }

    private View generateView(WeatherType type) {
        View view = null;
        switch (type) {
            case Sun:
                view = new SunView(mContext);
                break;
            case BigRain:
                view = new BigRainView(mContext);
                break;
            case Cloudy:
                view = new CloudyView(mContext);
                break;
            case Overcast:
                view = new OvercastView(mContext);
                break;
            case Shower:
                view = new ShowerView(mContext);
                break;
            case SmallRain:
                view = new SmallRainView(mContext);
                break;
            case Snow:
                view = new SnowView(mContext);
                break;
            case Thundershower:
                view = new ThundershowerView(mContext);
                break;
            case Haze:
                view = new HazeView(mContext);
                break;
            default:
                view = null;
                break;
        }
        return view;
    }


    /**
     * 定义从右侧进入的动画效果
     *
     * @return
     */
    public static Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(200);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    /**
     * 定义从左侧退出的动画效果
     *
     * @return
     */
    public static Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(200);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    /**
     * 定义从左侧进入的动画效果
     *
     * @return
     */
    public static Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(200);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    /**
     * 定义从右侧退出时的动画效果
     *
     * @return
     */
    public static Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(200);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }

}
