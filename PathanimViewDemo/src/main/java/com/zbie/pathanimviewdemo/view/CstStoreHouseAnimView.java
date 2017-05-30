package com.zbie.pathanimviewdemo.view;

import android.content.Context;
import android.util.AttributeSet;

import com.zbie.pathanimviewdemo.res.StoreHousePath;
import com.zbie.pathanimviewdemo.util.PathParserUtils;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.pathanimviewdemo.view
 * 创建时间         2017/05/06 00:25
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class CstStoreHouseAnimView extends StoreHouseAnimView {

    public CstStoreHouseAnimView(Context context) {
        this(context, null);
    }

    public CstStoreHouseAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CstStoreHouseAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // setSourcePath(PathParserUtils.getPathFromArrayFloatList(StoreHousePath.getPath("ZhangXuTong", 1.1f, 16)));
        setSourcePath(PathParserUtils.getPathFromArrayFloatList(StoreHousePath.getPath("ZOMBIE20161219", 1.1f, 16)));
    }
}
