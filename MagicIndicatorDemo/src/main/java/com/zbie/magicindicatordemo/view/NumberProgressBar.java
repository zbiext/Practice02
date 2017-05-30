package com.zbie.magicindicatordemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.zbie.magicindicatordemo.R;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.view
 * 创建时间         2017/05/06 01:00
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class NumberProgressBar extends View {

    /** 进度值最大值 */
    private int mMaxProgress     = 100;
    /** 当前进度值,不能超过进度值最大值 */
    private int mCurrentProgress = 0;

    /** 当前进度值文本之前的进度条颜色 */
    private int   mReachedBarColor;
    /** 当前进度值文本之后的进度条颜色 */
    private int   mUnreachedBarColor;
    /** 当前进度值文本的字体颜色 */
    private int   mTextColor;
    /** 当前进度值文本的字体大小 */
    private float mTextSize;
    /** 当前进度值文本之前的进度条高度 */
    private float mReachedBarHeight;
    /** 当前进度值文本之后的进度条高度 */
    private float mUnreachedBarHeight;

    /** 当前进度值得百分比后缀 */
    private String mSuffix = "%";
    /** 当前进度值得百分比前缀 */
    private String mPrefix = "";

    /** 当前进度值文本的默认颜色 */
    private final int default_text_color = Color.rgb(66, 145, 241);
    // /** 当前进度值文本的默认大小 */
    // private final float default_text_size;
    /** 当前进度值文本字体的默认大小 */
    private final float default_progress_text_size;

    /** 当前进度值文本之前的默认进度条颜色 */
    private final int default_reached_color   = Color.rgb(66, 154, 241);
    /** 当前进度值文本之后的默认进度条颜色 */
    private final int default_unreached_color = Color.rgb(204, 204, 204);

    /** 当前进度值文本之前文本的默认间距 */
    private final float default_progress_text_offset;
    /** 当前进度值文本之前的制度条的高度 */
    private final float default_reached_bar_height;
    /** 当前进度值文本之后的制度条的高度 */
    private final float default_unreached_bar_height;

    public static final String INSTANCE_STATE                = "save_instance";
    public static final String INSTANCE_TEXT_COLOR           = "text_color";
    public static final String INSTANCE_TEXT_SIZE            = "text_size";
    public static final String INSTANCE_REACHED_BAR_HEIGHT   = "reached_bar_height";
    public static final String INSTANCE_REACHED_BAR_COLOR    = "reached_bar_color";
    public static final String INSTANCE_UNREACHED_BAR_HEIGHT = "unreached_bar_height";
    public static final String INSTANCE_UNREACHED_BAR_COLOR  = "unreached_bar_color";
    public static final String INSTANCE_MAX                  = "max";
    public static final String INSTANCE_PROGRESS             = "progress";
    public static final String INSTANCE_SUFFIX               = "suffix";
    public static final String INSTANCE_PREFIX               = "prefix";
    public static final String INSTANCE_TEXT_VISIBILITY      = "text_visibility";

    /** 默认显示当前进度值文本 1为显示,0为不显示 */
    public static final int PROGRESS_TEXT_VISIBLE = 1;

    /** 要绘制的当前进度值的文本的宽度 */
    private float  mDrawtTextWidth;
    /** 要绘制的当前进度值的文本的起始位置 */
    private float  mDrawtTextStart;
    /** 要绘制的当前进度值的文本的结束位置 */
    private float  mDrawtTextEnd;
    /** 要绘制的当前进度值的文本(内容) */
    private String mCurrentDrawtText;
    /** 绘制当前进度值文本之前的进度条的画笔 */
    private Paint  mReachedBarPaint;
    /** 绘制当前进度值文本之后的进度条的画笔 */
    private Paint  mUnreachedBarPaint;
    /** 绘制当前进度值文本的画笔 */
    private Paint  mTextPaint;

    /** 当前进度值之前文本的长方形进度条 */
    private RectF mReachedRectF   = new RectF(0, 0, 0, 0);
    /** 当前进度值之后文本的长方形进度条 */
    private RectF mUnReachedRectF = new RectF(0, 0, 0, 0);
    /** 当前进度值之前文本的间距 */
    private float mOffset;
    /** 是否绘制当前进度值之后的进度条 */
    private boolean mDrawUnreachedBar = true;
    /** 是否绘制当前进度值之前的进度条 */
    private boolean mDrawReachedBar   = true;
    /** 是否绘制当前进度值文本 */
    private boolean mIfDrawText       = true;

    private OnProgressBarListener mListener;

    public enum ProgressTextVisibility {
        Visible, Invisible
    }

    public NumberProgressBar(Context context) {
        this(context, null);
    }

    public NumberProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.numberProgressBarStyle);
    }

    public NumberProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        default_reached_bar_height = dp2px(1.5f);
        default_unreached_bar_height = dp2px(1.0f);
        default_progress_text_size = sp2px(10);
        default_progress_text_offset = dp2px(3.0f);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberProgressBar, defStyleAttr, 0);
        mReachedBarColor = attributes.getColor(R.styleable.NumberProgressBar_progress_reached_color, default_reached_color);
        mUnreachedBarColor = attributes.getColor(R.styleable.NumberProgressBar_progress_unreached_color, default_unreached_color);
        mTextColor = attributes.getColor(R.styleable.NumberProgressBar_progress_text_color, default_text_color);
        mTextSize = attributes.getDimension(R.styleable.NumberProgressBar_progress_text_size, default_progress_text_size);
        mReachedBarHeight = attributes.getDimension(R.styleable.NumberProgressBar_progress_reached_bar_height, default_reached_bar_height);
        mUnreachedBarHeight = attributes.getDimension(R.styleable.NumberProgressBar_progress_unreached_bar_height, default_unreached_bar_height);
        mOffset = attributes.getDimension(R.styleable.NumberProgressBar_progress_text_offset, default_progress_text_offset);
        int textVisible = attributes.getInt(R.styleable.NumberProgressBar_progress_text_visibility, PROGRESS_TEXT_VISIBLE);
        if (textVisible != PROGRESS_TEXT_VISIBLE) {
            mIfDrawText = false;
        }
        setProgress(attributes.getInt(R.styleable.NumberProgressBar_progress_current, 0));
        setMaxProgress(attributes.getInt(R.styleable.NumberProgressBar_progress_max, 100));
        // 回收TypedArray,用于后续调用时可复用.回收到TypedArrayPool池中,以备后用
        attributes.recycle();
        initPainters();
    }

    /** 初始化三种画笔 */
    private void initPainters() {

        mReachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedBarPaint.setColor(mReachedBarColor);
        mUnreachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnreachedBarPaint.setColor(mUnreachedBarColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) mTextSize;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return Math.max((int) mTextSize, Math.max((int) mReachedBarHeight, (int) mUnreachedBarHeight));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode    = MeasureSpec.getMode(measureSpec);
        int size    = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        /** 父决定子的确切大小,子被限定在给定的边界,忽略本身想要的大小 */
        /** 当设置width或height为match_parent时,MeasureSpecMode是EXACTLY,因为子View会占据剩余容器的空间,所以它的大小是确定的 */
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            /** 子View最大可以达到的指定大小 */
            /** 当设置wrap_content时,MeasureSpecMode是AT_MOST,表示子View的大小最多是多少,这样子View会根据这个上限来设置自己的尺寸 */
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIfDrawText) {
            calculateDrawRecF();
        } else {
            calculateDrawRecFWithoutPrgressText();
        }
        if (mDrawReachedBar) {
            //            canvas.drawRect(mReachedRectF, mReachedBarPaint);
            canvas.drawRoundRect(mReachedRectF, 5f, 5f, mReachedBarPaint);
        }
        if (mDrawUnreachedBar) {
            //            canvas.drawRect(mUnReachedRectF, mUnreachedBarPaint);
            canvas.drawRoundRect(mUnReachedRectF, 5f, 5f, mUnreachedBarPaint);
        }
        if (mIfDrawText) {
            canvas.drawText(mCurrentDrawtText, mDrawtTextStart, mDrawtTextEnd, mTextPaint);
        }
    }

    /** 计算要绘制当前进度值文本时,(文本、之前的长方形、之后长方形)图形的各个属性 */
    private void calculateDrawRecF() {
        // 文本内容
        mCurrentDrawtText = mPrefix + String.format("%d", getProgress() * 100 / getMaxProgress()) + mSuffix;
        // 文本的宽度
        mDrawtTextWidth = mTextPaint.measureText(mCurrentDrawtText);

        if (getProgress() == 0) { // 如果当前进度值为0,则不绘制当前进度值之前的进度条
            mDrawReachedBar = false;
            mDrawtTextStart = getPaddingLeft();
        } else { // 否则需要绘制当前进度值之前的进度条
            mDrawReachedBar = true;
            mReachedRectF.left = getPaddingLeft();
            mReachedRectF.top = getHeight() / 2.0f - mReachedBarHeight / 2.0f;
            mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (getMaxProgress() * 1.0f) * getProgress() - mOffset + getPaddingLeft();
            mReachedRectF.bottom = getHeight() / 2.0f + mReachedBarHeight / 2.0f;
            // 当前进度值文本的起始位置
            mDrawtTextStart = mReachedRectF.right + mOffset;
        }
        // 当前进度值文本的结束位置
        mDrawtTextEnd = ((int) (getHeight() / 2.0f)) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2.0f);
        // 如果画不下当前进度值文本,就重新计算下当前进度值的文本起始位置和当前进度值之前的进度条(长方形)的右边
        if (mDrawtTextStart + mDrawtTextWidth >= getWidth() - getPaddingRight()) {
            mDrawtTextStart = getWidth() - getPaddingRight() - mDrawtTextWidth;
            mReachedRectF.right = mDrawtTextStart - mOffset;
        }
        // 当前进度值文本之后的进度值的起始位置
        float unreachedBarStart = mDrawtTextStart + mDrawtTextWidth + mOffset;
        // 如果画不下进度值文本之后的进度条了,就不画进度值之后的进度条
        if (unreachedBarStart >= getWidth() - getPaddingRight()) {
            mDrawUnreachedBar = false;
        } else {
            mDrawUnreachedBar = true;
            // 当前进度值文本之后的进度条(长方形)的属性
            mUnReachedRectF.left = unreachedBarStart;
            mUnReachedRectF.top = getHeight() / 2.0f - mUnreachedBarHeight / 2.0f;
            mUnReachedRectF.right = getWidth() - getPaddingRight();
            mUnReachedRectF.bottom = getHeight() / 2.0f + mUnreachedBarHeight / 2.0f;
        }

    }

    /** 计算不要绘制当前进度值文本时,图形的各个属性 */
    private void calculateDrawRecFWithoutPrgressText() {
        // 不需要画当前进度值文本
        // 当前进度值文本之前的进度条(长方形)的属性
        mReachedRectF.left = getPaddingLeft();
        mReachedRectF.top = getHeight() / 2.0f - mReachedBarHeight / 2.0f;
        mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (getMaxProgress() * 1.0f) * getProgress() + getPaddingLeft();
        mReachedRectF.bottom = getHeight() / 2.0f + mReachedBarHeight / 2.0f;
        // 当前进度值文本之后的进度条(长方形)的属性
        mUnReachedRectF.left = mReachedRectF.right;
        mUnReachedRectF.top = getHeight() / 2.0f - mUnreachedBarHeight / 2.0f;
        mUnReachedRectF.right = getWidth() - getPaddingRight();
        mUnReachedRectF.bottom = getHeight() / 2.0f + mUnreachedBarHeight / 2.0f;
    }

    /**
     * 获得当前进度值文本的字体颜色
     *
     * @return 当前进度值文本的字体颜色
     */
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * 设置当前进度值文本的字体颜色
     *
     * @param textColor 当前进度值文本的字体颜色
     */
    public void setTextColor(int textColor) {
        mTextColor = textColor;
        mTextPaint.setColor(textColor);
        invalidate();
    }

    /**
     * 获得当前进度值文本的字体大小
     *
     * @return 当前进度值文本的字体大小
     */
    public float getTextSize() {
        return mTextSize;
    }

    /**
     * 设置当前进度值文本的字体大小
     *
     * @param textSize 当前进度值文本的字体大小
     */
    public void setTextSize(float textSize) {
        mTextSize = textSize;
        mTextPaint.setTextSize(textSize);
        invalidate();
    }

    /**
     * 获得当前进度值文本之后的进度条颜色
     *
     * @return 当前进度值文本之后的进度条颜色
     */
    public int getUnreachedBarColor() {
        return mUnreachedBarColor;
    }

    /**
     * 设置当前进度值文本之后的进度条颜色
     *
     * @param unreachedBarColor 当前进度值文本之后的进度条颜色
     */
    public void setUnreachedBarColor(int unreachedBarColor) {
        mUnreachedBarColor = unreachedBarColor;
        mUnreachedBarPaint.setColor(unreachedBarColor);
        invalidate();
    }

    /**
     * 获得当前进度值文本之前的进度条颜色
     *
     * @return 当前进度值文本之前的进度条颜色
     */
    public int getReachedBarColor() {
        return mReachedBarColor;
    }

    /**
     * 设置当前进度值文本之前的进度条颜色
     *
     * @param reachedBarColor 当前进度值文本之前的进度条颜色
     */
    public void setReachedBarColor(int reachedBarColor) {
        mReachedBarColor = reachedBarColor;
        mReachedBarPaint.setColor(reachedBarColor);
        invalidate();
    }

    /**
     * 获得进度条的当前进度值
     *
     * @return 进度条的当前进度值
     */
    private int getProgress() {
        return mCurrentProgress;
    }

    /**
     * 设置进度条的当前进度值
     *
     * @param progress 进度条的当前进度值
     */
    public void setProgress(int progress) {
        if (progress <= getMaxProgress() && progress >= 0) {
            mCurrentProgress = progress;
            invalidate(); // 重绘视图
        }
    }

    /**
     * 获得进度条的最大进度值
     *
     * @return 进度条的最大进度值
     */
    private int getMaxProgress() {
        return mMaxProgress;
    }

    /**
     * 设置进度条的最大进度值
     *
     * @param maxProgress 进度条的最大进度值
     */
    public void setMaxProgress(int maxProgress) {
        if (maxProgress > 0) {
            mMaxProgress = maxProgress;
            invalidate();
        }
    }

    /**
     * 获得当前进度值文本之前的进度条高度
     *
     * @return 当前进度值文本之前的进度条高度
     */
    public float getReachedBarHeight() {
        return mReachedBarHeight;
    }

    /**
     * 设置当前进度值文本之前的进度条高度
     *
     * @param reachedBarHeight 当前进度值文本之前的进度条高度
     */
    public void setReachedBarHeight(float reachedBarHeight) {
        mReachedBarHeight = reachedBarHeight;
    }

    /**
     * 获得当前进度值文本之后的进度条高度
     *
     * @return 当前进度值文本之后的进度条高度
     */
    public float getUnreachedBarHeight() {
        return mUnreachedBarHeight;
    }

    /**
     * 设置当前进度值文本之后的进度条高度
     *
     * @param unreachedBarHeight 当前进度值文本之后的进度条高度
     */
    public void setUnreachedBarHeight(float unreachedBarHeight) {
        mUnreachedBarHeight = unreachedBarHeight;
    }

    /**
     * 获得当前进度文本的后缀
     *
     * @return 当前进度文本的后缀
     */
    public String getSuffix() {
        return mSuffix;
    }

    /**
     * 设置当前进度文本的后缀
     *
     * @param suffix 当前进度文本的后缀
     */
    public void setSuffix(String suffix) {
        if (suffix == null) {
            mSuffix = "";
        } else {
            mSuffix = suffix;
        }
    }

    /**
     * 获得当前进度文本的前缀
     *
     * @return 当前进度文本的前缀
     */
    public String getPrefix() {
        return mPrefix;
    }

    /**
     * 设置当前进度文本的前缀
     *
     * @param prefix 当前进度文本的前缀
     */
    public void setPrefix(String prefix) {
        if (prefix == null) {
            mPrefix = "";
        } else {
            mPrefix = prefix;
        }
    }

    /**
     * 设置进度条的步长
     *
     * @param by 步长
     */
    public void incrementProgressBy(int by) {
        if (by > 0) {
            setProgress(getProgress() + by);
        }
        if (mListener != null) {
            mListener.onProgressChange(getProgress(), getMaxProgress());
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize());
        bundle.putFloat(INSTANCE_REACHED_BAR_HEIGHT, getReachedBarHeight());
        bundle.putInt(INSTANCE_REACHED_BAR_COLOR, getReachedBarColor());
        bundle.putFloat(INSTANCE_UNREACHED_BAR_HEIGHT, getUnreachedBarHeight());
        bundle.putInt(INSTANCE_UNREACHED_BAR_COLOR, getUnreachedBarColor());
        bundle.putInt(INSTANCE_MAX, getMaxProgress());
        bundle.putInt(INSTANCE_PROGRESS, getProgress());
        bundle.putString(INSTANCE_SUFFIX, getSuffix());
        bundle.putString(INSTANCE_PREFIX, getPrefix());
        bundle.putBoolean(INSTANCE_TEXT_VISIBILITY, getProgressTextVisibility());
        return bundle;
    }

    public void setProgressTextVisibility(ProgressTextVisibility visibility) {
        mIfDrawText = visibility == ProgressTextVisibility.Visible;
        invalidate();
    }

    public boolean getProgressTextVisibility() {
        return mIfDrawText;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            mTextColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            mTextSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            mReachedBarHeight = bundle.getFloat(INSTANCE_REACHED_BAR_HEIGHT);
            mReachedBarColor = bundle.getInt(INSTANCE_REACHED_BAR_COLOR);
            mUnreachedBarHeight = bundle.getFloat(INSTANCE_UNREACHED_BAR_HEIGHT);
            mUnreachedBarColor = bundle.getInt(INSTANCE_UNREACHED_BAR_COLOR);
            initPainters();
            setMaxProgress(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getInt(INSTANCE_PROGRESS));
            setSuffix(bundle.getString(INSTANCE_SUFFIX));
            setPrefix(bundle.getString(INSTANCE_PREFIX));
        }
        super.onRestoreInstanceState(state);
    }

    /** dp转px */
    private float dp2px(float dpVal) {
        return getResources().getDisplayMetrics().density * dpVal + 0.5f;
    }

    /** sp转px */
    private float sp2px(int spVal) {
        return getResources().getDisplayMetrics().scaledDensity * spVal;
    }

    public void setOnProgressBarListener(OnProgressBarListener l) {
        mListener = l;
    }

    public interface OnProgressBarListener {

        void onProgressChange(int current, int max);
    }
}
