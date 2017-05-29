package com.zbie.handlertest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by 涛 on 2017/05/29.
 * 包名             com.zbie.handlertest
 * 创建时间         2017/05/29 23:16
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            子线程 向 主线程发送消息
 */
public class HandlerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "HandlerActivity";

    private Handler mHandler = new Handler();
    private Runnable mUpdateThread = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run: UpdateThread");
            mHandler.postDelayed(mUpdateThread, 3000);
            Log.d(TAG, "thread: " + Thread.currentThread());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "main thread: " + Thread.currentThread());
        setContentView(R.layout.activity_handler);
        findViewById(R.id.Btn_start).setOnClickListener(this);
        findViewById(R.id.Btn_stop).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Btn_start:
                mHandler.post(mUpdateThread);
                break;
            case R.id.Btn_stop:
                mHandler.removeCallbacks(mUpdateThread);
                break;
        }
    }
}
