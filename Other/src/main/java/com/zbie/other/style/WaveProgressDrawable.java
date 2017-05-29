package com.zbie.other.style;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateInterpolator;

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

public class WaveProgressDrawable extends Drawable implements Animatable {

    private int mProgressMode;

    private final Paint mPaint;
    private final Path  mPath;
    private final Paint lpaint;

    private float mProgressPercent;
    private float mSecondaryProgressPercent;

    private ValueAnimator valueAnimator;

    private boolean isRunningFlag = false;

    public WaveProgressDrawable() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPath = new Path();
        lpaint = new Paint();
        lpaint.setAlpha(125);
    }

    @Override
    public void draw(Canvas canvas) {

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
                //mStartLine = getBounds().width() * ((float) valueAnimator.getAnimatedValue());
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
