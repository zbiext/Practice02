package com.zbie.other.style;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateInterpolator;

import com.zbie.other.view.ProgressView;

/**
 * Created by 涛 on 2017/5/5 0005.
 * 项目名           Practice02
 * 包名             com.zbie.other.style
 * 创建时间         2017/05/05 23:47
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class LinearProgressDrawable extends Drawable implements Animatable {

    private static final String TAG = "LinearProgressDrawable";

    private int mProgressMode;

    private final Paint mPaint;
    private final Path  mPath;
    private final Paint lpaint;

    private float mProgressPercent;
    private float mSecondaryProgressPercent;
    private float mStartLine;
    private float mLineWidth;

    private ValueAnimator valueAnimator;

    private boolean isRunningFlag = false;

    public LinearProgressDrawable() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mPath = new Path();
        lpaint = new Paint();
        lpaint.setAlpha(125);
    }

    @Override
    public void draw(Canvas canvas) {
        switch (mProgressMode) {
            case ProgressView.MODE_DETERMINATE:
                drawDeterminate(canvas);
                break;
            case ProgressView.MODE_INDETERMINATE:
                drawIndeterminate(canvas);
                break;
        }
    }

    private void drawDeterminate(Canvas canvas) {
        Rect  bounds = getBounds();
        int   width  = bounds.width();
        float y      = bounds.height() / 2;
        float size   = 12f;

        float lineWidth       = width * mProgressPercent;
        float secondLineWidth = width * mSecondaryProgressPercent;
        // Log.i(TAG, "mProgressPercent:" + mProgressPercent + " mSecondaryProgressPercent:" + mSecondaryProgressPercent);
        mPaint.setStrokeWidth(size);
        mPaint.setStyle(Paint.Style.STROKE);

        if (mProgressPercent == 0) {
            start();
        }
        if (mProgressPercent == 1) {
            stop();
        }
        // 绘制进度条底色
        if (mProgressPercent != 1f) {
            mPaint.setColor(Color.parseColor("#E3F2FD"));
            canvas.drawLine(lineWidth, y, width, y, mPaint);
        }
        // 绘制较快的进度条
        if (mSecondaryProgressPercent != 0f) {
            mPaint.setColor(Color.parseColor("#BBDEFB"));
            drawLinePath(canvas, 0, y, secondLineWidth, y, mPaint);
        }
        // 绘制较慢的进度条
        if (mProgressPercent != 0f) {
            mPaint.setColor(Color.parseColor("#2196F3"));
            drawLinePath(canvas, 0, y, lineWidth, y, mPaint);
        }
        // 绘制最上面闪烁的进度
        if (mProgressPercent != 1f && mProgressPercent != 0f) {
            lpaint.setStrokeWidth(size);
            lpaint.setStyle(Paint.Style.STROKE);
            lpaint.setAlpha(160);
            float          endline  = Math.min(lineWidth, mStartLine + width / 8);
            float          statline = Math.min(mStartLine, endline);
            LinearGradient gradient = new LinearGradient(statline, y, endline, y, Color.parseColor("#2196F3"), Color.WHITE, Shader.TileMode.REPEAT);
            lpaint.setShader(gradient);
            drawLinePath(canvas, statline, y, endline, y, lpaint);
        }
    }

    private void drawIndeterminate(Canvas canvas) {
        Rect  bounds = getBounds();
        int   width  = bounds.width();
        float y      = bounds.height() / 2;
        float size   = 12f;

        mPaint.setStrokeWidth(size);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);

        canvas.drawLine(0, y, width, y, mPaint);

        mPaint.setColor(Color.RED);
        mLineWidth = width / 6;
        float endline = Math.min(width, mStartLine + mLineWidth);
        drawLinePath(canvas, mStartLine, y, endline, y, mPaint);
    }

    private void drawLinePath(Canvas canvas, float x1, float y1, float x2, float y2, Paint paint) {
        mPath.reset();
        mPath.moveTo(x1, y1); // 线条的起点
        mPath.lineTo(x2, y2); // 线条的终点
        canvas.drawPath(mPath, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public int getProgress() {
        return (int) (mProgressPercent * 100);
    }

    public void setProgress(int progress) {
        float fpercent = Math.min(1f, Math.max(0f, progress / 100f));
        if (mProgressPercent != fpercent) {
            mProgressPercent = fpercent;
            invalidateSelf();
        }
    }

    public int getSecondaryProgress() {
        return (int) (mSecondaryProgressPercent * 100);
    }

    public void setSecondaryProgress(int progress) {
        float fpercent = Math.min(1f, Math.max(0f, progress / 100f));
        if (mSecondaryProgressPercent != fpercent) {
            mSecondaryProgressPercent = fpercent;
            invalidateSelf();
        }
    }

    public void setProgressMode(int progressMode) {
        if (mProgressMode != progressMode) {
            mProgressMode = progressMode;
            invalidateSelf();
        }
    }

    @Override
    public void start() {
        if (isRunning()) {
            return;
        }
        isRunningFlag = true;
        valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setDuration(800);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mStartLine = getBounds().width() * ((float) valueAnimator.getAnimatedValue());
                invalidateSelf();
            }
        });
        valueAnimator.start();
    }

    @Override
    public void stop() {
        if (valueAnimator != null && isRunning()) {
            valueAnimator.cancel();
        }
        isRunningFlag = false;
    }

    @Override
    public boolean isRunning() {
        return isRunningFlag;
    }
}
