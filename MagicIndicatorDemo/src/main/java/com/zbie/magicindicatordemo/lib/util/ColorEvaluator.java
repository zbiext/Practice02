package com.zbie.magicindicatordemo.lib.util;

import android.animation.TypeEvaluator;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.lib.util
 * 创建时间         2017/05/06 01:37
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class ColorEvaluator implements TypeEvaluator<String> {

    /** 红 */
    private int mCurrentRed = -1;

    /** 绿 */
    private int mCurrentGreen = -1;

    /** 蓝 */
    private int mCurrentBlue = -1;

    @Override
    public String evaluate(float fraction, String startValue, String endValue) {

        String startColor = startValue;
        String endColor   = endValue;
        int    startRed   = Integer.parseInt(startColor.substring(1, 3), 16);
        int    startGreen = Integer.parseInt(startColor.substring(3, 5), 16);
        int    startBlue  = Integer.parseInt(startColor.substring(5, 7), 16);
        int    endRed     = Integer.parseInt(endColor.substring(1, 3), 16);
        int    endGreen   = Integer.parseInt(endColor.substring(3, 5), 16);
        int    endBlue    = Integer.parseInt(endColor.substring(5, 7), 16);

        // 初始化颜色的值
        if (mCurrentRed == -1) {
            mCurrentRed = startRed;
        }
        if (mCurrentGreen == -1) {
            mCurrentGreen = startGreen;
        }
        if (mCurrentBlue == -1) {
            mCurrentBlue = startBlue;
        }

        // 计算初始颜色和结束颜色之间的差值
        int redDiff   = Math.abs(startRed - endRed);
        int greenDiff = Math.abs(startGreen - endGreen);
        int blueDiff  = Math.abs(startBlue - endBlue);
        int colorDiff = redDiff + greenDiff + blueDiff;
        if (mCurrentRed != endRed) {
            mCurrentRed = getCurrentColor(startRed, endRed, colorDiff, 0, fraction);
        } else if (mCurrentGreen != endGreen) {
            mCurrentGreen = getCurrentColor(startGreen, endGreen, colorDiff, redDiff, fraction);
        } else if (mCurrentBlue != endBlue) {
            mCurrentBlue = getCurrentColor(startBlue, endBlue, colorDiff, redDiff + greenDiff, fraction);
        }

        // 将计算出的当前颜色的值组装返回
        String currentColor = "#" + getHexString(mCurrentRed) + getHexString(mCurrentGreen) + getHexString(mCurrentBlue);
        return currentColor;
    }

    /**
     * 根据fraction值来计算当前的颜色
     *
     * @param startColor
     * @param endColor
     * @param colorDiff
     * @param offset
     * @param fraction
     * @return
     */
    private int getCurrentColor(int startColor, int endColor, int colorDiff, int offset, float fraction) {
        int currentColor;
        if (startColor > endColor) {
            currentColor = (int) (startColor - (fraction * colorDiff - offset));
            if (currentColor < endColor) {
                currentColor = endColor;
            }
        } else {
            currentColor = (int) (startColor + (fraction * colorDiff - offset));
            if (currentColor > endColor) {
                currentColor = endColor;
            }
        }
        return currentColor;
    }

    /**
     * 将10进制颜色值转换成16进制
     *
     * @param value
     * @return
     */
    private String getHexString(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }
}
