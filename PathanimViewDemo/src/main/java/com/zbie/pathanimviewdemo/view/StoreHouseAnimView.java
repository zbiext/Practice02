package com.zbie.pathanimviewdemo.view;

import android.content.Context;
import android.util.AttributeSet;

import com.zbie.pathanimviewdemo.helper.PathAnimHelper;
import com.zbie.pathanimviewdemo.helper.StoreHouseAnimHelper;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.pathanimviewdemo.view
 * 创建时间         2017/05/06 00:23
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class StoreHouseAnimView extends PathAnimView {

    public StoreHouseAnimView(Context context) {
        this(context, null);
    }

    public StoreHouseAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StoreHouseAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public long getPathMaxLength() {
        return ((StoreHouseAnimHelper) mPathAnimHelper).getPathMaxLength();
    }

    /**
     * 设置残影最大长度
     *
     * @param pathMaxLength
     * @return
     */
    public StoreHouseAnimView setPathMaxLength(long pathMaxLength) {
        ((StoreHouseAnimHelper) mPathAnimHelper).setPathMaxLength(pathMaxLength);
        return this;
    }

    @Override
    protected PathAnimHelper getInitAnimHelper() {
        return new StoreHouseAnimHelper(this, mSourcePath, mAnimPath);
    }
}
