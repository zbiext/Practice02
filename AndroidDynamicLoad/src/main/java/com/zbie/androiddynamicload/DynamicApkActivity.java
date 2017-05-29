package com.zbie.androiddynamicload;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zbie.androiddynamicload.util.PluginManager;
import com.zbie.androiddynamicload.util.StorageManager;

import java.io.File;
import java.io.InputStream;

/**
 * Created by 涛 on 2017/5/5 0005.
 * 项目名           Practice02
 * 包名             com.zbie.androiddynamicload
 * 创建时间         2017/05/05 23:21
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class DynamicApkActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mTargetView;

    private File mPluginFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_apk);

        mTargetView = (ImageView) findViewById(R.id.iv_target);

        findViewById(R.id.btn_copy_plugin).setOnClickListener(this);
        findViewById(R.id.btn_load_plugin).setOnClickListener(this);
        findViewById(R.id.btn_load_portrait).setOnClickListener(this);
        findViewById(R.id.btn_load_qrcode).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_copy_plugin:
                InputStream inputStream = getResources().openRawResource(R.raw.pluginold);
                mPluginFile = StorageManager.copyFileToAppDir(getApplicationContext(), inputStream, "pluginold.apk");
                break;
            case R.id.btn_load_plugin:
                if (mPluginFile != null) {
                    PluginManager.loadPluginFile(getApplicationContext(), mPluginFile.getAbsolutePath());
                } else {
                    Toast.makeText(this, "SD卡上没找到插件文件", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_load_portrait:
                if (mPluginFile != null) {
                    int portraitResId = PluginManager.getPluginMipmapId(getApplicationContext(), mPluginFile.getAbsolutePath(), "portrait");
                    if (portraitResId != -1) {
                        mTargetView.setImageResource(portraitResId);
                    } else {
                        Toast.makeText(this, "没有找到portrait资源", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "插件未加载", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_load_qrcode:
                if (mPluginFile != null) {
                    int portraitResId = PluginManager.getPluginMipmapId(getApplicationContext(), mPluginFile.getAbsolutePath(), "wechat");
                    if (portraitResId != -1) {
                        mTargetView.setImageResource(portraitResId);
                    } else {
                        Toast.makeText(this, "没有找到wechat资源", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "插件未加载", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}