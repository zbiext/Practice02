package com.zbie.magicindicatordemo.lib.imp.commonnavigator.abs;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.lib.imp.circlenavigator
 * 创建时间         2017/05/06 01:09
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            抽象的指示器标题,适用于CommonNavigator
 */
public interface IPagerTitleView {

    /**
     * 被选中
     *
     * @param index
     * @param totalCount
     */
    void onSelected(int index, int totalCount);

    /**
     * 未被选中
     *
     * @param index
     * @param totalCount
     */
    void onDeselected(int index, int totalCount);

    /**
     * 离开
     *
     * @param index
     * @param totalCount
     * @param leavePercent 离开的百分比, 0.0f - 1.0f
     * @param leftToRight 从左至右离开
     */
    void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight);

    /**
     * 进入
     *
     * @param index
     * @param totalCount
     * @param enterPercent 进入的百分比, 0.0f - 1.0f
     * @param leftToRight  从左至右离开
     */
    void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight);
}
