package com.marc.marclibs.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.marc.marclibs.R;

/**
 * 描述：自定义半环形
 * 作者：Marc on 2016/8/13 14:16
 * 邮箱：aliali_ha@yeah.net
 */
public class CircleBar extends View {


    private static final String TAG = "CircleBar";

    private RectF mColorWheelRectangle = new RectF();//圆圈的矩形范围
    private Paint mDefaultWheelPaint;////绘制底部灰色圆圈的画笔
    private Paint mColorWheelPaint;//绘制蓝色扇形的画笔
    private Paint textPaint;//中间数值文字的画笔
    private Paint textDesPaint;//描述文字的画笔
    private float mColorWheelRadius;
    private float circleStrokeWidth;//圆圈的线条粗细
    private float pressExtraStrokeWidth;
    private int mTextColor = getResources().getColor(R.color.blue);//默认文字颜色
    private int mWheelColor = getResources().getColor(R.color.blue);//默认圆环颜色

    private String mText;
    private String mTextDes;//文字的描述
    private int mTextDesSize;//描述文字的大小
    private int mCount;//为了做动画
    private float mSweepAnglePer;//扇形弧度百分比
    private float mSweepAngle;//扇形弧度
    private int mTextSize;//文字大小
    private int mDistance;// 上下文字的距离
    BarAnimation anim;//动画
    private int TIME = 1000;//时间

    public CircleBar(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    private void init(AttributeSet attrs, int defStyle) {
        //初始化一些值
        circleStrokeWidth = dip2px(getContext(), 7);
        pressExtraStrokeWidth = dip2px(getContext(), 2);
        mTextSize = dip2px(getContext(), 30);
        mTextDesSize = dip2px(getContext(), 15);
        mDistance = dip2px(getContext(), 30);//文字距离
        //外圆环的画笔
        mColorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorWheelPaint.setColor(mWheelColor);
        mColorWheelPaint.setStyle(Paint.Style.STROKE);
        mColorWheelPaint.setStrokeWidth(circleStrokeWidth);//圆圈的线条粗细
        mColorWheelPaint.setStrokeCap(Paint.Cap.ROUND);//开启显示边缘为圆形
        //默认圆的画笔
        mDefaultWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultWheelPaint.setColor(getResources().getColor(R.color.gray));
        mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
        mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);//圆圈的线条粗细
        mDefaultWheelPaint.setStrokeCap(Paint.Cap.ROUND);//开启显示边缘为圆形
        //数值的画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setColor(mTextColor);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(mTextSize);
        //描述文字的画笔
        textDesPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textDesPaint.setColor(getResources().getColor(R.color.gray));
        textDesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textDesPaint.setTextSize(mTextDesSize);
        textDesPaint.setTextAlign(Paint.Align.LEFT);

        mText = "0";
        mTextDes = "本次考试";
        mSweepAngle = 0;
        anim = new BarAnimation();
        anim.setDuration(TIME);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(mColorWheelRectangle, -180, 180, false, mDefaultWheelPaint);//画外接的圆环
        canvas.drawArc(mColorWheelRectangle, -180, mSweepAnglePer, false, mColorWheelPaint);//画圆环
        Rect bounds = new Rect();
        String textstr = mCount + "";

        textPaint.getTextBounds(textstr, 0, textstr.length(), bounds);
        textDesPaint.getTextBounds(mTextDes, 0, mTextDes.length(), bounds);

        // drawText各个属性的意思(文字,x坐标,y坐标,画笔)
        canvas.drawText(
                textstr + "",
                (mColorWheelRectangle.centerX())
                        - (textPaint.measureText(textstr) / 2),
                mColorWheelRectangle.centerY() + bounds.height() / 2 - 50 + 40,
                textPaint);
        canvas.drawText(mTextDes,
                (mColorWheelRectangle.centerX())
                        - (textDesPaint.measureText(mTextDes) / 2),
                mColorWheelRectangle.centerY() + bounds.height() / 2 - 50 - mDistance + 40
                , textDesPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        mColorWheelRadius = min - circleStrokeWidth - pressExtraStrokeWidth;

        mColorWheelRectangle.set(circleStrokeWidth + pressExtraStrokeWidth, circleStrokeWidth + pressExtraStrokeWidth,
                mColorWheelRadius, mColorWheelRadius);
    }


    //监听器类接口

    public static abstract interface OnClickListener {
        public abstract void onClick(); //单击事件处理接口
    }

    OnClickListener listener = null;   //监听器类对象

    //实现这个View的监听器

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;   //引用监听器类对象,在这里可以使用监听器类的对象
    }


    public void startCustomAnimation() {
        this.startAnimation(anim);
    }

    public void setText(String text) {
        mText = text;
        this.startAnimation(anim);
    }

    public void setDesText(String text) {
        mTextDes = text;
        this.startAnimation(anim);
    }

    public void setTextColor(int color) {
        mTextColor = color;
        textPaint.setColor(mTextColor);
    }

    public void setSweepAngle(float sweepAngle) {
        mSweepAngle = sweepAngle;
    }

    public void setWheelColor(int color) {
        this.mColorWheelPaint.setColor(color);
    }


    public class BarAnimation extends Animation {
        /**
         * Initializes expand collapse animation, has two types, collapse (1) and expand (0).
         * 1 will collapse view and set to gone
         */
        public BarAnimation() {

        }

        //        * 动画类利用了applyTransformation参数中的interpolatedTime参数(从0到1)的变化特点，
//                * 实现了该View的某个属性随时间改变而改变。原理是在每次系统调用animation的applyTransformation()方法时，
//                * 改变mSweepAnglePer，mCount的值，
//                * 然后调用postInvalidate()不停的绘制view。
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            //mSweepAnglePer，mCount这两个属性只是动画过程中要用到的临时属性，
            //mText和mSweepAngle才是动画结束之后表示扇形弧度和中间数值的真实值。
            if (interpolatedTime < 1.0f) {
                mSweepAnglePer = interpolatedTime * mSweepAngle;
                mCount = (int) (interpolatedTime * Float.parseFloat(mText));
            } else {
                mSweepAnglePer = mSweepAngle;
                mCount = Integer.parseInt(mText);
            }
            postInvalidate();
        }
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
