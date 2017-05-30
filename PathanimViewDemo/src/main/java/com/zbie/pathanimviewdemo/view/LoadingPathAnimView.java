package com.zbie.pathanimviewdemo.view;

import android.content.Context;
import android.util.AttributeSet;

import com.zbie.pathanimviewdemo.res.StoreHousePath;
import com.zbie.pathanimviewdemo.util.PathParserUtils;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.pathanimviewdemo.view
 * 创建时间         2017/05/06 00:22
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class LoadingPathAnimView extends PathAnimView {

    public LoadingPathAnimView(Context context) {
        this(context, null);
    }

    public LoadingPathAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingPathAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // setSourcePath(PathParserUtils.getPathFromArrayFloatList(StoreHousePath.getPath("ZhangXuTong")));
        setSourcePath(PathParserUtils.getPathFromArrayFloatList(StoreHousePath.getPath("Zombie1991")));
    }
}
