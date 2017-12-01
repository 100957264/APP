package com.android.fisewatchlauncher.acty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.manager.PreferControler;
import com.android.fisewatchlauncher.service.FiseService;
import com.android.fisewatchlauncher.widget.FiseRectAnalogClockFive;
import com.android.fisewatchlauncher.widget.FiseRectAnalogClockFour;
import com.android.fisewatchlauncher.widget.FiseRectAnalogClockOne;
import com.android.fisewatchlauncher.widget.FiseRectAnalogClockSix;
import com.android.fisewatchlauncher.widget.FiseRectAnalogClockThree;
import com.android.fisewatchlauncher.widget.FiseRectAnalogClockTwo;


public class FiseKeyguardActivity extends BaseActivity implements OnTouchListener, OnGestureListener {
    GestureDetector mGestureDetector;
    FiseRectAnalogClockOne mFiseRectAnalogClockOne;
    FiseRectAnalogClockTwo mFiseRectAnalogClockTwo;
    FiseRectAnalogClockThree mFiseRectAnalogClockThree;
    FiseRectAnalogClockFour mFiseRectAnalogClockFour;
    FiseRectAnalogClockFive mFiseRectAnalogClockFive;
    FiseRectAnalogClockSix mFiseRectAnalogClockSix;
    RelativeLayout mRelativeLayout;

    private static final int FLING_MIN_DISTANCE = 50;
    private static final int FLING_MIN_VELOCITY = 0;
    final String ACTION_SERVICE = "com.android.fisewatchlauncher.service.FiseService";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("fengqing", "onCreate  start.....---> setContentView.");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.keyguard_fise_analog_clock);

        FiseService.pull(this);
        mGestureDetector = new GestureDetector(this, this);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.activity_fise_keyguard);
        mRelativeLayout.setOnTouchListener(this);
        mRelativeLayout.setLongClickable(true);
        initView();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
       ;
        showSlectedView( PreferControler.instance().getAnalogClockIndex());
    }

    private void initView() {
        mFiseRectAnalogClockOne = (FiseRectAnalogClockOne) findViewById(R.id.analog_clock_1);
        mFiseRectAnalogClockThree = (FiseRectAnalogClockThree) findViewById(R.id.analog_clock_3);
        mFiseRectAnalogClockFour = (FiseRectAnalogClockFour) findViewById(R.id.analog_clock_4);
        mFiseRectAnalogClockFive = (FiseRectAnalogClockFive) findViewById(R.id.analog_clock_5);
        new View(this).setVisibility(View.VISIBLE);
    }

    private void showSlectedView(int id) {
        switch (id) {
            case 1:
                mFiseRectAnalogClockOne.setVisibility(View.VISIBLE);
                mFiseRectAnalogClockThree.setVisibility(View.GONE);
                mFiseRectAnalogClockFour.setVisibility(View.GONE);
                mFiseRectAnalogClockFive.setVisibility(View.GONE);
                break;
            case 2:
                mFiseRectAnalogClockOne.setVisibility(View.GONE);
                mFiseRectAnalogClockThree.setVisibility(View.VISIBLE);
                mFiseRectAnalogClockFour.setVisibility(View.GONE);
                mFiseRectAnalogClockFive.setVisibility(View.GONE);
                break;
            case 3:
                mFiseRectAnalogClockOne.setVisibility(View.GONE);
                mFiseRectAnalogClockThree.setVisibility(View.GONE);
                mFiseRectAnalogClockFour.setVisibility(View.VISIBLE);
                mFiseRectAnalogClockFive.setVisibility(View.GONE);
                break;
            case 4:
                mFiseRectAnalogClockOne.setVisibility(View.GONE);
                mFiseRectAnalogClockThree.setVisibility(View.GONE);
                mFiseRectAnalogClockFour.setVisibility(View.GONE);
                mFiseRectAnalogClockFive.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setAnalogClockStyle(int result) {
        PreferControler.instance().setAnalogClockIndex(result);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int style = data.getIntExtra("style", PreferControler.instance().getAnalogClockIndex());
                showSlectedView(style);
                setAnalogClockStyle(style);
            }
        }

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            Intent mIntent = new Intent(FiseKeyguardActivity.this, FiseLauncherActivity.class);
            startActivity(mIntent);
        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            Intent mIntent = new Intent(FiseKeyguardActivity.this, FiseLauncherActivity.class);
            startActivity(mIntent);
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Intent intent = new Intent(FiseKeyguardActivity.this, FiseAnalogClockSelect.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public static void showLockScreen(Context ctx) {
        Intent intent = new Intent(ctx, FiseKeyguardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }
}
