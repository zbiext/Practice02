package com.zbie.magicindicatordemo.lib.imp.commonnavigator.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zbie.magicindicatordemo.lib.imp.commonnavigator.abs.IPagerIndicator;
import com.zbie.magicindicatordemo.lib.imp.commonnavigator.model.PositionData;

import java.util.List;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.lib.imp.commonnavigator.indicators
 * 创建时间         2017/05/06 01:15
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            用于测试的指示器,可用来检测自定义的IMeasurablePagerTitleView是否正确测量内容区域
 */
public class TestPagerIndicator extends View implements IPagerIndicator {

    private Paint mPaint;
    private int   mOutRectColor;
    private int   mInnerRectColor;
    private RectF mOutRect   = new RectF();
    private RectF mInnerRect = new RectF();

    private List<PositionData> mPositionDataList;

    public TestPagerIndicator(Context context) {
        super(context);
        init(context);
    }

    public TestPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mOutRectColor = Color.RED;
        mInnerRectColor = Color.GREEN;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mOutRectColor);
        canvas.drawRect(mOutRect, mPaint);
        mPaint.setColor(mInnerRectColor);
        canvas.drawRect(mInnerRect, mPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }
        // 计算锚点位置
        int          currentPosition = Math.min(mPositionDataList.size() - 1, position);
        int          nextPosition    = Math.min(mPositionDataList.size() - 1, position + 1);
        PositionData current         = mPositionDataList.get(currentPosition);
        PositionData next            = mPositionDataList.get(nextPosition);

        mOutRect.left = current.mLeft + (next.mLeft - current.mLeft) * positionOffset;
        mOutRect.top = current.mTop + (next.mTop - current.mTop) * positionOffset;
        mOutRect.right = current.mRight + (next.mRight - current.mRight) * positionOffset;
        mOutRect.bottom = current.mBottom + (next.mBottom - current.mBottom) * positionOffset;

        mInnerRect.left = current.mContentLeft + (next.mContentLeft - current.mContentLeft) * positionOffset;
        mInnerRect.top = current.mContentTop + (next.mContentTop - current.mContentTop) * positionOffset;
        mInnerRect.right = current.mContentRight + (next.mContentRight - current.mContentRight) * positionOffset;
        mInnerRect.bottom = current.mContentBottom + (next.mContentBottom - current.mContentBottom) * positionOffset;

        invalidate();
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mPositionDataList = dataList;
    }

    public int getOutRectColor() {
        return mOutRectColor;
    }

    public void setOutRectColor(int outRectColor) {
        mOutRectColor = outRectColor;
    }

    public int getInnerRectColor() {
        return mInnerRectColor;
    }

    public void setInnerRectColor(int innerRectColor) {
        mInnerRectColor = innerRectColor;
    }
}
