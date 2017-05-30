package com.zbie.magicindicatordemo.lib.imp.commonnavigator;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.zbie.magicindicatordemo.R;
import com.zbie.magicindicatordemo.lib.NavigatorHelper;
import com.zbie.magicindicatordemo.lib.abs.IPagerNavigator;
import com.zbie.magicindicatordemo.lib.imp.commonnavigator.abs.CommonNavigatorAdapter;
import com.zbie.magicindicatordemo.lib.imp.commonnavigator.abs.IMeasurablePagerTitleView;
import com.zbie.magicindicatordemo.lib.imp.commonnavigator.abs.IPagerIndicator;
import com.zbie.magicindicatordemo.lib.imp.commonnavigator.abs.IPagerTitleView;
import com.zbie.magicindicatordemo.lib.imp.commonnavigator.model.PositionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.lib.imp.commonnavigator
 * 创建时间         2017/05/06 01:32
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            通用的ViewPager指示器,包含PagerTitle和PagerIndicator
 */
public class CommonNavigator extends FrameLayout implements IPagerNavigator, NavigatorHelper.OnNavigatorScrollListener {

    private HorizontalScrollView mScrollView;
    private LinearLayout         mTitleContainer;
    private LinearLayout         mIndicatorContainer;
    private IPagerIndicator      mIndicator;

    private CommonNavigatorAdapter mAdapter;
    private NavigatorHelper        mNavigatorHelper;

    /** 提供给外部的参数配置 */
    /****************************************************/
    private boolean mAdjustMode;// 自适应模式,适用于数目固定的、少量的title
    private boolean mEnablePivotScroll;// 启动中心点滚动
    private float   mScrollPivotX = 0.5f;// 滚动中心点 0.0f - 1.0f
    private boolean mSmoothScroll = true;// 是否平滑滚动，适用于 !mAdjustMode && !mFollowTouch
    private boolean mFollowTouch  = true;// 是否手指跟随滚动
    private int     mRightPadding;
    private int     mLeftPadding;
    private boolean mIndicatorOnTop;// 指示器是否在title上方,   默认为下方
    private boolean mSkimOver;// 跨多页切换时,中间页是否显示 "掠过" 效果
    private boolean            mReselectWhenLayout = true;// PositionData准备好时,是否重新选中当前页,为true可保证在极端情况下指示器状态正确
    /****************************************************/

    // 保存每个title的位置信息，为扩展indicator提供保障
    private List<PositionData> mPositionDataList   = new ArrayList<>();

