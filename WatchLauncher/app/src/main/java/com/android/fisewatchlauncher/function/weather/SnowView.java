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
 * @Description:TODO 雪
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/10 0010
 * @time 23:15
 */
public class SnowView extends BaseView {
    private static final String TAG = "SnowView";
    private Context mContext;
    private int viewWidth, viewHight;
    private Paint mPaint;
    private Bitmap mSnow;
    private int mSnowWidth, mSnowHeight;
    private float secondCloudyStartX, secondCloudyStartY;

    public SnowView(Context context) {
        this(context, null);
    }

    public SnowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPaint();
    }

    private void initPaint() {
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
        mSnow = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.bmp_weather_snowbig);
        mSnowWidth = mSnow.getWidth();
        mSnowHeight = mSnow.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        beginAnimation(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewWidth = getWidth();
        viewHight = getHeight();
        secondCloudyStartX = /*viewWidth / 1.93f*/0;
        secondCloudyStartY = viewHight / 2f;
    }

    /**
     * 此变量代表雪花移动的距离
     */
    private int moveDistance = 0;

    private void drawBitmap(Canvas canvas) {
        canvas.drawBitmap(mSnow, secondCloudyStartX + moveDistance,
                secondCloudyStartY, mPaint);
    }

    private float mDegrees = 0;
    private int mAlpha = 0;

    private void display(Canvas canvas, int item) {
        float startX = 0;
        float startY = 0;
        float mScale = 0;
        switch (item) {
            case 0:
                startX = -40 / 800f * viewWidth;
                startY = moveDistance * 5;
                mScale = 0.5f;
                break;
            case 1:
                startX = 50 / 800f * viewWidth;
                startY = moveDistance + 90;
                mScale = 0.1f;
                break;
            case 2:
                startX = 100 / 800f * viewWidth;
                startY = moveDistance * 2 - 110;
                mScale = 0.3f;
                break;
            case 3:
                startX = 250 / 800f * viewWidth;
                startY = moveDistance * 8 - 400;
                mScale = 0.8f;
                break;
            case 4:
                startX = 400 / 800f * viewWidth;
                startY = moveDistance * 2 - 10;
                mScale = 0.3f;
                break;
            case 5:
                startX = 600 / 800f * viewWidth;
                startY = moveDistance - 60;
                mScale = 0.1f;
                break;
            case 6:
                startX = 550 / 800f * viewWidth;
                startY = moveDistance * 5.2f;
                mScale = 0.5f;
                break;
        }
        drawSnow(canvas, startX, startY, mScale);
    }

    private void drawAll(Canvas canvas) {
        for (int i = 0; i < 7; i++) {
            display(canvas, i);
        }
    }

    /***
     *
     * @param canvas
     */
    private void drawSnow(Canvas canvas, float startX, float startY,
                          float mScale) {
        mPaint.setAlpha(mAlpha);
        canvas.save();
        canvas.scale(mScale, mScale, startX + mSnowWidth / 2, startY
                + moveDistance + mSnowHeight / 2);
        canvas.rotate(mDegrees, startX + mSnowWidth / 2, startY + moveDistance
                + mSnowHeight / 2);
        canvas.drawBitmap(mSnow, startX, startY + moveDistance, mPaint);
        canvas.restore();
        // canvas.drawBitmap(mSnow, mTranMatrix, mPaint);
    }

    private void beginAnimation(Canvas canvas) {
        int distance = 0;
        int alpha = 0;
        mDegrees += 5;
        moveDistance += 1;
        mAlpha += 3;
        if (moveDistance == 85) {
            mCurMoveDistance = moveDistance;
            mcurAlpha = mAlpha;
            moveDistance = 0;
            mAlpha = 0;
        }
        distance = moveDistance;
        alpha = mAlpha;

        if (mCurMoveDistance >= 85) {
            mCurMoveDistance += 1;
            mcurAlpha -= 6;
            moveDistance = mCurMoveDistance;
            mAlpha = mcurAlpha;
            drawAll(canvas);
        }
        if (mCurMoveDistance >= 127) {
            mCurMoveDistance = 0;
            mcurAlpha = 0;
        }
        moveDistance = distance;
        mAlpha = alpha;

        drawAll(canvas);
        invalidate();
    }

    private int mCurMoveDistance = 0;
    private int mcurAlpha = 0;


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mSnow != null) {
            mSnow.recycle();
            mSnow = null;
        }
    }
}