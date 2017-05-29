package com.zbie.other.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 涛 on 2017/5/5 0005.
 * 项目名           Practice02
 * 包名             com.zbie.other.view
 * 创建时间         2017/05/05 23:52
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */

public class RingRotateView extends View {

    private static final String TAG = "RingRotateView";

    private Context mContext;

    private Paint mRingPaint;
    private Paint mCirclePaint;

    private int width; // 外圆弧宽
    private int height; // 外圆弧高
    private int outRadius; // 外圆弧半径
    private int inRadius; // 小圆半径

    private float outX, outY, inX, inY;//外圆弧和小圆中心点

    private float distance;//大圆中心点到小圆中心点的距离

    private int PADDING       = 50;
    private int STROKEN_WIDTH = 4; // 圆环宽度

    private CirclePoint mCurrentPoint;

    public RingRotateView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public RingRotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public RingRotateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mRingPaint = new Paint();
        mRingPaint.setStrokeWidth(STROKEN_WIDTH);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setColor(Color.WHITE);
        mRingPaint.setAntiAlias(true);

        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.RED);
        mCirclePaint.setAntiAlias(true);

        mCurrentPoint = new CirclePoint(0, 10, Color.parseColor("#f9cce2"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        outRadius = width > height ? height / 2 - PADDING : width / 2 - PADDING;
        inRadius = outRadius / 16;
        outX = width / 2;
        outY = height / 2;
        distance = outRadius - inRadius - STROKEN_WIDTH / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(outX, outY, outRadius, mRingPaint);
        inX = outX + distance * (float) Math.sin(mCurrentPoint.angle / 360 * 2 * Math.PI);
        inY = outY - distance * (float) Math.cos(mCurrentPoint.angle / 360 * 2 * Math.PI);
        canvas.drawCircle(inX, inY, inRadius, mCirclePaint);
    }

    public void startAnimation() {

        ValueAnimator animator = ValueAnimator.ofObject(new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                CirclePoint start = (CirclePoint) startValue;
                CirclePoint end   = (CirclePoint) endValue;

                float startAngle   = start.angle;
                float endAngle     = end.angle;
                float currentAngle = startAngle + (endAngle - startAngle) * fraction;

                float startRadius   = start.radius;
                float endRadius     = end.radius;
                float currentRadius = startRadius + (endRadius - startRadius) * fraction * 5;
                //Log.i(TAG, "startRadius === " + startRadius + ", endRadius === " + endRadius + ", currentRadius === " + currentRadius);

                int startColor   = start.color;
                int endColor     = end.color;
                int currentColor = (int) (startColor + (endColor - startColor) * fraction);
                //Log.i(TAG, "startColor === " + startColor + ", endColor === " + endColor + ", currentColor === " + currentColor);

                // return new CirclePoint(currentAngle);
                return new CirclePoint(currentAngle, currentRadius, currentColor);
                // return new CirclePoint(currentAngle, 10, Color.parseColor("#f9cce2"));
            }
            // }, new CirclePoint(0), new CirclePoint(360));
            // }, new CirclePoint(0, 10, Color.parseColor("#f9cce2")), new CirclePoint(360, 10, Color.parseColor("#f9cce2")));
        }, new CirclePoint(0, 10, Color.parseColor("#000000")), new CirclePoint(360, 200, Color.parseColor("#ff0000")));

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurrentPoint = (CirclePoint) valueAnimator.getAnimatedValue();
                invalidate(); // 具体绘制逻辑需要在onDraw中实现
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);//无限重复
        animator.setDuration(1000);
        animator.start();
    }

    private class CirclePoint {
        public float angle;
        public float radius;
        public int   color;

        public CirclePoint(float angle, float radius, int color) {
            this.angle = angle;
            this.radius = radius;
            this.color = color;
        }

        public CirclePoint(float angle) {
            this.angle = angle;
        }
    }
}
