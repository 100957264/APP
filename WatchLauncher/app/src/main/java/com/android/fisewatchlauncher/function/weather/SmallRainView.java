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
 * @Description:TODO 小雨
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/10 0010
 * @time 23:15
 */
public class SmallRainView extends BaseView {
    private static final String TAG = "BigRainView";
    private Context mContext;
    private int viewWidth, viewHight;
    private Paint mPaint, mRainDropsPaint;
    private Bitmap cloudy1, cloudy2, mRaindrops;
    private float fristCloudyStartX, fristCloudyStartY;
    private float secondCloudyStartX, secondCloudyStartY;

    public SmallRainView(Context context) {
        this(context, null);
    }

    public SmallRainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPaint();
    }

    public void initPaint() {
        mPaint = new Paint();
        mRainDropsPaint = new Paint();
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
        cloudy1 = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.bmp_weather_cloud_right);
        cloudy2 = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.bmp_weather_bigcloudy);
        mRaindrops = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.bmp_weather_raindrops);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        beginAnimation(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        // TODO Auto-generated method stub
        super.onLayout(changed, left, top, right, bottom);
        viewWidth = getWidth();
        viewHight = getHeight();
        fristCloudyStartX = -viewWidth / 4f;
        fristCloudyStartY = viewHight / 4f;
        secondCloudyStartX = viewWidth;
        secondCloudyStartY = viewHight / 4.7f;
    }

    private float moveDistance = 0;

    private void drawBitmap(Canvas canvas) {
        canvas.drawBitmap(cloudy1, fristCloudyStartX + moveDistance,
                fristCloudyStartY, mPaint);
        canvas.drawBitmap(cloudy2, secondCloudyStartX - moveDistance,
                secondCloudyStartY, mPaint);
    }

    private void drawRaindrops(Canvas canvas, int startX, int startY) {
        canvas.drawBitmap(mRaindrops, startX, startY, mRainDropsPaint);
    }

    private void showRainDrops(Canvas canvas) {
        int startX = (int) (1 / 4f * viewWidth), startY = 0;
        for (int i = 0; i < 3; i++) {
            startX += 1 / 12f * viewWidth;
            startY = (int) (1 / 2f * viewHight);
            if (i == 0) {
                startY += moveDistanceY;
                mRainDropsPaint.setAlpha(mDropsAlpha);
            } else if (i == 1) {
                startY = (int) (startY - 1 / 10f * viewHight + moveDistanceY);
                mRainDropsPaint.setAlpha(mDropsAlpha1);
            } else {
                startY += moveDistanceY;
                mRainDropsPaint.setAlpha(mDropsAlpha2);
            }
            drawRaindrops(canvas, startX, startY);
        }
    }

    private int mDropsAlpha = 50, mDropsAlpha1 = 50, mDropsAlpha2 = 50;
    private boolean bl1 = false, bl2 = false, bl3 = false;

    private int moveDistanceY = 0;

    private void displayDrops(Canvas canvas) {
        moveDistanceY += 2;
        if (moveDistanceY >= 7 / 12f * viewHight) {
            moveDistanceY = 0;
            bl1 = false;
            bl2 = false;
            bl3 = false;
            mDropsAlpha = 50;
            mDropsAlpha1 = 50;
            mDropsAlpha2 = 50;
        }
        if (bl1) {
            mDropsAlpha -= 6;
            if (mDropsAlpha <= 0) {
                mDropsAlpha = 0;
            }
        } else {
            mDropsAlpha += 6;
            if (mDropsAlpha >= 244) {
                bl1 = true;
            }
        }

        if (bl2) {
            mDropsAlpha1 -= 3;
            if (mDropsAlpha1 <= 0) {
                mDropsAlpha1 = 0;
            }
        } else {
            mDropsAlpha1 += 3;
            if (mDropsAlpha1 >= 244) {
                bl2 = true;
            }
        }
        if (bl3) {
            mDropsAlpha2 -= 4;
            if (mDropsAlpha2 <= 0) {
                mDropsAlpha2 = 0;
            }
        } else {
            mDropsAlpha2 += 4;
            if (mDropsAlpha2 >= 244) {
                bl3 = true;
            }
        }
        showRainDrops(canvas);
    }

    private void beginAnimation(Canvas canvas) {
        if ((Boolean) this.getTag()) {
            if (moveDistance >= 1 / 2f * viewWidth) {//0.56
                moveDistance -= 0.2;
            }
            displayDrops(canvas);
        } else {
            moveDistance += 4;
            if (moveDistance >= 3 / 5f * viewWidth) {//0,59
                this.setTag(true);
            }
        }
        drawBitmap(canvas);
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        if (cloudy1 != null) {
            cloudy1.recycle();
            cloudy1 = null;
        }
        if (cloudy2 != null) {
            cloudy2.recycle();
            cloudy2 = null;
        }
    }

}