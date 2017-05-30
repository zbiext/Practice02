package com.zbie.magicindicatordemo.ext.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zbie.magicindicatordemo.lib.imp.commonnavigator.abs.IPagerIndicator;
import com.zbie.magicindicatordemo.lib.imp.commonnavigator.model.PositionData;
import com.zbie.magicindicatordemo.lib.util.UIUtil;

import java.util.List;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.ext.indicators
 * 创建时间         2017/05/06 01:44
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            非手指跟随的小圆点指示器
 */
public class DotPagerIndicator extends View implements IPagerIndicator {

    private List<PositionData> mDataList;
    private float              mRadius;
    private float              mYOffset;
    private int                mDotColor;

    private float mCircleCenterX;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DotPagerIndicator(Context context) {
        super(context);
        init(context);
    }

    public DotPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mRadius = UIUtil.dp2px(context, 3);
        mYOffset = UIUtil.dp2px(context, 3);
        mDotColor = Color.WHITE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mDotColor);
        canvas.drawCircle(mCircleCenterX, getHeight() - mYOffset - mRadius, mRadius, mPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mDataList == null || mDataList.isEmpty()) {
            return;
        }
        PositionData data = mDataList.get(position);
        mCircleCenterX = data.mLeft + data.width() / 2;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mDataList = dataList;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
        invalidate();
    }

    public float getYOffset() {
        return mYOffset;
    }

    public void setYOffset(float YOffset) {
        mYOffset = YOffset;
        invalidate();
    }

    public int getDotColor() {
        return mDotColor;
    }

    public void setDotColor(int dotColor) {
        mDotColor = dotColor;
        invalidate();
    }
}
