package com.android.fisewatchlauncher.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.android.fisewatchlauncher.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * project : AlarmViewDemo
 * author : ChenJP
 * date : 2017/10/25  17:14
 * describe : 闹钟动画
 */

public class AlarmAnimView extends View {
    private Bitmap mAlarmBitmap;
    private float  mPositionX;
    private float  mPositionY;
    private String  TAG         = "chenjp";
    private float   mDegrees    = 0f;
    public int mCenterX;
    public int mCenterY;
    private boolean mStopAnim = false;
    private boolean increate  = true;
    public ExecutorService mSingleThreadExecutor;

    public AlarmAnimView(Context context) {
        super(context);
    }

    public AlarmAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AlarmAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        mAlarmBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_clock);
        mSingleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCenterX = getMeasuredWidth() / 2;
        mCenterY = getMeasuredHeight() / 2;
        mPositionX = mCenterX - mAlarmBitmap.getWidth() / 2;
        mPositionY = mCenterY - mAlarmBitmap.getHeight() / 2;
    }

    private void startAnim() {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mStopAnim = false;
                while (!mStopAnim) {
                    if (increate) {
                        mDegrees += 6;
                    } else {
                        mDegrees -= 6;
                    }
                    if (mDegrees > 13) {
                        increate = false;
                    } else if (mDegrees < -13) {
                        increate = true;
                    }
                    postInvalidate();
                    SystemClock.sleep(16);
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(mDegrees, mCenterX, mCenterY);
        canvas.drawBitmap(mAlarmBitmap, mPositionX, mPositionY, null);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initView();
        startAnim();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAlarmBitmap != null) {
            mAlarmBitmap.recycle();
        }
        mStopAnim = true;
        mSingleThreadExecutor.shutdownNow();
    }
}
