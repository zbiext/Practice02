package com.zbie.magicindicatordemo.lib.imp.commonnavigator.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.zbie.magicindicatordemo.lib.imp.commonnavigator.abs.IPagerIndicator;
import com.zbie.magicindicatordemo.lib.imp.commonnavigator.model.PositionData;
import com.zbie.magicindicatordemo.lib.util.ArgbEvaluatorHolder;
import com.zbie.magicindicatordemo.lib.util.UIUtil;

import java.util.List;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.lib.imp.commonnavigator.indicators
 * 创建时间         2017/05/06 01:12
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            贝塞尔曲线ViewPager指示器,带颜色渐变
 */
public class BezierPagerIndicator extends View implements IPagerIndicator {

    private List<PositionData> mPositionDataList;

    private float mLeftCircleRadius;
    private float mLeftCircleX;
    private float mRightCircleRadius;
    private float mRightCircleX;

    private float mYOffset;
    private float mMaxCircleRadius;
    private float mMinCircleRadius;

    private Paint mPaint;
    private Path mPath = new Path();

    private List<String> mColorList;
    private Interpolator mStartInterpolator = new AccelerateInterpolator();
    private Interpolator mEndInterpolator   = new DecelerateInterpolator();

    public BezierPagerIndicator(Context context) {
        super(context);
        init(context);
    }

    public BezierPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mMaxCircleRadius = UIUtil.dp2px(context, 3.5);
        mMinCircleRadius = UIUtil.dp2px(context, 2);
        mYOffset = UIUtil.dp2px(context, 1.5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mLeftCircleX, getHeight() - mYOffset - mMaxCircleRadius, mLeftCircleRadius, mPaint);
        canvas.drawCircle(mRightCircleX, getHeight() - mYOffset - mMaxCircleRadius, mRightCircleRadius, mPaint);
        drawBezierCurve(canvas);
    }

    /**
     * 绘制贝塞尔曲线
     *
     * @param canvas
     */
    private void drawBezierCurve(Canvas canvas) {
        mPath.reset();
        float y = getHeight() - mYOffset - mMaxCircleRadius;
        mPath.moveTo(mRightCircleX, y);
        mPath.lineTo(mRightCircleX, y - mRightCircleRadius);
        mPath.quadTo(mRightCircleX + (mLeftCircleX - mRightCircleX) / 2.0f, y, mLeftCircleX, y - mLeftCircleRadius);
        mPath.lineTo(mLeftCircleX, y + mLeftCircleRadius);
        mPath.quadTo(mRightCircleX + (mLeftCircleX - mRightCircleX) / 2.0f, y, mRightCircleX, y + mRightCircleRadius);
        mPath.close();// 闭合
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }
        // 计算颜色
        if (mColorList != null && mColorList.size() > 0) {
            int currentColor = Color.parseColor(mColorList.get(position % mColorList.size()));
            int nextColor    = Color.parseColor(mColorList.get((position + 1) % mColorList.size()));
            int color        = (Integer) ArgbEvaluatorHolder.eval(positionOffset, currentColor, nextColor);
            mPaint.setColor(color);
        }
        // 计算锚点位置
        int          currentPosition = Math.min(mPositionDataList.size() - 1, position);
        int          nextPosition    = Math.min(mPositionDataList.size() - 1, position + 1);
        PositionData current         = mPositionDataList.get(currentPosition);
        PositionData next            = mPositionDataList.get(nextPosition);

        float leftX  = current.mLeft + (current.mRight - current.mLeft) / 2;
        float rightX = next.mLeft + (next.mRight - next.mLeft) / 2;

        mLeftCircleX = leftX + (rightX - leftX) * mStartInterpolator.getInterpolation(positionOffset);
        mRightCircleX = leftX + (rightX - leftX) * mEndInterpolator.getInterpolation(positionOffset);
        mLeftCircleRadius = mMaxCircleRadius + (mMinCircleRadius - mMaxCircleRadius) * mEndInterpolator.getInterpolation(positionOffset);
        mRightCircleRadius = mMinCircleRadius + (mMaxCircleRadius - mMinCircleRadius) * mStartInterpolator.getInterpolation(positionOffset);

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

    public float getMaxCircleRadius() {
        return mMaxCircleRadius;
    }

    public void setMaxCircleRadius(float maxCircleRadius) {
        mMaxCircleRadius = maxCircleRadius;
    }

    public float getMinCircleRadius() {
        return mMinCircleRadius;
    }

    public void setMinCircleRadius(float minCircleRadius) {
        mMinCircleRadius = minCircleRadius;
    }

    public float getYOffset() {
        return mYOffset;
    }

    public void setYOffset(float YOffset) {
        mYOffset = YOffset;
    }

    public void setColorList(List<String> colorList) {
        mColorList = colorList;
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new AccelerateInterpolator();
        }
    }

    public void setEndInterpolator(Interpolator endInterpolator) {
        mEndInterpolator = endInterpolator;
        if (mEndInterpolator == null) {
            mEndInterpolator = new DecelerateInterpolator();
        }
    }
}