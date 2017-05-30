package com.zbie.magicindicatordemo.zoomhoverview_lib;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.util.SimpleArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

import com.zbie.magicindicatordemo.R;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.KITKAT;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.zoomhoverview_lib
 * 创建时间         2017/05/06 01:52
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            http://www.jianshu.com/p/76d1881d7a73#
 */
public class ZoomHoverView extends RelativeLayout implements ZoomHoverAdapter.OnDataChangedListener, View.OnClickListener {

    private static final String TAG = "ZoomHoverView";

    /**
     * adapter ZoomHoverView的数据适配器
     * {@link #setAdapter}
     */
    private ZoomHoverAdapter mZoomHoverAdapter;
    // 需要的列数
    private int mColumnNum    = 3;
    // 行列的分割线宽度
    private int mDivider      = 10;
    // 子view距离父控件的外边距宽度
    private int mMarginParent = 20;
    // 动画持续时间
    private int   mAnimDuration;
    // 动画缩放倍数
    private float mAnimZoomTo;
    // 记录当前行
    private int mCurrentRow    = 1;
    // 记录当前列
    private int mCurrentColumn = 0;

    // 记录每行第一列的下标（row First column position）
    // K--所在行数   V--当前view的下标
    private SimpleArrayMap<Integer, Integer> mRFColPosMap = new SimpleArrayMap<>();

    // 当前放大动画
    private AnimatorSet mCurrentZoomInAnim = null;

    // 当前缩小动画
    private AnimatorSet mCurrentZoomOutAnim = null;

    // 缩放动画监听器
    private OnZoomAnimatorListener mOnZoomAnimatorListener = null;

    // 缩放动画插值器
    private Interpolator mZoomInInterpolator;// 放大插值器
    private Interpolator mZoomOutInterpolator;// 缩小插值器

    // 上一个ZoomOut的view(为了解决快速切换时，上一个被缩小的view缩放大小不正常的情况)
    private View mPreZoomOutView;

    // 当前被选中的view
    private View mCurrentSelectedView = null;

    // item选中监听器
    private OnItemSelectedListener mOnItemSelectedListener;

    // 存储当前layout中所有子view
    private List<View> mViewList;

    public ZoomHoverView(Context context) {
        this(context, null);
    }

