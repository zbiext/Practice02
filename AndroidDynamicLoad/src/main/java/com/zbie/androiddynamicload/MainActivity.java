package com.zbie.androiddynamicload;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.zbie.permissionwrapperdemo.util.Func;
import com.zbie.permissionwrapperdemo.util.PermissionUtil;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int    REQUEST_CODE_CONTACTS  = 1;
    private static final int    REQUEST_CODE_STORAGE   = 2;
    private static final int    REQUEST_CODE_BOTH      = 3;

    private final static int      REQUEST_PERMISSION_CODE  = 0x11;
    private final static String[] REQUEST_PERMISSION_GROUP = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE};

    private PermissionUtil.PermissionRequestObject mStoragePermissionRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission1();

        findViewById(R.id.btn_dynamic_load_apk).setOnClickListener(this);
    }

    private void checkPermission1() {
        mStoragePermissionRequest = PermissionUtil.with(this).request(WRITE_EXTERNAL_STORAGE).onAllGranted(new Func() {
            @Override
            protected void call() {
                // do nothing
            }
        }).onAnyDenied(new Func() {
            @Override
            protected void call() {
                // do nothing
            }
        }).ask(REQUEST_CODE_STORAGE);
    }

    /**
     * 6.0动态权限检测申请
     */
    private void checkPermission() {
        //检测读取存储器的权限是否授权
        if (ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)) {//向用户解释授权
                new AlertDialog.Builder(this)
                        .setTitle("授权申请")
                        .setMessage("本App需要申请读取SD卡等权限才能正常使用")
                        .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, REQUEST_PERMISSION_GROUP, REQUEST_PERMISSION_CODE);
                            }
                        })
                        .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "没有获得权限，应用可能无法正常使用", Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, REQUEST_PERMISSION_GROUP, REQUEST_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_dynamic_load_apk) {
            Intent dynamicLoadApk = new Intent(this, DynamicApkActivity.class);
            startActivity(dynamicLoadApk);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mStoragePermissionRequest != null) {
            mStoragePermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //if (requestCode == REQUEST_PERMISSION_CODE) {
        //    boolean allGranted = true;
        //    for (int grantResult : grantResults) {
        //        if (grantResult == PackageManager.PERMISSION_DENIED) {
        //            allGranted = false;
        //        }
        //    }
        //    if (!allGranted) {
        //        Toast.makeText(this, "部分权限授权失败", Toast.LENGTH_SHORT).show();
        //    }
        //}
    }
}
