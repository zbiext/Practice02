package com.zbie.magicindicatordemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zbie.magicindicatordemo.lib.util.ColorEvaluator;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo
 * 创建时间         2017/05/06 01:57
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class Bule2Red extends Activity {

    private Button targetView;
    private int mCurrentRed   = -1;
    private int mCurrentGreen = -1;
    private int mCurrentBlue  = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bule2_red);
        targetView = (Button) findViewById(R.id.tv_color_backgroound);
        targetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayResult(targetView, "#0000ff", "#ff0000");
            }
        });
    }

    private void displayResult(final View target, final String start, final String end) {
        ////创建ValueAnimator对象，实现颜色渐变
        ////方法一: http://www.cnblogs.com/wondertwo/p/5312482.html
        //ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, 100f);
        //valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        //
        //    @Override
        //    public void onAnimationUpdate(ValueAnimator animation) {
        //        // 获取当前动画的进度值，1~100
        //        float currentValue = (float) animation.getAnimatedValue();
        //        Log.d("当前动画值", "current value : " + currentValue);
        //
        //        // 获取动画当前时间流逝的百分比，范围在0~1之间
        //        float fraction = animation.getAnimatedFraction();
        //        // 直接调用evaluateForColor()方法，通过百分比计算出对应的颜色值
        //        String colorResult = evaluateForColor(fraction, start, end);
        //
        //        /**
        //         * 通过Color.parseColor(colorResult)解析字符串颜色值，传给ColorDrawable，创建ColorDrawable对象
        //         */
        //        /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) target.getLayoutParams();*/
        //        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(colorResult));
        //        // 把ColorDrawable对象设置为target的背景
        //        target.setBackground(colorDrawable);
        //        target.invalidate();
        //        target.setClickable(false);
        //    }
        //});
        //valueAnimator.setDuration(6 * 1000);

        // 方法二: http://www.cnblogs.com/wondertwo/p/5327586.html
        ObjectAnimator anim = ObjectAnimator.ofObject(target, "color", new ColorEvaluator(), "#0000FF", "#FF0000");
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                String currentValue = (String) animation.getAnimatedValue();
                Log.d("当前动画值", "current value : " + currentValue);
                ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(currentValue));
                target.setBackground(colorDrawable);
                target.invalidate();
                target.setClickable(false);
            }
        });
        anim.setDuration(6 * 1000);

        // 组装缩放动画
        ValueAnimator animator_1 = ObjectAnimator.ofFloat(target, "scaleX", 1f, 0.5f);
        ValueAnimator animator_2 = ObjectAnimator.ofFloat(target, "scaleY", 1f, 0.5f);
        ValueAnimator animator_3 = ObjectAnimator.ofFloat(target, "scaleX", 0.5f, 1f);
        ValueAnimator animator_4 = ObjectAnimator.ofFloat(target, "scaleY", 0.5f, 1f);
        AnimatorSet   set_1      = new AnimatorSet();
        set_1.play(animator_1).with(animator_2);
        AnimatorSet set_2 = new AnimatorSet();
        set_2.play(animator_3).with(animator_4);
        AnimatorSet set_3 = new AnimatorSet();
        set_3.play(set_1).before(set_2);
        set_3.setDuration(3 * 1000);

        AnimatorSet set_4 = new AnimatorSet();
        // 组装颜色动画和缩放动画，并启动动画
        // 方式一
        //        set_4.play(valueAnimator).with(set_3);
        // 方式二
        set_4.play(anim).with(set_3);
        set_4.start();

        set_4.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                target.setBackground(new ColorDrawable(Color.parseColor("#0000ff")));
                target.invalidate();
                mCurrentRed   = -1;
                mCurrentGreen = -1;
                mCurrentBlue  = -1;
                target.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * evaluateForColor()计算颜色值并返回
     */
    private String evaluateForColor(float fraction, String startValue, String endValue) {

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
     * 将10进制颜色值转换成16进制。
     */
    private String getHexString(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }
}
