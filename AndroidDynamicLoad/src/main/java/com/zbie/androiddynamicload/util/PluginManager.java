package com.zbie.androiddynamicload.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.util.Log;

import com.zbie.androiddynamicload.R;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by 涛 on 2017/5/5 0005.
 * 项目名           Practice02
 * 包名             com.zbie.androiddynamicload.util
 * 创建时间         2017/05/05 23:23
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            插件加载管理器
 */
public class PluginManager {
    private final static String TAG = "PluginManager";

    private PluginManager() {
    }

    /**
     * 获取加载插件的DexClassLoader
     *
     * @param context
     * @param pluginFilePath
     * @return
     */
    private static DexClassLoader getDexClassLoader(Context context, String pluginFilePath) {
        File odexDir = context.getDir("odex", Context.MODE_PRIVATE);
        Log.d(TAG, "odexDir = " + odexDir.getAbsolutePath());
        //dexPath：需要装载的APK或者Jar文件的路径。包含多个路径用File.pathSeparator间隔开,在Android上默认是 ":"
        //optimizedDirectory：优化后的dex文件存放目录，不能为null
        //librarySearchPath：目标类中使用的C/C++库的列表,每个目录用File.pathSeparator间隔开; 可以为 null
        //parent：该类装载器的父装载器，一般用当前执行类的装载器
        DexClassLoader dexClassLoader = new DexClassLoader(pluginFilePath, odexDir.getAbsolutePath(), null, context.getClassLoader());
        return dexClassLoader;
    }

    /**
     * 加载插件
     *
     * @param context
     * @param pluginFilePath
     */
    public static void loadPluginFile(Context context, String pluginFilePath) {
        AssetManager assetManager = context.getAssets();
        try {
            Method addAssetPathMethod = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, pluginFilePath);
            Log.d(TAG, "加载成功");
            //Toast.makeText(context, "加载成功", Toast.LENGTH_SHORT).show();
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
     * 获取插件中指定资源名字的资源id
     *
     * @param context
     * @param pluginPath
     * @param resName    资源名
     * @return
     */
    public static int getPluginMipmapId(Context context, String pluginPath, String resName) {
        DexClassLoader dexClassLoader    = getDexClassLoader(context, pluginPath);
        String         pluginPackageName = getPluginPackageName(context, pluginPath);
        try {
            Class<?> mipmapClass = dexClassLoader.loadClass(pluginPackageName + ".R$mipmap");//通过反射动态加载插件中的所有资源，包括类和资源文件等
            Field    mipmapField = mipmapClass.getDeclaredField(resName);//获取mipmap中生成的资源字段14
            int      resId       = mipmapField.getInt(R.id.class);
            Log.d(TAG, "resId: " + resId);
            return resId;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取Apk包名信息
     *
     * @param context
     * @param pluginPath 插件Apk的路径
     * @return
     */
    private static String getPluginPackageName(Context context, String pluginPath) {
        PackageManager pm          = context.getPackageManager();
        PackageInfo    packageInfo = pm.getPackageArchiveInfo(pluginPath, PackageManager.GET_ACTIVITIES);
        String         packageName = packageInfo == null ? "" : packageInfo.packageName;
        Log.d(TAG, "packageName: " + packageName);
        return packageName;
    }
}
