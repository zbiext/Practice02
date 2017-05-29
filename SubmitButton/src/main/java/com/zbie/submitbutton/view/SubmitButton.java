package com.zbie.submitbutton.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.zbie.submitbutton.R;

/**
 * Created by 涛 on 2017/5/5 0005.
 * 项目名           Practice02
 * 包名             com.zbie.submitbutton.view
 * 创建时间         2017/05/05 23:01
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class SubmitButton extends View {

    private static final String TAG = "SubmitButton";

    private static final int STATE_NONE    = 0;
    private static final int STATE_SUBMIT  = 1;
    private static final int STATE_LOADING = 2;
    private static final int STATE_RESULT  = 3;

    private static final int STYLE_LOADING  = 0;
    private static final int STYLE_PROGRESS = 1;

    //view状态
    private int mViewState = STATE_NONE;

    //View宽高
    private int mWidth;
    private int mHeight;

    private int MAX_WIDTH;
    private int MAX_HEIGHT;

    //画布坐标原点
    private int x;
    private int y;

    private String mButtonText = "";
    private int mButtonColor;
    private int mSubmitSucceedColor;
    private int mSubmitFailedColor;
    private int mButtonTextSize;

    //view加载等待模式
    private int mProgressStyle = STYLE_LOADING;
    private float mCurrentProgress;
    private Paint mBgPaint;
    private Paint mLoadingPaint;
    private Paint mResultPaint;
    private Paint mTextPaint;

    //文本宽高
    private int mTextWidth;
    private int mTextHeight;

    private Path        mButtonPath;
    private Path        mLoadPath;
    private Path        mResultPath;
    private Path        mDst;
    private RectF       mCircleMid;
    private RectF       mCircleLeft;
    private RectF       mCircleRight;
    private PathMeasure mPathMeasure;

    private float loadValue;

    private ValueAnimator mSubmitAnim;
    private ValueAnimator mLoadingAnim;
    private ValueAnimator mResultAnim;

    private boolean isDoResult;
    private boolean isSucceed;

    public SubmitButton(Context context) {
        this(context, null);
    }

    public SubmitButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubmitButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SubmitButton);
        if (typedArray.getString(R.styleable.SubmitButton_buttonText) != null) {
            mButtonText = typedArray.getString(R.styleable.SubmitButton_buttonText);
        }
        mButtonColor = typedArray.getColor(R.styleable.SubmitButton_buttonColor, Color.parseColor("#19CC95"));
        mSubmitSucceedColor = typedArray.getColor(R.styleable.SubmitButton_submitsucceedColor, Color.parseColor("#19CC95"));
        mSubmitFailedColor = typedArray.getColor(R.styleable.SubmitButton_submitfailedColor, Color.parseColor("#FC8E34"));
        mButtonTextSize = (int) typedArray.getDimension(R.styleable.SubmitButton_buttonTextSize, sp2px(15));
        mProgressStyle = typedArray.getInt(R.styleable.SubmitButton_progressStyle, STYLE_LOADING);
        typedArray.recycle();
        init();
    }

    /**
     * 初始化Paint、Path
     */
    private void init() {
        mBgPaint = new Paint();
        mBgPaint.setColor(mButtonColor);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(5);
        mBgPaint.setAntiAlias(true);

        mLoadingPaint = new Paint();
        mLoadingPaint.setColor(mButtonColor);
        mLoadingPaint.setStyle(Paint.Style.STROKE);
        mLoadingPaint.setStrokeWidth(9);
        mLoadingPaint.setAntiAlias(true);

        mResultPaint = new Paint();
        mResultPaint.setColor(Color.WHITE);
        mResultPaint.setStyle(Paint.Style.STROKE);
        mResultPaint.setStrokeWidth(9);
        mResultPaint.setStrokeCap(Paint.Cap.ROUND);
        mResultPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(mButtonColor);
        mTextPaint.setTextSize(mButtonTextSize);
        mTextPaint.setStrokeWidth(mButtonTextSize / 6);
        mTextPaint.setAntiAlias(true);

        mButtonPath = new Path();
        mLoadPath = new Path();
        mResultPath = new Path();
        mDst = new Path();
        mCircleMid = new RectF();
        mCircleLeft = new RectF();
        mCircleRight = new RectF();
        mPathMeasure = new PathMeasure();

        mTextWidth = getTextWidth(mTextPaint, mButtonText);
        mTextHeight = getTextHeight(mTextPaint, mButtonText);
    }

    /**
     * 获取Text高度
     *
     * @param paint
     * @param text  文本内容
     * @return
     */
    private int getTextWidth(Paint paint, String text) {
        int widthTmp = 0;
        if (text != null && text.length() > 0) {
            int     len    = text.length();
            float[] widths = new float[len];
            paint.getTextWidths(text, widths);
            for (int i = 0; i < len; i++) {
                widthTmp += Math.ceil(widths[i]);
            }
        }
        return widthTmp;
    }

    /**
     * 获取Text宽度
     *
     * @param paint
     * @param text  文本内容
     * @return
     */
    private int getTextHeight(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode  = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize  = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mTextWidth + 100;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = (int) (mTextHeight * 2.5);
        }

        if (heightSize > widthSize) {
            heightSize = (int) (widthSize * 0.25);
        }

        mWidth = widthSize - 10;
        mHeight = heightSize - 10;
        x = (int) (widthSize * 0.5);
        y = (int) (heightSize * 0.5);
        MAX_WIDTH = mWidth;
        MAX_HEIGHT = mHeight;

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(x, y);
        drawButton(canvas);
        switch (mViewState) {
            case STATE_NONE:
            case STATE_SUBMIT:
                if (mWidth > mTextWidth) {
                    drawButtonText(canvas);
                }
                break;
            case STATE_LOADING:
                drawLoading(canvas);
                break;
            case STATE_RESULT:
                drawResult(canvas, isSucceed);
                break;
        }
        //        if (mViewState == STATE_NONE || mViewState == STATE_SUBMIT && mWidth > mTextWidth) {
        //            drawButtonText(canvas);
        //        }
        //        if (mViewState == STATE_LOADING) {
        //            drawLoading(canvas);
        //        }
        //        if (mViewState == STATE_RESULT) {
        //            drawResult(canvas, isSucceed);
        //        }
    }

    /**
     * 绘制初始状态Button
     *
     * @param canvas 画布
     */
    private void drawButton(Canvas canvas) {
        mButtonPath.reset();
        mCircleLeft.set(-mWidth / 2, -mHeight / 2, -mWidth / 2 + mHeight, mHeight / 2);
        mButtonPath.arcTo(mCircleLeft, 90, 180);
        mButtonPath.lineTo(mWidth / 2 - mHeight / 2, -mHeight / 2);
        mCircleRight.set(mWidth / 2 - mHeight, -mHeight / 2, mWidth / 2, mHeight / 2);
        mButtonPath.arcTo(mCircleRight, 270, 180);
        mButtonPath.lineTo(-mWidth / 2 + mHeight / 2, mHeight / 2);
        canvas.drawPath(mButtonPath, mBgPaint);
    }

    /**
     * 绘制Button文本
     *
     * @param canvas 画布
     */
    private void drawButtonText(Canvas canvas) {
        mTextPaint.setAlpha(((mWidth - mTextWidth) * 255) / (MAX_WIDTH - mTextWidth));
        canvas.drawText(mButtonText, -mTextWidth / 2, getTextBaseLineOffset(), mTextPaint);
    }

    /**
     * 计算水平居中的baseline
     *
     * @return
     */
    private float getTextBaseLineOffset() {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        return -(fm.bottom + fm.top) / 2;
    }

    /**
     * 绘制加载状态的Button
     *
     * @param canvas 画布
     */
    private void drawLoading(Canvas canvas) {
        mDst.reset();
        mCircleMid.set(-MAX_HEIGHT / 2, -MAX_HEIGHT / 2, MAX_HEIGHT / 2, MAX_HEIGHT / 2);
        mLoadPath.addArc(mCircleMid, 270, 359.999f);
        mPathMeasure.setPath(mLoadPath, true);
        float startD = 0f, stopD;
        if (mProgressStyle == STYLE_LOADING) {
            startD = mPathMeasure.getLength() * loadValue;
            stopD = startD + mPathMeasure.getLength() / 2 * loadValue;
        } else {
            stopD = mPathMeasure.getLength() * mCurrentProgress;
        }
        mPathMeasure.getSegment(startD, stopD, mDst, true);
        canvas.drawPath(mDst, mLoadingPaint);
    }

    /**
     * 绘制结果状态的Button
     *
     * @param canvas    画布
     * @param isSucceed
     */
    private void drawResult(Canvas canvas, boolean isSucceed) {
        if (isSucceed) {
            mResultPath.moveTo(-mHeight / 6, 0);
            mResultPath.lineTo(0, (float) (-mHeight / 6 + (1 + Math.sqrt(5)) * mHeight / 12));
            mResultPath.lineTo(mHeight / 6, -mHeight / 6);
        } else {
            mResultPath.moveTo(-mHeight / 6, mHeight / 6);
            mResultPath.lineTo(mHeight / 6, -mHeight / 6);
            mResultPath.moveTo(-mHeight / 6, -mHeight / 6);
            mResultPath.lineTo(mHeight / 6, mHeight / 6);
        }
        canvas.drawPath(mResultPath, mResultPaint);
    }

    /**
     * 开始'提交'动画
     */
    private void startSubmitAnim() {
        mViewState = STATE_SUBMIT;
        mSubmitAnim = new ValueAnimator().ofInt(MAX_WIDTH, MAX_HEIGHT);
        mSubmitAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWidth = (int) animation.getAnimatedValue();
                if (mWidth == mHeight) {
                    mBgPaint.setColor(Color.parseColor("#DDDDDD"));
                }
                invalidate();
            }
        });
        mSubmitAnim.setDuration(300);
        mSubmitAnim.setInterpolator(new AccelerateInterpolator());
        mSubmitAnim.start();
        mSubmitAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isDoResult) {
                    startResultAnim();
                } else {
                    startLoadingAnim();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * 开始'加载'动画
     */
    private void startLoadingAnim() {
        mViewState = STATE_LOADING;
        if (mProgressStyle == STYLE_PROGRESS) {
            return;
        }
        mLoadingAnim = new ValueAnimator().ofFloat(0.0f, 1.0f);
        mLoadingAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                loadValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mLoadingAnim.setDuration(2000);
        mLoadingAnim.setRepeatCount(ValueAnimator.INFINITE);
        mLoadingAnim.start();
    }

    /**
     * 开始'结果'动画
     */
    private void startResultAnim() {
        mViewState = STATE_RESULT;
        if (mLoadingAnim != null) {
            mLoadingAnim.cancel();
        }
        mResultAnim = new ValueAnimator().ofInt(MAX_HEIGHT, MAX_WIDTH);
        mResultAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWidth = (int) animation.getAnimatedValue();
                mResultPaint.setAlpha(((mWidth - mHeight) * 255) / (MAX_WIDTH - MAX_HEIGHT));
                if (mWidth == mHeight) {
                    if (isSucceed) {
                        mBgPaint.setColor(mSubmitSucceedColor);
                    } else {
                        mBgPaint.setColor(mSubmitFailedColor);
                    }
                    mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                }
                invalidate();
            }
        });
        mResultAnim.setDuration(300);
        mResultAnim.setInterpolator(new AccelerateInterpolator());
        mResultAnim.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mViewState == STATE_NONE) {
                    startSubmitAnim();
                }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置submit结果
     *
     * @param isSucceed 是否成功
     */
    public void doResult(boolean isSucceed) {
        if (mViewState == STATE_NONE || mViewState == STATE_RESULT || isDoResult) {
            Log.d(TAG, "doResult: mViewState = " + mViewState);
            return;
        }
        isDoResult = true;
        this.isSucceed = isSucceed;
        if (mViewState == STATE_LOADING) {
            startResultAnim();
        }
    }

    /**
     * 恢复初始化Button状态
     */
    public void reset() {
        if (mSubmitAnim != null) {
            mSubmitAnim.cancel();
        }
        if (mLoadingAnim != null) {
            mLoadingAnim.cancel();
        }
        if (mResultAnim != null) {
            mResultAnim.cancel();
        }
        mViewState = STATE_NONE;
        mWidth = MAX_WIDTH;
        mHeight = MAX_HEIGHT;
        isSucceed = false;
        isDoResult = false;
        mCurrentProgress = 0;
        init();
        invalidate();
    }

    /**
     * 设置进度
     *
     * @param progress 进度值 (0-100)
     */
    public void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            return;
        }
        mCurrentProgress = (float) (progress * 0.01);
        if (mProgressStyle == STYLE_PROGRESS && mViewState == STATE_LOADING) {
            invalidate();
        }
    }


    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    private int px2dp(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    private int px2sp(float pxValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
