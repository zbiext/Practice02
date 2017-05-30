package com.zbie.magicindicatordemo.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.zbie.magicindicatordemo.lib.abs.IPagerNavigator;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.lib
 * 创建时间         2017/05/06 01:36
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            整个框架的入口，核心
 */
public class MagicIndicator extends FrameLayout {

    private static final String TAG = "MagicIndicator";
    private IPagerNavigator mPackNavigator;

    public MagicIndicator(Context context) {
        super(context);
    }

    public MagicIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MagicIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPackNavigator != null) {
            mPackNavigator.onPageScrolled(position, positionOffset, positionOffsetPixels);
        } else {
            Log.d(TAG, "mPackNavigator is null");
            throw new IllegalArgumentException("mPackNavigator is null");
        }
    }

    public void onPageSelected(int position) {
        if (mPackNavigator != null) {
            mPackNavigator.onPageSelected(position);
        } else {
            Log.d(TAG, "mPackNavigator is null");
            throw new IllegalArgumentException("mPackNavigator is null");
        }
    }

    public void onPageScrollStateChanged(int state) {
        if (mPackNavigator != null) {
            mPackNavigator.onPageScrollStateChanged(state);
        } else {
            Log.d(TAG, "mPackNavigator is null");
            throw new IllegalArgumentException("mPackNavigator is null");
        }
    }

    public IPagerNavigator getNavigator() {
        return mPackNavigator;
    }

    public void setNavigator(IPagerNavigator packNavigator) {
        if (mPackNavigator == packNavigator) {
            return;
        }
        if (mPackNavigator != null) {
            mPackNavigator.onDetachFromMagicIndicator();
        }
        mPackNavigator = packNavigator;
        removeAllViews();
        if (mPackNavigator instanceof View) {
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            addView((View) mPackNavigator, lp);
            mPackNavigator.onAttachToMagicIndicator();
        }
    }
}
