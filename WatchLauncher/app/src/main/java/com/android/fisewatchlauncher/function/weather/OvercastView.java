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
import com.android.fisewatchlauncher.utils.LogUtils;

/**
 * @author mare
 * @Description:TODO 阴天
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/10 0010
 * @time 23:15
 */
public class OvercastView extends BaseView {
    private static final String TAG = "BigRainView";
    private Context mContext;
    private int viewWidth, viewHight;
    private Paint mPaint;
    private Bitmap smallCloudy, bigCloudy;
    private float fristCloudyStartX, fristCloudyStartY;
    private float secondCloudyStartX, secondCloudyStartY;

    /**
     * 白云左右移动最大距离
     */
    private static int MAX_CLOUDYMOVE = 430;

    public OvercastView(Context context) {
        this(context, null);
    }

    public OvercastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPaint();
        init();
    }

    private void init() {
        MAX_CLOUDYMOVE = (int) (/*3 / 4f * */viewWidth);
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
        smallCloudy = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.bmp_weather_small_cloud);
        bigCloudy = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.bmp_weather_big_cloud);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        beginAnimation(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        // TODO Auto-generated method stub
        super.onLayout(changed, left, top, right, bottom);
        if (moveDistance != 0) {
            return;
        }
        viewWidth = getWidth();
        viewHight = getHeight();
        fristCloudyStartX = -viewWidth / 4f;
        fristCloudyStartY = viewHight / 4f;
        secondCloudyStartX = viewWidth;
        secondCloudyStartY = viewHight / 5.5f;
    }

    private float moveDistance = 0;

    // 画大小白云
    private void drawBitmap(Canvas canvas) {
        mPaint.setAlpha(255);
        canvas.drawBitmap(smallCloudy, secondCloudyStartX - moveDistance,
                secondCloudyStartY, mPaint);
        canvas.drawBitmap(bigCloudy, fristCloudyStartX + moveDistance,
                fristCloudyStartY, mPaint);
    }


    private boolean isMove = false;

    private void moveCloudy(Canvas canvas) {
        if (isMove) {
            moveDistance += 0.5f;
            if (moveDistance >= viewWidth * 3 / 5f) {
                isMove = false;
            }
        } else {
            moveDistance -= 0.6;
            if (moveDistance <= viewWidth * 2 / 5f) {
                isMove = true;
            }
        }
    }

    /**
     * 开始动画
     */
    private void beginAnimation(Canvas canvas) {
        if ((Boolean) this.getTag()) {
            // reboundEffect(canvas);
            moveCloudy(canvas);
        } else {
            moveDistance += 2;
            if (moveDistance >= MAX_CLOUDYMOVE) {
                this.setTag(true);
            }
        }
        drawBitmap(canvas);
        LogUtils.e("moveDistance " + moveDistance);
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        if (smallCloudy != null) {
            smallCloudy.recycle();
            smallCloudy = null;
        }
        if (bigCloudy != null) {
            bigCloudy.recycle();
            bigCloudy = null;
        }
    }

}