    public ZoomHoverView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomHoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZoomHoverView);
        mColumnNum = typedArray.getInt(R.styleable.ZoomHoverView_zhv_column_num, 3);
        mDivider = typedArray.getDimensionPixelOffset(R.styleable.ZoomHoverView_zhv_divider, DensityUtils.dip2px(context, 5));
        mMarginParent = typedArray.getDimensionPixelOffset(R.styleable.ZoomHoverView_zhv_margin_parent, DensityUtils.dip2px(context, 5));
        mAnimDuration = typedArray.getInt(R.styleable.ZoomHoverView_zhv_zoom_duration, getResources().getInteger(android.R.integer.config_shortAnimTime));
        mAnimZoomTo = typedArray.getFloat(R.styleable.ZoomHoverView_zhv_zoom_to, 1.2f);
        typedArray.recycle();
        mZoomOutInterpolator = mZoomInInterpolator = new AccelerateDecelerateInterpolator();
    }

    /**
     * 设置数据适配器
     *
     * @param adapter {@link #mZoomHoverAdapter}
     */
    public void setAdapter(ZoomHoverAdapter adapter) {
        mZoomHoverAdapter = adapter;
        mZoomHoverAdapter.setDataChangedListener(this);
        changeAdapter();
    }

    /**
     * 根据adapter添加view
     * {@link #mZoomHoverAdapter}
     */
    private void changeAdapter() {
        removeAllViews();
        // 重置参数(因为changeAdapter可能调用多次)
        mColumnNum = 3;
        mCurrentRow = 1;
        mCurrentColumn = 0;
        mRFColPosMap.clear();

        mViewList = new ArrayList<>(mZoomHoverAdapter.getCount());
        // 需要拉伸的下标的参数K-下标，V-跨度???
        SimpleArrayMap<Integer, Integer> needSpanMap = mZoomHoverAdapter.getSpanList();
        for (int i = 0; i < mZoomHoverAdapter.getCount(); i++) {
            // 获取子view
            View childView = mZoomHoverAdapter.getView(this, i, mZoomHoverAdapter.getItem(i));
            mViewList.add(childView);
            childView.setId(i + 1);

            // 判断当前view是否设置了跨度
            int span = 1;
            if (needSpanMap.containsKey(i)) {
                span = needSpanMap.get(i);
            }
            // 获取AdapterView的的布局参数
            LayoutParams childViewParams = (LayoutParams) childView.getLayoutParams();

            if (childViewParams == null) {
                childViewParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            // 如果view的宽高设置了wrap_content或者match_parent则span无效
            if (childViewParams.width <= 0) {
                span = 1;
            }

            // 如果跨度有变,重新设置view的宽
            if (span > 1 && span <= mColumnNum) {
                childViewParams.width = childViewParams.width * span + (span - 1) * mDivider;
            } else if (span < 1) {
                span = 1;
            } else if (span > mColumnNum) {
                span = mColumnNum;
                childViewParams.width = childViewParams.width * span + (span - 1) * mDivider;
            }

            // 设置右下左上的边距
            int rightMargin = 0;
            int bottomMargin = 0;
            int leftMargin = 0;
            int topMargin = 0;

            // 如果  跨度 + 当前的列 > 设置的列数 ,换行
            if (span + mCurrentColumn > mColumnNum) {
                // 换行当前行数+1
                mCurrentRow++;
                // 当前列等于当前view的跨度
                mCurrentColumn = span;
                // 换行以后肯定是第一个
                mRFColPosMap.put(mCurrentRow, i);
                // 换行操作
                // 因为换行,肯定不是第一行
                // 换行操作后将当前view添加到上一行第一个位置的下面
                childViewParams.addRule(RelativeLayout.BELOW,
                        mViewList.get(mRFColPosMap.get(mCurrentRow - 1)).getId());
                // 不是第一行,所以上边距为分割线的宽度
                topMargin = mDivider;
                // 换行后位置在左边第一个,所以左边距为距离父控件的边距
                leftMargin = mMarginParent;
            } else {
                if (mCurrentColumn <= 0 && mCurrentRow <= 1) {
                    //第一行第一列的位置保存第一列信息,同时第一列不需要任何相对规则
                    mRFColPosMap.put(mCurrentRow, i);
                    //第一行第一列上边距和左边距都是距离父控件的边距
                    topMargin = mMarginParent;
                    leftMargin = mMarginParent;
                } else {
                    //不是每一行的第一个,就添加到前一个的view的右面,并且和前一个顶部对齐
                    childViewParams.addRule(RelativeLayout.RIGHT_OF,
                            mViewList.get(i - 1).getId());
                    childViewParams.addRule(ALIGN_TOP, mViewList.get(i - 1).getId());

                }
                // 移动到当前列
                mCurrentColumn += span;
            }

            if (mCurrentColumn >= mColumnNum || i >= mZoomHoverAdapter.getCount() - 1) {
                // 如果当前列为列总数或者当前view的下标等于最后一个view的下标那么就是最右边的view,设置父边距
                rightMargin = mMarginParent;
            } else {
                rightMargin = mDivider;
            }

            // 如果当前view是最后一个那么他肯定是最后一行
            if (i >= (mZoomHoverAdapter.getCount() - 1)) {
                bottomMargin = mMarginParent;
            }

            // 设置外边距
            childViewParams.setMargins(leftMargin, topMargin, rightMargin,
                    bottomMargin);
            // 添加view
            addView(childView, childViewParams);
            // 添加点击事件
            childView.setOnClickListener(this);
        }
    }

    @Override
    public void onChanged() {
        changeAdapter();
    }

    /**
     * 宫格里的Item点击逻辑(如各个Item的缩放动画变化)
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (mCurrentSelectedView == null) {
            // 如果mCurrentSelectedView为null，证明第一次点击
            zoomInAnim(v);
            mCurrentSelectedView = v;
            if (mOnItemSelectedListener != null) {
                mOnItemSelectedListener.onItemSelected(mCurrentSelectedView, mCurrentSelectedView.getId() - 1);
            }
        } else {
            if (v.getId() != mCurrentSelectedView.getId()) {
                // 点击的view不是mCurrentSelectedView
                // mCurrentSelectedView执行缩小动画
                zoomOutAnim(mCurrentSelectedView);
                //当前点击的view赋值给mCurrentSelectedView
                mCurrentSelectedView = v;
                // 执行放大动画
                zoomInAnim(mCurrentSelectedView);
                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(mCurrentSelectedView, mCurrentSelectedView.getId() - 1);
                }
            }
        }
    }

    /**
     * 放大动画
     *
     * @param v
     */
    private void zoomInAnim(final View v) {
        // 将view放在其他view之上
        v.bringToFront();
        // 按照bringToFront文档来的，暂没测试
        if (Build.VERSION.SDK_INT < KITKAT) {
            requestLayout();
        }
        if (mCurrentZoomInAnim != null) {
            // 如果当前有放大动画执行，cancel调
            mCurrentZoomInAnim.cancel();
        }
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(v, "scaleX", 1.0f, mAnimZoomTo);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(v, "scaleY", 1.0f, mAnimZoomTo);
        objectAnimatorX.setDuration(mAnimDuration);
        objectAnimatorX.setInterpolator(mZoomInInterpolator);
        objectAnimatorY.setDuration(mAnimDuration);
        objectAnimatorY.setInterpolator(mZoomInInterpolator);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(objectAnimatorX, objectAnimatorY);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // 放大动画开始
                if (mOnZoomAnimatorListener != null) {
                    mOnZoomAnimatorListener.onZoomInStart(v);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 放大动画结束
                if (mOnZoomAnimatorListener != null) {
                    mOnZoomAnimatorListener.onZoomInEnd(v);
                }
                mCurrentZoomInAnim = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // 放大动画退出
                mCurrentZoomInAnim = null;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
        mCurrentZoomInAnim = set;
    }

    /**
     * 缩小动画
     *
     * @param v
     */
    private void zoomOutAnim(final View v) {
        if (mCurrentZoomOutAnim != null) {
            // 如果当前有放大动画执行，cancel调
            mCurrentZoomOutAnim.cancel();
            // 动画cancel后，上一个缩小view的scaleX不是1.0，就手动设置scaleX，Y到1.0
            if (mPreZoomOutView != null && mPreZoomOutView.getScaleX() > 1.0) {
                mPreZoomOutView.setScaleX(1.0f);
                mPreZoomOutView.setScaleY(1.0f);
            }
        }
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(v, "scaleX", mAnimZoomTo, 1.0f);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(v, "scaleY", mAnimZoomTo, 1.0f);
        objectAnimatorX.setDuration(mAnimDuration);
        objectAnimatorX.setInterpolator(mZoomOutInterpolator);
        objectAnimatorY.setDuration(mAnimDuration);
        objectAnimatorY.setInterpolator(mZoomOutInterpolator);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(objectAnimatorX, objectAnimatorY);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // 缩小动画开始
                if (mOnZoomAnimatorListener != null) {
                    mOnZoomAnimatorListener.onZoomOutStart(v);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 缩小动画结束
                if (mOnZoomAnimatorListener != null) {
                    mOnZoomAnimatorListener.onZoomOutEnd(v);
                }
                mCurrentZoomOutAnim = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // 缩小动画退出
                mCurrentZoomOutAnim = null;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
        mCurrentZoomOutAnim = set;
        // 把当前缩放的view作为上一个缩放的view
        mPreZoomOutView = v;
    }

    /**
     * 设置缩放动画监听器
     *
     * @param l
     */
    public void setOnZoomAnimatorListener(OnZoomAnimatorListener l) {
        if (l == null) {
            Log.d(TAG, "OnZoomAnimatorListener is null");
            throw new IllegalArgumentException("OnZoomAnimatorListener is null");
        }
        mOnZoomAnimatorListener = l;
    }

    /**
     * 同时设置放大、缩小动画的插值器
     *
     * @param interpolator
     */
    public void setZoomInterpolator(Interpolator interpolator) {
        mZoomInInterpolator = mZoomOutInterpolator = interpolator;
    }

    /**
     * 设置放大动画的插值器
     *
     * @param interpolator
     */
    public void setZoomInInterpolator(Interpolator interpolator) {
        mZoomInInterpolator = interpolator;
    }

    /**
     * 设置缩小动画的插值器
     *
     * @param interpolator
     */
    public void setZoomOutInterpolator(Interpolator interpolator) {
        mZoomOutInterpolator = interpolator;
    }

    /**
     * 设置item选中监听器
     *
     * @param l
     */
    public void setOnItemSelectedListener(OnItemSelectedListener l) {
        if (l == null) {
            Log.d(TAG, "OnItemSelectedListener is null");
            throw new IllegalArgumentException("OnItemSelectedListener is null");
        }
        mOnItemSelectedListener = l;
    }

    /**
     * 设置选中的条目
     *
     * @param position
     */
    public void setSelectedItem(int position) {
        // list为空或者size<position不执行操作
        if (mViewList == null || mViewList.size() <= position) {
            return;
        }

        // 同onClick事件处理
        if (mCurrentSelectedView != null) {
            if (mCurrentSelectedView.getId() - 1 != position) {
                zoomOutAnim(mCurrentSelectedView);
                mCurrentSelectedView = mViewList.get(position);
                zoomInAnim(mCurrentSelectedView);
                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(mCurrentSelectedView, mCurrentSelectedView.getId() - 1);
                }
            }
        } else {
            zoomInAnim(mViewList.get(position));
            mCurrentSelectedView = mViewList.get(position);
            if (mOnItemSelectedListener != null) {
                mOnItemSelectedListener.onItemSelected(mCurrentSelectedView, mCurrentSelectedView.getId() - 1);
            }
        }
    }

    /**
     * 设置动画持续时长
     *
     * @param duration
     */
    public void setAnimDuration(int duration) {
        mAnimDuration = duration;
    }

    /**
     * 设置放大的倍数
     *
     * @param animZoomTo
     */
    public void setAnimZoomTo(float animZoomTo) {
        mAnimZoomTo = animZoomTo;
    }

    /**
     * 设置列数
     *
     * @param columnNum
     */
    public void setColumnNum(int columnNum) {
        mColumnNum = columnNum;
    }

    /**
     * 设置分割线宽度
     *
     * @param divider
     */
    public void setZoomDivider(int divider) {
        mDivider = DensityUtils.dip2px(getContext(), divider);
    }

    /**
     * 设置距离父边框的宽度
     *
     * @param marginParent
     */
    public void setZoomMarginParent(int marginParent) {
        mMarginParent = DensityUtils.dip2px(getContext(), marginParent);
        ;
    }

    /**
     * 缩放动画监听器
     */
    public interface OnZoomAnimatorListener {
        /**
         * 放大动画开始
         *
         * @param v 被选中的View,将被放大
         */
        void onZoomInStart(View v);

        /**
         * 放大动画结束
         *
         * @param v 被选中的View,将被放大
         */
        void onZoomInEnd(View v);

        /**
         * 缩小动画开始
         *
         * @param v 被选中的View,将被缩小
         */
        void onZoomOutStart(View v);

        /**
         * 缩小动画结束
         *
         * @param v 被选中的View,将被缩小
         */
        void onZoomOutEnd(View v);
    }

    /**
     * item选中回调
     */
    public interface OnItemSelectedListener {
        /**
         * item选中回调方法
         *
         * @param v        选中的View
         * @param position 选中View的位置序号
         */
        void onItemSelected(View v, int position);
    }
}
