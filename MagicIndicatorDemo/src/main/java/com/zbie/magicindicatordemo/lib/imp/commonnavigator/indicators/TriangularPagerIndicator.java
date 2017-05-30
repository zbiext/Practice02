package com.zbie.magicindicatordemo.lib.imp.commonnavigator.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.zbie.magicindicatordemo.lib.imp.commonnavigator.abs.IPagerIndicator;
import com.zbie.magicindicatordemo.lib.imp.commonnavigator.model.PositionData;
import com.zbie.magicindicatordemo.lib.util.UIUtil;

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
 * 描述            带有小尖角的直线指示器
 */
public class TriangularPagerIndicator extends View implements IPagerIndicator {

    private List<PositionData> mPositionDataList;
    private Paint              mPaint;
    private int                mLineHeight;
    private int                mLineColor;
    private int                mTriangleHeight;
    private int                mTriangleWidth;

    private Path         mPath              = new Path();
    private Interpolator mStartInterpolator = new LinearInterpolator();
    private float mAnchorX;

    public TriangularPagerIndicator(Context context) {
        super(context);
        init(context);
    }

    public TriangularPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangularPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mLineHeight = UIUtil.dp2px(context, 3);
        mTriangleWidth = UIUtil.dp2px(context, 14);
        mTriangleHeight = UIUtil.dp2px(context, 8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mLineColor);
        canvas.drawRect(0, getHeight() - mLineHeight, getWidth(), getHeight(), mPaint);
        mPath.reset();
        mPath.moveTo(mAnchorX - mTriangleWidth / 2, getHeight());
        mPath.lineTo(mAnchorX, getHeight() - mTriangleHeight);
        mPath.lineTo(mAnchorX + mTriangleWidth / 2, getHeight());
        mPath.close();
        canvas.drawPath(mPath, mPaint);
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

        float leftX  = current.mLeft + (current.mRight - current.mLeft) / 2;
        float rightX = next.mLeft + (next.mRight - next.mLeft) / 2;

        mAnchorX = leftX + (rightX - leftX) * mStartInterpolator.getInterpolation(positionOffset);

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

    public int getLineHeight() {
        return mLineHeight;
    }

    public void setLineHeight(int lineHeight) {
        mLineHeight = lineHeight;
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int lineColor) {
        mLineColor = lineColor;
    }

    public int getTriangleHeight() {
        return mTriangleHeight;
    }

    public void setTriangleHeight(int triangleHeight) {
        mTriangleHeight = triangleHeight;
    }

    public int getTriangleWidth() {
        return mTriangleWidth;
    }

    public void setTriangleWidth(int triangleWidth) {
        mTriangleWidth = triangleWidth;
    }

    public Interpolator getStartInterpolator() {
        return mStartInterpolator;
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new LinearInterpolator();
        }
    }
}
