package com.zbie.magicindicatordemo.lib.imp.commonnavigator.titles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.zbie.magicindicatordemo.lib.imp.commonnavigator.abs.IPagerTitleView;
import com.zbie.magicindicatordemo.lib.util.UIUtil;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.lib.imp.commonnavigator.titles
 * 创建时间         2017/05/06 01:24
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            类似今日头条切换效果的指示器标题
 */
public class ClipPagerTitleView extends View implements IPagerTitleView {

    private String  mText;
    private int     mTextColor;
    private int     mClipColor;
    private boolean mLeftToRight;
    private float   mClipPercent;

    private Paint mPaint;
    private Rect mTextBounds = new Rect();

    public ClipPagerTitleView(Context context) {
        super(context);
        init(context);
    }

    public ClipPagerTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ClipPagerTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        int textSize = UIUtil.dp2px(context, 16);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(textSize);
        int padding = UIUtil.dp2px(context, 10);
        setPadding(padding, 0, padding, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureTextBounds();
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private void measureTextBounds() {
        mPaint.getTextBounds(mText, 0, mText == null ? 0 : mText.length(), mTextBounds);
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode   = MeasureSpec.getMode(widthMeasureSpec);
        int size   = MeasureSpec.getSize(widthMeasureSpec);
        int result = size;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                int width = mTextBounds.width() + getPaddingLeft() + getPaddingRight();
                result = Math.min(width, size);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = mTextBounds.width() + getPaddingLeft() + getPaddingRight();
                break;
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode   = MeasureSpec.getMode(heightMeasureSpec);
        int size   = MeasureSpec.getSize(heightMeasureSpec);
        int result = size;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                int height = mTextBounds.height() + getPaddingTop() + getPaddingBottom();
                result = Math.min(height, size);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = mTextBounds.height() + getPaddingTop() + getPaddingBottom();
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int x = (getWidth() - mTextBounds.width()) / 2;
        int y = (getHeight() + mTextBounds.height()) / 2;
        // 画底层
        mPaint.setColor(mTextColor);
        canvas.drawText(mText, x, y, mPaint);
        // 画clip层
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        if (mLeftToRight) {
            canvas.clipRect(0, 0, getWidth() * mClipPercent, getHeight());
        } else {
            canvas.clipRect(getWidth() * (1 - mClipPercent), 0, getWidth(), getHeight());
        }
        mPaint.setColor(mClipColor);
        canvas.drawText(mText, x, y, mPaint);
        canvas.restore();
    }

    @Override
    public void onSelected(int index, int totalCount) {

    }

    @Override
    public void onDeselected(int index, int totalCount) {

    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        mLeftToRight = !leftToRight;
        mClipPercent = 1.0f - leavePercent;
        invalidate();
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        mLeftToRight = leftToRight;
        mClipPercent = enterPercent;
        invalidate();
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public float getTextSize() {
        return mPaint.getTextSize();
    }

    public void setTextSize(float textSize) {
        mPaint.setTextSize(textSize);
        requestLayout();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        invalidate();
    }

    public int getClipColor() {
        return mClipColor;
    }

    public void setClipColor(int clipColor) {
        mClipColor = clipColor;
        invalidate();
    }
}