    private DataSetObserver mObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            mNavigatorHelper.setTotalCount(mAdapter.getCount());// 如果使用helper，应始终保证helper中的totalCount为最新
            viewInit();
        }

        @Override
        public void onInvalidated() {
            // 没什么用,暂不做处理
        }
    };

    public CommonNavigator(Context context) {
        super(context);
        init();
    }

    public CommonNavigator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CommonNavigator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mNavigatorHelper = new NavigatorHelper();
        mNavigatorHelper.setNavigatorScrollListener(this);
    }

    private void viewInit() {
        removeAllViews();
        View root;
        if (mAdjustMode) {
            root = LayoutInflater.from(getContext()).inflate(R.layout.pager_navigator_layout_no_scroll, this);
        } else {
            root = LayoutInflater.from(getContext()).inflate(R.layout.pager_navigator_layout, this);
        }
        mScrollView = (HorizontalScrollView) root.findViewById(R.id.scroll_view);   // mAdjustMode为true时，mScrollView为null
        mTitleContainer = (LinearLayout) root.findViewById(R.id.title_container);
        mTitleContainer.setPadding(mLeftPadding, 0, mRightPadding, 0);

        mIndicatorContainer = (LinearLayout) root.findViewById(R.id.indicator_container);
        if (mIndicatorOnTop) {
            mIndicatorContainer.getParent().bringChildToFront(mIndicatorContainer);
        }
        initTitlesAndIndicator();
    }

    /**
     * 初始化title和indicator
     */
    private void initTitlesAndIndicator() {
        for (int i = 0, j = mNavigatorHelper.getTotalCount(); i < j; i++) {
            IPagerTitleView v = mAdapter.getTitleView(getContext(), i);
            if (v instanceof View) {
                View                      view = (View) v;
                LinearLayout.LayoutParams lp;
                if (mAdjustMode) {
                    lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    lp.weight = mAdapter.getTitleWeight(getContext(), i);
                } else {
                    lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                }
                mTitleContainer.addView(view, lp);
            }
        }
        if (mAdapter != null) {
            mIndicator = mAdapter.getIndicator(getContext());
            if (mIndicator instanceof View) {
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mIndicatorContainer.addView((View) mIndicator, lp);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mAdapter != null) {
            mNavigatorHelper.onPageScrolled(position, positionOffset, positionOffsetPixels);
            if (mIndicator != null) {
                mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            // 手指跟随滚动
            if (mScrollView != null && mPositionDataList.size() > 0) {
                if (mFollowTouch) {
                    int          currentPosition = Math.min(mPositionDataList.size() - 1, position);
                    int          nextPosition    = Math.min(mPositionDataList.size() - 1, position + 1);
                    PositionData current         = mPositionDataList.get(currentPosition);
                    PositionData next            = mPositionDataList.get(nextPosition);
                    float        scrollTo        = current.horizontalCenter() - mScrollView.getWidth() * mScrollPivotX;
                    float        nextScrollTo    = next.horizontalCenter() - mScrollView.getWidth() * mScrollPivotX;
                    mScrollView.scrollTo((int) (scrollTo + (nextScrollTo - scrollTo) * positionOffset), 0);
                } else if (!mEnablePivotScroll) {
                    // TODO 实现待选中项完全显示出来
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mAdapter != null) {
            mNavigatorHelper.onPageSelected(position);
            if (mIndicator != null) {
                mIndicator.onPageSelected(position);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mAdapter != null) {
            mNavigatorHelper.onPageScrollStateChanged(state);
            if (mIndicator != null) {
                mIndicator.onPageScrollStateChanged(state);
            }
        }
    }

    @Override
    public void onAttachToMagicIndicator() {
        viewInit();// 将初始化延迟到这里
    }

    @Override
    public void onDetachFromMagicIndicator() {

    }

    @Override
    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        if (mTitleContainer == null) {
            return;
        }
        View chlidView = mTitleContainer.getChildAt(index);
        if (chlidView instanceof IPagerTitleView) {
            ((IPagerTitleView) chlidView).onEnter(index, totalCount, enterPercent, leftToRight);
        }
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        if (mTitleContainer == null) {
            return;
        }
        View chlidView = mTitleContainer.getChildAt(index);
        if (chlidView instanceof IPagerTitleView) {
            ((IPagerTitleView) chlidView).onLeave(index, totalCount, leavePercent, leftToRight);
        }
    }

    @Override
    public void onSelected(int index, int totalCount) {
        if (mTitleContainer == null) {
            return;
        }
        View childView = mTitleContainer.getChildAt(index);
        if (childView instanceof IPagerTitleView) {
            ((IPagerTitleView) childView).onSelected(index, totalCount);
        }
        if (!mAdjustMode && !mFollowTouch && mScrollView != null && mPositionDataList.size() > 0) {
            int          currentIndex = Math.min(mPositionDataList.size() - 1, index);
            PositionData current      = mPositionDataList.get(currentIndex);
            if (mEnablePivotScroll) {
                float scrollTo = current.horizontalCenter() - mScrollView.getWidth() * mScrollPivotX;
                if (mSmoothScroll) {
                    mScrollView.smoothScrollTo((int) (scrollTo), 0);
                } else {
                    mScrollView.scrollTo((int) (scrollTo), 0);
                }
            } else {
                // 如果当前项被部分遮挡，则滚动显示完全
                if (mScrollView.getScrollX() > current.mLeft) {
                    if (mSmoothScroll) {
                        mScrollView.smoothScrollTo(current.mLeft, 0);
                    } else {
                        mScrollView.scrollTo(current.mLeft, 0);
                    }
                } else if (mScrollView.getScrollX() + getWidth() < current.mRight) {
                    if (mSmoothScroll) {
                        mScrollView.smoothScrollTo(current.mRight - getWidth(), 0);
                    } else {
                        mScrollView.scrollTo(current.mRight - getWidth(), 0);
                    }
                }
            }
        }
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        if (mTitleContainer == null) {
            return;
        }
        View childView = mTitleContainer.getChildAt(index);
        if (childView instanceof IPagerTitleView) {
            ((IPagerTitleView) childView).onDeselected(index, totalCount);
        }
    }

    public boolean isAdjustMode() {
        return mAdjustMode;
    }

    public void setAdjustMode(boolean is) {
        mAdjustMode = is;
    }

    public CommonNavigatorAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(CommonNavigatorAdapter adapter) {
        if (mAdapter == adapter) {
            return;
        }
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mObserver);
            adapter.notifyDataSetChanged();
        } else {
            mNavigatorHelper.setTotalCount(0);
            viewInit();
        }
    }

    public float getScrollPivotX() {
        return mScrollPivotX;
    }

    public void setScrollPivotX(float scrollPivotX) {
        mScrollPivotX = scrollPivotX;
    }

    public IPagerIndicator getPagerIndicator() {
        return mIndicator;
    }

    public IPagerTitleView getPagerTitleView(int index) {
        if (mTitleContainer == null) {
            return null;
        }
        return (IPagerTitleView) mTitleContainer.getChildAt(index);
    }

    public boolean isEnablePivotScroll() {
        return mEnablePivotScroll;
    }

    public void setEnablePivotScroll(boolean is) {
        mEnablePivotScroll = is;
    }

    public boolean isSmoothScroll() {
        return mSmoothScroll;
    }

    public void setSmoothScroll(boolean smoothScroll) {
        mSmoothScroll = smoothScroll;
    }

    public boolean isFollowTouch() {
        return mFollowTouch;
    }

    public void setFollowTouch(boolean followTouch) {
        mFollowTouch = followTouch;
    }

    public boolean isSkimOver() {
        return mSkimOver;
    }

    public void setSkimOver(boolean skimOver) {
        mSkimOver = skimOver;
        mNavigatorHelper.setSkimOver(skimOver);
    }

    public int getRightPadding() {
        return mRightPadding;
    }

    public void setRightPadding(int rightPadding) {
        mRightPadding = rightPadding;
    }

    public int getLeftPadding() {
        return mLeftPadding;
    }

    public void setLeftPadding(int leftPadding) {
        mLeftPadding = leftPadding;
    }

    public boolean isIndicatorOnTop() {
        return mIndicatorOnTop;
    }

    public void setIndicatorOnTop(boolean indicatorOnTop) {
        mIndicatorOnTop = indicatorOnTop;
    }

    public boolean isReselectWhenLayout() {
        return mReselectWhenLayout;
    }

    public void setReselectWhenLayout(boolean reselectWhenLayout) {
        mReselectWhenLayout = reselectWhenLayout;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mAdapter != null) {
            preparePositionData();
            if (mIndicator != null) {
                mIndicator.onPositionDataProvide(mPositionDataList);
            }
            if (mReselectWhenLayout && mNavigatorHelper.getScrollState() == ViewPager.SCROLL_STATE_IDLE) {
                onPageSelected(mNavigatorHelper.getCurrentIndex());
                onPageScrolled(mNavigatorHelper.getCurrentIndex(), 0.0f, 0);
            }
        }
    }

    /**
     * 获取title的位置信息,为打造不同的指示器、各种效果提供可能
     */
    private void preparePositionData() {
        mPositionDataList.clear();
        for (int i = 0, j = mNavigatorHelper.getTotalCount(); i < j; i++) {
            PositionData data = new PositionData();
            View         view = mTitleContainer.getChildAt(i);
            if (view != null) {
                data.mLeft = view.getLeft();
                data.mTop = view.getTop();
                data.mRight = view.getRight();
                data.mBottom = view.getBottom();
                if (view instanceof IMeasurablePagerTitleView) {
                    IMeasurablePagerTitleView measurablePagerTitleView = (IMeasurablePagerTitleView) view;
                    data.mContentLeft = measurablePagerTitleView.getContentLeft();
                    data.mContentTop = measurablePagerTitleView.getContentTop();
                    data.mContentRight = measurablePagerTitleView.getContentRight();
                    data.mContentBottom = measurablePagerTitleView.getContentBottom();
                } else {
                    data.mContentLeft = data.mLeft;
                    data.mContentTop = data.mTop;
                    data.mContentRight = data.mRight;
                    data.mContentBottom = data.mBottom;
                }
            }
            mPositionDataList.add(data);
        }
    }
}
