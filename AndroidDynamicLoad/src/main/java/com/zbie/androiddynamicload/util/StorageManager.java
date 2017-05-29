package com.zbie.androiddynamicload.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 涛 on 2017/5/5 0005.
 * 项目名           Practice02
 * 包名             com.zbie.androiddynamicload.util
 * 创建时间         2017/05/05 23:24
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class StorageManager {

    private final static String TAG     = StorageManager.class.getSimpleName();
    private final static String APP_DIR = "android-dynamic-load";

    private StorageManager() {
    }

    /**
     * 获取App在SD卡上的目录
     *
     * @return 成功则返回目录，否则返回null
     */
    public static File getSdcardAppDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File sdcard = Environment.getExternalStorageDirectory();
            File appDir = new File(sdcard, APP_DIR);
            if (!appDir.exists()) {
                boolean success = appDir.mkdir();
                if (success) {
                    return appDir;
                } else {
                    return null;
                }
            }
            return appDir;
        }
        return null;
    }

    /**
     * 复制文件到App在SD卡上的目录
     *
     * @param context
     * @param inputStream
     * @param outputFileName 文件名
     * @return 复制成功则返回文件目录，否则返回null
     */
    public static File copyFileToAppDir(Context context, InputStream inputStream, String outputFileName) {
        if (inputStream != null && !TextUtils.isEmpty(outputFileName)) {
            File appDir = getSdcardAppDir();
            if (appDir != null) {
                File outputFile = new File(appDir, outputFileName);
                if (outputFile.exists()) {
                    Log.d(TAG, "文件已存在");
                    //Toast.makeText(context, "文件已存在", Toast.LENGTH_SHORT).show();
                    return outputFile;
                }
                try {
                    FileOutputStream     fos    = new FileOutputStream(outputFile);
                    BufferedOutputStream bos    = new BufferedOutputStream(fos);
                    byte[]               buffer = new byte[1024];
                    int                  length = 0;
                    while ((length = inputStream.read(buffer)) != -1) {
                        bos.write(buffer, 0, length);
                    }
                    bos.flush();
                    bos.close();
                    inputStream.close();
                    Log.d(TAG, "文件复制成功");
                    //Toast.makeText(context, "文件复制成功", Toast.LENGTH_SHORT).show();
                    return outputFile;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "文件复制出错");
                //Toast.makeText(context, "文件复制出错", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "没有可用的SD卡目录");
                //Toast.makeText(context, "没有可用的SD卡目录", Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }
}
