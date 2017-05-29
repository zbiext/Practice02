package com.zbie.androiddynamicload;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by 涛 on 2017/5/29 0029.
 * 项目名           Practice02
 * 包名             com.zbie.androiddynamicload
 * 创建时间         2017/05/29 22:06
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public final class DexTest {

    private static final String TAG             = "DexTest";
    private final static String APP_DIR         = "DexTest";
    private static       String mPluginFilePath = "";
    private static Context mContext;

    private DexTest() {
    }

    /**
     * 获得apk(dex)缓存的sd卡目录
     *
     * @return
     */
    private static File getSdcardAppDir() {
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
     * apk(dex)copy到sd卡目录
     *
     * @param inputStream
     * @param outputFileName
     * @return
     */
    private static void copyFileToAppDir(InputStream inputStream, String outputFileName) {
        if (inputStream != null && !TextUtils.isEmpty(outputFileName)) {
            File appDir = getSdcardAppDir();
            if (appDir != null) {
                File outputFile = new File(appDir, outputFileName);
                if (outputFile.exists()) {
                    Log.d(TAG, "文件已存在");
                    mPluginFilePath = outputFile.getAbsolutePath();
                    //return outputFile;
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
                    mPluginFilePath = outputFile.getAbsolutePath();
                    //return outputFile;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "文件复制出错");
            } else {
                Log.d(TAG, "没有可用的SD卡目录");
            }
        }
        //return null;
    }

    /**
     * 加载插件
     */
    private static void loadPluginFile() {
        AssetManager assetManager = mContext.getAssets();
        try {
            Method addAssetPathMethod = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, mPluginFilePath);
            Log.d(TAG, "加载成功");
            return;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "加载失败");
    }

    /**
     * 获取加载插件的DexClassLoader
     *
     * @return
     */
    private static DexClassLoader getDexClassLoader() {
        File           odexDir        = mContext.getDir("odex", Context.MODE_PRIVATE);
        DexClassLoader dexClassLoader = new DexClassLoader(mPluginFilePath, odexDir.getAbsolutePath(), null, mContext.getClassLoader());
        return dexClassLoader;
    }

    /**
     * 获取Apk(dex)包名信息
     *
     * @return
     */
    private static String getPluginPackageName() {
        PackageManager pm          = mContext.getPackageManager();
        PackageInfo    packageInfo = pm.getPackageArchiveInfo(mPluginFilePath, PackageManager.GET_ACTIVITIES);
        String         packageName = packageInfo == null ? "" : packageInfo.packageName;
        Log.d(TAG, "packageName: " + packageName);
        return packageName;
    }

    public static Class<?> getMd5Class(Context context) {
        mContext = context;
        // 1.先复制apk(dex)到sd卡目录下
        copyFileToAppDir(mContext.getResources().openRawResource(R.raw.pluginold), "tldongl.apk");
        // 2.加载apk(dex)插件
        loadPluginFile();
        try {
            // 3.加载你所需要的类
            Class<?> clazz = getDexClassLoader().loadClass(getPluginPackageName() + ".cppwrapper.utils.Md5");
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
