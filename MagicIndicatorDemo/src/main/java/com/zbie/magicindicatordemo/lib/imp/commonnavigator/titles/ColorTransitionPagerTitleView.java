package com.zbie.magicindicatordemo.lib.imp.commonnavigator.titles;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.zbie.magicindicatordemo.lib.util.ArgbEvaluatorHolder;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.lib.imp.commonnavigator.titles
 * 创建时间         2017/05/06 01:27
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            两种颜色过渡的指示器标题
 */
public class ColorTransitionPagerTitleView extends SimplePagerTitleView {

    private Interpolator mStartInterpolator = new LinearInterpolator();
    private Interpolator mEndInterpolator   = new LinearInterpolator();

    public ColorTransitionPagerTitleView(Context context) {
        super(context);
    }

    public ColorTransitionPagerTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorTransitionPagerTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onSelected(int index, int totalCount) {
    }

    @Override
    public void onDeselected(int index, int totalCount) {
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        int color = ArgbEvaluatorHolder.eval(mEndInterpolator.getInterpolation(leavePercent), getSelectedColor(), getNormalColor());
        setTextColor(color);
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        int color = ArgbEvaluatorHolder.eval(mStartInterpolator.getInterpolation(enterPercent), getNormalColor(), getSelectedColor());
        setTextColor(color);
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

    public Interpolator getEndInterpolator() {
        return mEndInterpolator;
    }

    public void setEndInterpolator(Interpolator endInterpolator) {
        mEndInterpolator = endInterpolator;
        if (mEndInterpolator == null) {
            mEndInterpolator = new LinearInterpolator();
        }
    }
}
