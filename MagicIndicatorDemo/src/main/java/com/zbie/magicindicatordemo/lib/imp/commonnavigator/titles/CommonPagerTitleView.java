package com.zbie.magicindicatordemo.lib.imp.commonnavigator.titles;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zbie.magicindicatordemo.lib.imp.commonnavigator.abs.IMeasurablePagerTitleView;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.lib.imp.commonnavigator.titles
 * 创建时间         2017/05/06 01:28
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            通用的指示器标题,子元素内容由外部提供,事件回传给外部
 */
public class CommonPagerTitleView extends FrameLayout implements IMeasurablePagerTitleView {

    private OnPagerTitleChangeListener  mOnPagerTitleChangeListener;
    private ContentPositionDataProvider mContentPositionDataProvider;

    public CommonPagerTitleView(Context context) {
        super(context);
    }

    public CommonPagerTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonPagerTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        if (mOnPagerTitleChangeListener != null) {
            mOnPagerTitleChangeListener.onSelected(index, totalCount);
        }
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        if (mOnPagerTitleChangeListener != null) {
            mOnPagerTitleChangeListener.onDeselected(index, totalCount);
        }
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        if (mOnPagerTitleChangeListener != null) {
            mOnPagerTitleChangeListener.onLeave(index, totalCount, leavePercent, leftToRight);
        }
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        if (mOnPagerTitleChangeListener != null) {
            mOnPagerTitleChangeListener.onEnter(index, totalCount, enterPercent, leftToRight);
        }
    }


    @Override
    public int getContentLeft() {
        if (mContentPositionDataProvider != null) {
            return mContentPositionDataProvider.getContentLeft();
        }
        return getLeft();
    }

    @Override
    public int getContentTop() {
        if (mContentPositionDataProvider != null) {
            return mContentPositionDataProvider.getContentTop();
        }
        return getTop();
    }

    @Override
    public int getContentRight() {
        if (mContentPositionDataProvider != null) {
            return mContentPositionDataProvider.getContentRight();
        }
        return getRight();
    }

    @Override
    public int getContentBottom() {
        if (mContentPositionDataProvider != null) {
            return mContentPositionDataProvider.getContentBottom();
        }
        return getBottom();
    }

    /**
     * 外部直接将布局设置进来
     *
     * @param contentView
     */
    public void setContentView(View contentView) {
        setContentView(contentView, null);
    }

    /**
     * 外部直接将布局设置进来
     *
     * @param contentView
     * @param lp
     */
    public void setContentView(View contentView, LayoutParams lp) {
        removeAllViews();
        if (contentView != null) {
            if (lp == null) {
                lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
            addView(contentView, lp);
        }
    }

    /**
     * 外部直接将布局设置进来
     *
     * @param layoutId
     */
    public void setContentView(int layoutId) {
        View child = LayoutInflater.from(getContext()).inflate(layoutId, null);
        setContentView(child, null);
    }

    public OnPagerTitleChangeListener getOnPagerTitleChangeListener() {
        return mOnPagerTitleChangeListener;
    }

    public void setOnPagerTitleChangeListener(OnPagerTitleChangeListener onPagerTitleChangeListener) {
        mOnPagerTitleChangeListener = onPagerTitleChangeListener;
    }

    public ContentPositionDataProvider getContentPositionDataProvider() {
        return mContentPositionDataProvider;
    }

    public void setContentPositionDataProvider(ContentPositionDataProvider contentPositionDataProvider) {
        mContentPositionDataProvider = contentPositionDataProvider;
    }

    public interface OnPagerTitleChangeListener {
        void onSelected(int index, int totalCount);

        void onDeselected(int index, int totalCount);

        void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight);

        void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight);
    }

    public interface ContentPositionDataProvider {
        int getContentLeft();

        int getContentTop();

        int getContentRight();

        int getContentBottom();
    }
}
