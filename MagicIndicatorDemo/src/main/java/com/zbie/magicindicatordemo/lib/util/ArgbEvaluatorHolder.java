package com.zbie.magicindicatordemo.lib.util;

import android.animation.TypeEvaluator;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.lib.util
 * 创建时间         2017/05/06 01:17
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            解决系统自带的ArgbEvaluator存在的兼容性问题
 */
public class ArgbEvaluatorHolder implements TypeEvaluator<Integer> {

    private static final ArgbEvaluatorHolder sInstance = new ArgbEvaluatorHolder();

    public static Integer eval(float fraction, Integer startValue, Integer endValue) {
        return sInstance.evaluate(fraction, startValue, endValue);
    }

    @Override
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (startA + (int) (fraction * (endA - startA))) << 24 |
                (startR + (int) (fraction * (endR - startR))) << 16 |
                (startG + (int) (fraction * (endG - startG))) << 8 |
                (startB + (int) (fraction * (endB - startB)));
    }
}
