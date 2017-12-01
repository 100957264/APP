package com.android.fisewatchlauncher.function.weather;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/11 0011
 * @time 02:03
 */

public class BaseView extends View {

    public BaseView(Context context) {
        this(context, null);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(WeatherManager.getMeasuredW(), WeatherManager.getMeasuredH());
    }
}
