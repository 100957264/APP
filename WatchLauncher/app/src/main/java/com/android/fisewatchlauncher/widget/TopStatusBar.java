package com.android.fisewatchlauncher.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by user on 2017/9/8.
 */

public class TopStatusBar extends FrameLayout {
    public TopStatusBar(Context context) {
        this(context,null);
    }

    public TopStatusBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TopStatusBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }
}
