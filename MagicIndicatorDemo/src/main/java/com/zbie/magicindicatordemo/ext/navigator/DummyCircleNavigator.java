package com.zbie.magicindicatordemo.ext.navigator;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import com.zbie.magicindicatordemo.lib.abs.IPagerNavigator;
import com.zbie.magicindicatordemo.lib.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.ext.navigator
 * 创建时间         2017/05/06 01:45
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class DummyCircleNavigator extends View implements IPagerNavigator {

    private int mRadius;
    private int mCircleColor;
    private int mStrokeWidth;
    private int mCircleSpacing;
    private int mCircleCount;

    private int mCurrentIndex;
    private List<PointF> mCirclePoints = new ArrayList<PointF>();
    private Paint        mPaint        = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DummyCircleNavigator(Context context) {
        super(context);
        init(context);
    }

    public DummyCircleNavigator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DummyCircleNavigator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mRadius = UIUtil.dp2px(context, 3);
        mCircleSpacing = UIUtil.dp2px(context, 8);
        mStrokeWidth = UIUtil.dp2px(context, 1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onAttachToMagicIndicator() {

    }

    @Override
    public void onDetachFromMagicIndicator() {

    }

    @Override
    public void notifyDataSetChanged() {

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        prepareCirclePoints();
    }

    private void prepareCirclePoints() {
        mCirclePoints.clear();
        if (mCircleCount > 0) {
            int halfHeight    = getHeight() / 2;
            int measureWidth  = mCircleCount * mRadius * 2 + (mCircleCount - 1) * mCircleSpacing;
            int centerSpacing = mRadius * 2 + mCircleSpacing;
            int startX        = (getWidth() - measureWidth) / 2 + mRadius;
            for (int i = 0; i < mCircleCount; i++) {
                PointF pointF = new PointF(startX, halfHeight);
                mCirclePoints.add(pointF);
                startX += centerSpacing;
            }
        }
    }
}
