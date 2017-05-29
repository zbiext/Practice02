package com.zbie.other.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.zbie.other.R;

/**
 * Created by 涛 on 2017/5/5 0005.
 * 项目名           Practice02
 * 包名             com.zbie.other.view
 * 创建时间         2017/05/05 23:54
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */

public class TempControlView extends View {

    private static final String TAG = "TempControlView";
    /**
     * 刻度盘画笔
     */
    private Paint                mDialPaint;
    /**
     * 圆弧画笔
     */
    private Paint                mArcPaint;
    /**
     * 标题画笔
     */
    private Paint                mTitlePaint;
    /**
     * 温度标识画笔
     */
    private Paint                mTempFlagPaint;
    /**
     * 旋钮画笔
     */
    private Paint                mBtnPaint;
    /**
     * 抗锯齿
     */
    private PaintFlagsDrawFilter mPaintFlagsDrawFilter;
    /**
     * 温度显示画笔
     */
    private Paint                mTempPaint;

    /**
     * 控件宽
     */
    private int mWidth;
    /**
     * 控件高
     */
    private int mHeight;
    /**
     * 刻度盘半径
     */
    private int mDialRadius;
    /**
     * 圆弧半径
     */
    private int mArcRadius;
    /**
     * 刻度线长度
     */
    private int mScaleHeight = dp2px(10);

    /**
     * 文本提示
     */
    private String mTitle       = "最高温度设置";
    /**
     * 温度
     */
    private int    mTemperature = 15;
    /**
     * 最低温度
     */
    private int    mMinTemp     = 15;
    /**
     * 最高温度
     */
    private int    mMaxTemp     = 30;
    /**
     * 四格（每格4.5度，共18度）代表温度1度
     */
    private int    mAngleRate   = 4;

    /**
     * 按钮图片
     */
    private Bitmap mBtnImage       = BitmapFactory.decodeResource(getResources(), R.mipmap.btn_rotate);
    /**
     * 按钮图片阴影
     */
    private Bitmap mBtnImageShadow = BitmapFactory.decodeResource(getResources(), R.mipmap.btn_rotate_shadow);

    private OnTempChangeListener mOnTempChangeListener;

    // 以下为旋转按钮相关
    /**
     * 当前按钮旋转的角度
     */
    private float mRotateAngle;
    /**
     * 当前的角度
     */
    private float mCurrentAngle;

    private boolean isDown;
    private boolean isMove;

    public TempControlView(Context context) {
        super(context);
        init();
    }

    public TempControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TempControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDialPaint = new Paint();
        mDialPaint.setAntiAlias(true);
        mDialPaint.setStrokeWidth(dp2px(2));
        mDialPaint.setStyle(Paint.Style.STROKE);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(Color.parseColor("#3CB7EA"));
        mArcPaint.setStrokeWidth(dp2px(2));
        mArcPaint.setStyle(Paint.Style.STROKE);

        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setTextSize(sp2px(15));
        mTitlePaint.setColor(Color.parseColor("#3B434E"));
        mTitlePaint.setStyle(Paint.Style.STROKE);

        mTempFlagPaint = new Paint();
        mTempFlagPaint.setAntiAlias(true);
        mTempFlagPaint.setTextSize(sp2px(25));
        mTempFlagPaint.setColor(Color.parseColor("#E4A07E"));
        mTempFlagPaint.setStyle(Paint.Style.STROKE);

        mBtnPaint = new Paint();
        mTempFlagPaint.setAntiAlias(true);

        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        mTempPaint = new Paint();
        mTempPaint.setAntiAlias(true);
        mTempPaint.setTextSize(sp2px(60));
        mTempPaint.setColor(Color.parseColor("#E27A3F"));
        mTempPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 控件宽、高
        mWidth = mHeight = Math.min(h, w);
        // 刻度盘半径
        mDialRadius = mWidth / 2 - dp2px(20);
        // 圆弧半径
        mArcRadius = mDialRadius - dp2px(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawScale(canvas);
        drawArc(canvas);
        drawText(canvas);
        drawButton(canvas);
        drawTemp(canvas);
    }

