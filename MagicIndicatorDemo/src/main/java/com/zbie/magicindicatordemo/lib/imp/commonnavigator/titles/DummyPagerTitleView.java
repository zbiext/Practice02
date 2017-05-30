package com.zbie.magicindicatordemo.lib.imp.commonnavigator.titles;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.zbie.magicindicatordemo.lib.imp.commonnavigator.abs.IPagerTitleView;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.lib.imp.commonnavigator.titles
 * 创建时间         2017/05/06 01:30
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            空指示器标题,用于只需要指示器而不需要title的需求
 */
public class DummyPagerTitleView extends View implements IPagerTitleView {

    public DummyPagerTitleView(Context context) {
        super(context);
    }

    public DummyPagerTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DummyPagerTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

    }
}
