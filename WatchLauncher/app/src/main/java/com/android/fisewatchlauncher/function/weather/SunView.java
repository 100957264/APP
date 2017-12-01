package com.android.fisewatchlauncher.function.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.util.AttributeSet;

import com.android.fisewatchlauncher.R;


/**
 * @author mare
 * @Description:TODO 晴
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/10 0010
 * @time 23:15
 */
public class SunView extends BaseView {
    private Context mContext;
    private int viewWidth, viewHight;
    private Paint mPaint;
    private Bitmap mSun;
    private float sunStartX, sunStartY;
    private static final float MAX_SCALE = 1.1f;
    private static final float MIN_SCALE = 1.0f;

    public SunView(Context context) {
        this(context, null);
    }

    public SunView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPaint();
    }

    public void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.version_eye_2));
        mPaint.setStrokeWidth(15);
        mPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        mPaint.setStrokeJoin(Join.ROUND);//
        mPaint.setStrokeCap(Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL);// 实心
        this.setTag(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.setBackgroundDrawable(getResources().getDrawable(R.color.weather_color));
        mSun = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.bmp_weather_sun);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        beginAnimation(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int viewW = (int) Math.min(WeatherManager.getMeasuredW(), MAX_SCALE * mSun.getWidth() + sunStartX * 2);
        setMeasuredDimension(WeatherManager.getMeasuredW(), WeatherManager.getMeasuredH());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewWidth = getWidth();
        viewHight = getHeight();
        sunStartX = viewWidth / 2.96f;
        sunStartY = viewHight / 2f;
    }

    private float mDegrees = 0;

    private void drawSun(Canvas canvas) {
        int width = mSun.getWidth();
        canvas.save();
        canvas.scale(mScale, mScale, sunStartX + width / 2, sunStartY
                - moveDistance + width / 2);
        canvas.rotate(mDegrees, sunStartX + width / 2, sunStartY - moveDistance
                + width / 2);
        canvas.drawBitmap(mSun, sunStartX, sunStartY - moveDistance, mPaint);
        canvas.restore();
    }

    private boolean isScale = false;
    private int time = 0;

    private void beginScale() {
        if (time == 2) {
            mScale = 1;
            mDegrees += 0.8;
            return;
        }
        if (isScale) {
            if (mScale <= MIN_SCALE) {
                isScale = false;
                time++;
            } else {
                mScale -= 0.02;
            }
        } else {
            if (mScale >= MAX_SCALE) {
                isScale = true;
            } else {
                mScale += 0.02;
            }
        }

    }

    private int moveDistance = 0;
    private float mScale = 0;

    private void beginAnimation(Canvas canvas) {
        if (moveDistance >= 5 / 8 * viewHight) {
            beginScale();
        } else {
            moveDistance += 1 / 12 * viewHight;
            mScale += 0.066;
        }
        drawSun(canvas);
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        if (mSun != null) {
            mSun.recycle();
            mSun = null;
        }
    }

}