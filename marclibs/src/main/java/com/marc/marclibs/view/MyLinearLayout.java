package com.marc.marclibs.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {

    private DragLayout mDragLayout;
    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDraglayout(DragLayout mDragLayout) {
        this.mDragLayout = mDragLayout;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mDragLayout.getStatus() == DragLayout.Status.Close) {
            // 如果当前是关闭状态, 按之前方法判断
            return super.onInterceptTouchEvent(ev);//默认不会被拦截，交由子View的dispatchTouchEvent进行处理。
        } else {
            return true;//则表示将事件进行拦截，并将拦截到的事件交由本层控件 的 onTouchEvent 进行处理；
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 这里对事件进行处理
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果当前是关闭状态, 按之前方法处理
        if (mDragLayout.getStatus() == DragLayout.Status.Close) {
            return super.onTouchEvent(event);
        } else {
            // 手指抬起, 执行关闭操作
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mDragLayout.close();
            }
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    //手指按下
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }

    }

}
