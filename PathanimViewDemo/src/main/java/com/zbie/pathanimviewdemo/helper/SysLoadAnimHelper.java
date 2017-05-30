package com.zbie.pathanimviewdemo.helper;

import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.View;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.pathanimviewdemo.helper
 * 创建时间         2017/05/06 00:26
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class SysLoadAnimHelper extends PathAnimHelper {

    public SysLoadAnimHelper(View view, Path sourcePath, Path animPath) {
        super(view, sourcePath, animPath);
    }

    public SysLoadAnimHelper(View view, Path sourcePath, Path animPath, long animTime, boolean isInfinite) {
        super(view, sourcePath, animPath, animTime, isInfinite);
    }

    @Override
    public void onPathAnimCallback(View view, Path sourcePath, Path animPath, PathMeasure pathMeasure, ValueAnimator animation) {
        float value = (float) animation.getAnimatedValue();
        // 获取一个段落
        float end = pathMeasure.getLength() * value;
        float begin = (float) (end - ((0.5 - Math.abs(value - 0.5)) * pathMeasure.getLength()));
        animPath.reset();
        animPath.lineTo(0, 0);
        pathMeasure.getSegment(begin, end, animPath, true);
    }
}
