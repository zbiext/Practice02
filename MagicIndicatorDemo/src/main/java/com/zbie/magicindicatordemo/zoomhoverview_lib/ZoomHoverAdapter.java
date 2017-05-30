package com.zbie.magicindicatordemo.zoomhoverview_lib;

import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.zoomhoverview_lib
 * 创建时间         2017/05/06 01:50
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            ZoomHoverView的适配器 抽象
 */
public abstract class ZoomHoverAdapter<T> {

    private static final String TAG = "ZoomHoverAdapter";

    private List<T> mDataList;

    // 存储需要横跨的下标和跨度
    private SimpleArrayMap<Integer, Integer> mSpanMap = new SimpleArrayMap<>();

    // 数据变化回调
    private OnDataChangedListener mOnDataChangedListener;

    public ZoomHoverAdapter(List<T> list) {
        this.mDataList = list;
    }

    public ZoomHoverAdapter(T[] datas) {
        this.mDataList = new ArrayList<>(Arrays.asList(datas));
    }

    /**
     * 设置需要横跨的下标和跨度
     *
     * @param map key代表下标
     *            value代表跨度
     */
    public void setSpan(SimpleArrayMap<Integer, Integer> map) {
        mSpanMap.clear();
        mSpanMap.putAll(map);
        if (mOnDataChangedListener != null) {
            mOnDataChangedListener.onChanged();
        }
    }

    public void setSpan(int position, int span) {
        this.mSpanMap.clear();
        this.mSpanMap.put(position, span);
        if (mOnDataChangedListener != null) {
            mOnDataChangedListener.onChanged();
        }
    }

    public void setSpan(int position, int span, int position1, int span1) {
        this.mSpanMap.clear();
        this.mSpanMap.put(position, span);
        this.mSpanMap.put(position1, span1);
        if (mOnDataChangedListener != null) {
            mOnDataChangedListener.onChanged();
        }
    }

    public void setDataChangedListener(OnDataChangedListener l) {
        if (l == null) {
            Log.d(TAG, "OnDataChangedListener is null");
            throw new IllegalArgumentException("OnDataChangedListener is null");
        }
        mOnDataChangedListener = l;
    }

    /**
     * 获取数据Item的总数
     *
     * @return
     */
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public SimpleArrayMap getSpanList() {
        return mSpanMap;
    }

    public T getItem(int position) {
        if (mDataList == null) {
            return null;
        } else {
            return mDataList.get(position);
        }
    }

    interface OnDataChangedListener {
        void onChanged();
    }

    public abstract View getView(ViewGroup parent, int position, T t);
}
