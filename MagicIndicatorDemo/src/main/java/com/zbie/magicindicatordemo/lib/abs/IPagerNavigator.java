package com.zbie.magicindicatordemo.lib.abs;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.lib.abs
 * 创建时间         2017/05/06 01:03
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            抽象的ViewPager导航器
 */
public interface IPagerNavigator {

    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);

    /**
     * 当IPagerNavigator被添加到MagicIndicator时调用
     */
    void onAttachToMagicIndicator();

    /**
     * 当IPagerNavigator从MagicIndicator上移除时调用
     */
    void onDetachFromMagicIndicator();

    /**
     * ViewPager内容改变时需要先调用此方法，自定义的IPagerNavigator应当遵守此约定
     */
    void notifyDataSetChanged();
}
