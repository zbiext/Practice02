package com.zbie.magicindicatordemo.lib.imp.commonnavigator.titles.badge;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.lib.imp.commonnavigator.titles.badge
 * 创建时间         2017/05/06 01:21
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            角标的定位规则
 */
public class BadgeRule {

    private BadgeAnchor mAnchor;
    private int         mOffset;

    public BadgeRule(BadgeAnchor anchor, int offset) {
        mAnchor = anchor;
        mOffset = offset;
    }

    public BadgeAnchor getAnchor() {
        return mAnchor;
    }

    public void setAnchor(BadgeAnchor anchor) {
        mAnchor = anchor;
    }

    public int getOffset() {
        return mOffset;
    }

    public void setOffset(int offset) {
        mOffset = offset;
    }
}
