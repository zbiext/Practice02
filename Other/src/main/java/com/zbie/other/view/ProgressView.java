package com.zbie.other.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zbie.other.R;
import com.zbie.other.style.CircularProgressDrawable;
import com.zbie.other.style.LinearProgressDrawable;
import com.zbie.other.style.WaveProgressDrawable;

/**
 * Created by 涛 on 2017/5/5 0005.
 * 项目名           Practice02
 * 包名             com.zbie.other.view
 * 创建时间         2017/05/05 23:45
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class ProgressView extends View {

    private static final String TAG = "ProgressView";

    public static final int MODE_DETERMINATE   = 0;// 有准确进度的
    public static final int MODE_INDETERMINATE = 1;// 没有准确进度

    public static final String STYLE_CIRCULAR = "circular";// 圆形进度条
    public static final String STYLE_LINEAR   = "linear";// 直线型进度条
    public static final String STYLE_WAVE     = "wave";// 波浪型进度条

    private Drawable progressDrawable;
    private String   progressStyle;
    private int      progressMode;

    public ProgressView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        applyStyle(context, attrs, defStyleAttr, 0);
    }

    private void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        int progress          = 0;
        int secondaryProgress = 0;
        // 读取xml中传入的样式
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressView, defStyleAttr, defStyleRes);
        for (int i = 0, count = a.getIndexCount(); i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ProgressView_pv_mode) {
                progressMode = a.getInteger(attr, 0);
            } else if (attr == R.styleable.ProgressView_pv_progress) {
                progress = a.getInteger(attr, 0);
            } else if (attr == R.styleable.ProgressView_pv_secondary_progress) {
                secondaryProgress = a.getInteger(attr, 0);
            } else if (attr == R.styleable.ProgressView_pv_style) {
                progressStyle = a.getString(attr);
            }
        }
        a.recycle();
        // 设置初始样式
        if (progressDrawable == null) {
            if (STYLE_CIRCULAR.equals(progressStyle)) {
                progressDrawable = new CircularProgressDrawable();
            } else if (STYLE_LINEAR.equals(progressStyle)) {
                progressDrawable = new LinearProgressDrawable();
            } else if (STYLE_WAVE.equals(progressStyle)) {
                progressDrawable = new WaveProgressDrawable();
            } else {
                throw new IllegalArgumentException("invalid  progressStyle, progressStyle:" + progressStyle);
            }
            // 关联View和Drawable对象
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(progressDrawable);
            } else {
                setBackgroundDrawable(progressDrawable);
            }
        }
        // 设置初始模式
        if (STYLE_CIRCULAR.equals(progressStyle)) {
            ((CircularProgressDrawable) progressDrawable).setProgressMode(progressMode);
        } else if (STYLE_LINEAR.equals(progressStyle)) {
            ((LinearProgressDrawable) progressDrawable).setProgressMode(progressMode);
        } else if (STYLE_WAVE.equals(progressStyle)) {
            ((WaveProgressDrawable) progressDrawable).setProgressMode(progressMode);
        }
        // 设置初始进度
        if (progress >= 0) {
            setProgress(progress);
        }
        if (secondaryProgress >= 0) {
            setSecondaryProgress(secondaryProgress);
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Log.i(TAG, "onVisibilityChanged is executed");
        if (changedView != this) {
            return;
        }
        if (visibility == View.VISIBLE) {
            start();
        } else {
            setProgress(0);
            setSecondaryProgress(0);
            stop();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG, "onDetachedFromWindow is executed");
        stop();
    }

    private void start() {
        if (progressDrawable != null) {
            ((Animatable) progressDrawable).start();
        }
    }

    private void stop() {
        if (progressDrawable != null) {
            ((Animatable) progressDrawable).stop();
        }
    }

    public int getProgress() {
        if (STYLE_CIRCULAR.equals(progressStyle)) {
            return ((CircularProgressDrawable) progressDrawable).getProgress();
        } else if (STYLE_LINEAR.equals(progressStyle)) {
            return ((LinearProgressDrawable) progressDrawable).getProgress();
        } else if (STYLE_WAVE.equals(progressStyle)) {
            return ((WaveProgressDrawable) progressDrawable).getProgress();
        } else {
            return 0;
        }
    }

    public void setProgress(int percent) {
        if (STYLE_CIRCULAR.equals(progressStyle)) {
            ((CircularProgressDrawable) progressDrawable).setProgress(percent);
        } else if (STYLE_LINEAR.equals(progressStyle)) {
            ((LinearProgressDrawable) progressDrawable).setProgress(percent);
        } else if (STYLE_WAVE.equals(progressStyle)) {
            ((WaveProgressDrawable) progressDrawable).setProgress(percent);
        }
    }

    public int getSecondaryProgress() {
        if (STYLE_CIRCULAR.equals(progressStyle)) {
            return ((CircularProgressDrawable) progressDrawable).getSecondaryProgress();
        } else if (STYLE_LINEAR.equals(progressStyle)) {
            return ((LinearProgressDrawable) progressDrawable).getSecondaryProgress();
        } else if (STYLE_WAVE.equals(progressStyle)) {
            return ((WaveProgressDrawable) progressDrawable).getSecondaryProgress();
        } else {
            return 0;
        }
    }

    public void setSecondaryProgress(int percent) {
        if (STYLE_CIRCULAR.equals(progressStyle)) {
            ((CircularProgressDrawable) progressDrawable).setSecondaryProgress(percent);
        } else if (STYLE_LINEAR.equals(progressStyle)) {
            ((LinearProgressDrawable) progressDrawable).setSecondaryProgress(percent);
        } else if (STYLE_WAVE.equals(progressStyle)) {
            ((WaveProgressDrawable) progressDrawable).setSecondaryProgress(percent);
        }
    }
}
