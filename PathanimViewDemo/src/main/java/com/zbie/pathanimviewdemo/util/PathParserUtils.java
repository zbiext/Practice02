package com.zbie.pathanimviewdemo.util;

import android.content.Context;
import android.graphics.Path;

import java.util.ArrayList;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.pathanimviewdemo.util
 * 创建时间         2017/05/06 00:17
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            解析成Path的工具类
 */
public class PathParserUtils {
    /**
     * 从R.array.xxx里取出点阵
     *
     * @param context
     * @param arrayId
     * @param zoomSize
     * @return
     */
    public static Path getPathFromStringArray(Context context, int arrayId, float zoomSize) {
        Path     path   = new Path();
        String[] points = context.getResources().getStringArray(arrayId);
        for (int i = 0; i < points.length; i++) {
            String[] x = points[i].split(",");
            // for (int j = 0; j < x.length; j += 2) {
            for (int j = 0; j < x.length; j = j + 2) {
                if (j == 0) {
                    path.moveTo(Float.parseFloat(x[j]) * zoomSize, Float.parseFloat(x[j + 1]) * zoomSize);
                } else {
                    path.lineTo(Float.parseFloat(x[j]) * zoomSize, Float.parseFloat(x[j + 1]) * zoomSize);
                }
            }
        }
        return path;
    }

    /**
     * 根据ArrayList<float[]> path 解析
     *
     * @param path
     * @return
     */
    public static Path getPathFromArrayFloatList(ArrayList<float[]> path) {
        Path sPath = new Path();
        for (int i = 0; i < path.size(); i++) {
            float[] floats = path.get(i);
            sPath.moveTo(floats[0], floats[1]);
            sPath.lineTo(floats[2], floats[3]);
        }
        return sPath;
    }
}
