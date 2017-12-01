package com.android.fisewatchlauncher.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import static com.android.fisewatchlauncher.widget.DragViewGroup.PullState.CLOSED;
import static com.android.fisewatchlauncher.widget.DragViewGroup.PullState.OPENED;
import static com.android.fisewatchlauncher.widget.DragViewGroup.PullState.SLIDING;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/28
 * @time 16:34
 */
public class DragViewGroup extends FrameLayout {
    View topSheet, mainContent;
    private boolean canDrag = true;
    private ViewDragHelper mViewDragHelper;

    private int mWidth;
    private boolean isConflictWithPager = false;
    private int lastX, lastY;
    private int sheetOriginalHeight = 0;
    private int sheetHeight;
    private boolean isDraging = false, isCapturing = false;
    private PullState mState = PullState.CLOSED;

    public DragViewGroup(Context context) {
        this(context, null);
    }

    public DragViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, callback);
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        // 何时开始检测触摸事件
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            // 如果当前触摸的child是mMainView时开始检测
            return canDrag && !isConflictWithPager && (child == topSheet || topSheet.getTop() >= sheetOriginalHeight);
        }

        // 触摸到View后回调
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            isCapturing = capturedChild == topSheet;
            Log.e("mare", "onViewCaptures isCapturing " + isCapturing);
        }

        // 当拖拽状态改变，比如idle，dragging
        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            isDraging = state == ViewDragHelper.STATE_DRAGGING;
            Log.e("mare", "onViewDragStateChanged " + state2String(state));
        }

        // 当位置改变时调用，常用于滑动时更改scale等
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            Log.e("mare", "clampViewPositionVertical top " + top + " dy " + dy);

//            settingSheet(closePanel, openpanel, sheetCanDrag);
            int sheetTop = topSheet.getTop();
            boolean closePanel = sheetTop < 0;
            boolean openpanel = sheetTop < 0;
            boolean sheetCanDrag = !isConflictWithPager && canDrag;
//            settingSheet(closePanel, openpanel, sheetCanDrag);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.e("mare", "clampViewPositionVertical top " + top);
            int topBound = sheetHeight;//顶端边界
            int bottomBound = getPaddingBottom();//底端边界
            int bound = Math.min(Math.max(topBound, top), bottomBound);
            Log.e("mare", " bound : " + bound);
            return bound;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            boolean conflict = Math.abs(left) > mViewDragHelper.getTouchSlop();
//            Log.e("mare","conflict " + conflict + " , left " +left );
//            isConflictWithPager = conflict;
            return 0;
        }

        // 拖动结束后调用
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            // 手指抬起后缓慢移动到指定位置
            Log.e("mare", "yvel " + yvel);
            boolean closePanel = yvel < 0;
            boolean openpanel = yvel > 0;

            Log.e("mare", "closePanel " + closePanel + " , openpanel " + openpanel);
            settingSheet(closePanel, openpanel);
            invalidate();
        }

        @Override
        public int getViewVerticalDragRange(View child) {

            return getMaxRange();
        }
    };

    private int getMaxRange() {
        return 0;
    }

    private void settingSheet(boolean closePanel, boolean openpanel) {
        if (openpanel) {
            openPanel();
        }
        if (closePanel) {
            closePanel();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int curX = (int) ev.getX();
        int curY = (int) ev.getY();
        int action = ev.getAction();
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                mViewDragHelper.cancel(); // 相当于调用 processTouchEvent收到ACTION_CANCEL
                lastX = curX;
                lastY = curY;
                break;

            case MotionEvent.ACTION_MOVE:
                int offsetX = curX - lastX;
                int offsetY = curY - lastY;
//                if (Math.abs(offsetX) >= mViewDragHelper.getTouchSlop() || Math.abs(offsetX) >= Math.abs(offsetY)) {
//                    closePanel();
//                    return false;
//                }
                if (Math.abs(offsetY) >= mViewDragHelper.getTouchSlop() || Math.abs(offsetY) >= Math.abs(offsetX)) {

                } else {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_CANCEL:
                mViewDragHelper.cancel(); // 相当于调用 processTouchEvent收到ACTION_CANCEL
                break;
        }

        /**
         * 检查是否可以拦截touch事件
         * 如果onInterceptTouchEvent可以return true 则这里return true
         */
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将触摸事件传递给ViewDragHelper，此操作必不可少
        if (event.getAction() == MotionEvent.ACTION_CANCEL){
                mViewDragHelper.cancel();
        }else{
            mViewDragHelper.processTouchEvent(event);
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mainContent = getChildAt(0);
        topSheet = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mainContent.getMeasuredWidth();
        sheetHeight = topSheet.getMeasuredHeight();
        sheetOriginalHeight = -sheetHeight;
        mViewDragHelper.smoothSlideViewTo(topSheet, 0, sheetOriginalHeight);
    }

    public boolean isCanDrag() {
        return canDrag;
    }

    public void setCanDrag(boolean canDrag) {
        this.canDrag = canDrag;
    }

    public boolean isConflictWithPager() {
        return isConflictWithPager;
    }

    public void setConflictWithPager(boolean conflictWithPager) {
        isConflictWithPager = conflictWithPager;
        if (isConflictWithPager) {
            mViewDragHelper.smoothSlideViewTo(topSheet, 0, sheetOriginalHeight);
        }
    }

    private String state2String(int state) {
        String stateStr = null;
        switch (state) {
            case ViewDragHelper.STATE_IDLE:
                stateStr = "STATE_IDLE";
                break;
            case ViewDragHelper.STATE_DRAGGING:
                stateStr = "STATE_DRAGGING";
                break;
            case ViewDragHelper.STATE_SETTLING:
                stateStr = "STATE_SETTLING";
                break;
        }
        return stateStr;
    }

    private void closePanel() {
        boolean sheetCanDrag = !isConflictWithPager && canDrag;
        if (topSheet.getTop() > sheetOriginalHeight && sheetCanDrag) {
            // 关闭菜单
           boolean continueSliding =  mViewDragHelper.smoothSlideViewTo(topSheet, 0, sheetOriginalHeight);
            ViewCompat.postInvalidateOnAnimation(DragViewGroup.this);
            if (continueSliding){
                mState = SLIDING;
            }else{
                mState = CLOSED;
            }
        }
    }

    private void openPanel() {
        boolean sheetCanDrag = !isConflictWithPager && canDrag;
        if (topSheet.getTop() < 0 && sheetCanDrag) {
            // 打开菜单
            Log.e("mare", "openpanel ");
          boolean continueSliding =   mViewDragHelper.smoothSlideViewTo(topSheet, 0, 0);
            ViewCompat.postInvalidateOnAnimation(DragViewGroup.this);
            if (continueSliding){
                mState = SLIDING;
            }else{
                mState = OPENED;
            }
        }
    }

    public ViewDragHelper getViewDragHelper(){
        return mViewDragHelper;
    }

    public  enum PullState{
      SLIDING,OPENED,CLOSED
    }

    public boolean isUnClosed() {
        return mState != CLOSED;
    }

}