    /**
     * 绘制刻度盘
     *
     * @param canvas 同一块画布
     */
    private void drawScale(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        // 逆时针旋转135-2度
        canvas.rotate(-133);
        mDialPaint.setColor(Color.parseColor("#3CB7EA"));
        for (int i = 0; i < 60; i++) {
            canvas.drawLine(0, -mDialRadius, 0, -mDialRadius + mScaleHeight, mDialPaint);
            canvas.rotate(4.5f);
        }

        canvas.rotate(90);
        mDialPaint.setColor(Color.parseColor("#E37364"));
        for (int i = 0; i < (mTemperature - mMinTemp) * mAngleRate; i++) {
            canvas.drawLine(0, -mDialRadius, 0, -mDialRadius + mScaleHeight, mDialPaint);
            canvas.rotate(4.5f);
        }
        canvas.restore();
    }

    /**
     * 绘制刻度盘下的圆弧
     *
     * @param canvas 同一块画布
     */
    private void drawArc(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2); // 把当前画布的原点移到(dx,dy),后面的操作都以(dx,dy)作为参照点，默认原点为(0,0)
        canvas.rotate(135 + 2);
        RectF rectF = new RectF(-mArcRadius, -mArcRadius, mArcRadius, mArcRadius);
        canvas.drawArc(rectF, 0, 265, false, mArcPaint);
        canvas.restore();
    }

    /**
     * 绘制标题与最值温度标识
     *
     * @param canvas 同一块画布
     */
    private void drawText(Canvas canvas) {
        canvas.save();

        // 绘制刻度盘的小标题
        float titleWidth = mTitlePaint.measureText(mTitle);
        canvas.drawText(mTitle, (mWidth - titleWidth) / 2, mDialRadius * 2 + dp2px(15), mTitlePaint);

        // 绘制温度最小值标识
        // 最小温度如小于10，显示0x
        String minTempFlag   = mMinTemp < 10 ? "0" + mMinTemp : mMinTemp + "";
        float  tempFlagWidth = mTitlePaint.measureText(mMaxTemp + "");
        canvas.rotate(55, mWidth / 2, mHeight / 2);
        canvas.drawText(minTempFlag, (mWidth - tempFlagWidth) / 2, mHeight + dp2px(5), mTempFlagPaint);

        // 绘制温度最大值标识
        canvas.rotate(-105, mWidth / 2, mHeight / 2);
        canvas.drawText(mMaxTemp + "", (mWidth - tempFlagWidth) / 2, mHeight + dp2px(5), mTempFlagPaint);

        canvas.restore();
    }

    /**
     * 绘制旋钮
     *
     * @param canvas 同一块画布
     */
    private void drawButton(Canvas canvas) {
        // 按钮宽度和高度
        int btnWidth  = mBtnImage.getWidth();
        int btnHeight = mBtnImage.getHeight();
        // 按钮阴影宽度和高度
        int btnShadowWidth  = mBtnImageShadow.getWidth();
        int btnShadowHeight = mBtnImageShadow.getHeight();

        // 绘制按钮阴影
        canvas.drawBitmap(mBtnImageShadow, (mWidth - btnShadowWidth) / 2, (mHeight - btnShadowHeight) / 2, mBtnPaint);

        Matrix matrix = new Matrix();
        // 设置旋钮位置
        matrix.setTranslate(btnWidth / 2, btnHeight / 2);
        // 设置旋钮角度
        matrix.preRotate(45 + mRotateAngle);
        // 按钮位置还原，此时按钮位置在左上角
        matrix.preTranslate(-btnWidth / 2, -btnHeight / 2);
        // 将按钮移到中心位置
        matrix.postTranslate((mWidth - btnWidth) / 2, (mHeight - btnHeight) / 2);

        // 设置抗锯齿
        canvas.setDrawFilter(mPaintFlagsDrawFilter);
        canvas.drawBitmap(mBtnImage, matrix, mBtnPaint);
    }

    /**
     * 绘制温度显示
     *
     * @param canvas 同一块画布
     */
    private void drawTemp(Canvas canvas) {
        canvas.save();

        canvas.translate(getWidth() / 2, getHeight() / 2);
        float tempTextWidth  = mTempPaint.measureText(mTemperature + "");
        float tempTextHeight = (mTempPaint.ascent() + mTempPaint.descent()) / 2;
        canvas.drawText(mTemperature + "°", -tempTextWidth / 2 - dp2px(5), -tempTextHeight, mTempPaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                float downX = event.getX();
                float downY = event.getY();
                mCurrentAngle = calcAngle(downX, downY);
                break;
            case MotionEvent.ACTION_MOVE:
                isMove = true;
                float targetX;
                float targetY;
                downX = targetX = event.getX();
                downY = targetY = event.getY();
                float angle = calcAngle(targetX, targetY);
                // 滑过的角度增量
                float angleIncreased = angle - mCurrentAngle;
                // 防止越界
                if (angleIncreased < -270) {
                    angleIncreased = angleIncreased + 360;
                } else if (angleIncreased > 270) {
                    angleIncreased = angleIncreased - 360;
                }
                IncreaseAngle(angleIncreased);
                mCurrentAngle = angle;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isDown && isMove) {
                    // 纠正指针位置
                    mRotateAngle = (float) ((mTemperature - mMinTemp) * mAngleRate * 4.5);
                    invalidate();
                    // 回调温度改变监听
                    mOnTempChangeListener.change(mTemperature);
                    isDown = false;
                    isMove = false;
                }
                break;
        }
        return true;
        // return super.onTouchEvent(event);
    }

    /**
     * 以按钮圆心为坐标圆点，建立坐标系，求出(targetX, targetY)坐标与x轴的夹角
     *
     * @param targetX x坐标
     * @param targetY y坐标
     * @return (targetX, targetY)坐标与x轴的夹角
     */
    private float calcAngle(float targetX, float targetY) {
        float  x = targetX - mWidth / 2;
        float  y = targetY - mHeight / 2;
        double radian;
        if (x != 0) {
            float tan = Math.abs(y / x);
            if (x > 0) {
                if (y >= 0) {
                    radian = Math.atan(tan);
                } else {
                    radian = 2 * Math.PI - Math.atan(tan);
                }
            } else {
                if (y >= 0) {
                    radian = Math.PI - Math.atan(tan);
                } else {
                    radian = Math.PI + Math.atan(tan);
                }
            }
        } else {
            if (y > 0) {
                radian = Math.PI / 2;
            } else {
                radian = -Math.PI / 2;
            }
        }
        return (float) ((radian * 180) / Math.PI);
    }

    /**
     * 增加旋转角度
     *
     * @param angle 增加的角度
     */
    private void IncreaseAngle(float angle) {
        mRotateAngle += angle;
        if (mRotateAngle < 0) {
            mRotateAngle = 0;
        } else if (mRotateAngle > 270) {
            mRotateAngle = 270;
        }
        mTemperature = (int) (mRotateAngle / 4.5) / mAngleRate + mMinTemp;
    }

    private int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    private int sp2px(float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getResources().getDisplayMetrics());
    }

    /**
     * 设置温度
     *
     * @param minTemp 最小温度
     * @param maxTemp 最大温度
     * @param temp    设置的温度
     */
    public void setTemp(int minTemp, int maxTemp, int temp) {
        this.mMinTemp = minTemp;
        this.mMaxTemp = maxTemp;
        this.mTemperature = temp;
        this.mAngleRate = 60 / (maxTemp - minTemp);
        mRotateAngle = (float) ((temp - minTemp) * mAngleRate * 4.5);
        invalidate();
    }

    /**
     * 设置监听器
     *
     * @param l
     */
    public void setOnTempChangeListener(OnTempChangeListener l) {
        if (l != null) {
            mOnTempChangeListener = l;
        } else {
            throw new IllegalArgumentException("OnTempChangeListener isn't to be null！");
        }
    }

    /**
     * 温度改变监听接口
     */
    public interface OnTempChangeListener {

        /**
         * 回调方法
         *
         * @param temp 温度
         */
        void change(int temp);

    }
}
