package com.zbie.other.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.zbie.other.R;

/**
 * Created by 涛 on 2017/05/06.
 * 包名             com.zbie.other.activity
 * 创建时间         2017/05/06 0:08
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            TODO 有个Handler取消问题待解决 已解决
 *                 星星 闪亮的两种方式
 */
public class MainActivity extends AppCompatActivity {

    private Runnable mRunnable;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //finish();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                MainActivity.this.finish();
                MainActivity.this.startActivity(new Intent(MainActivity.this, ProgressActivity.class));
            }
        };
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 5000);
        // TODO: 2017/5/5 有个疑问：界面一出现，就马上按返回键(的默认逻辑是finish方法)，可是界面没有立即销毁(onDestroy)，handler的消息是已经发送出去了
        // 问题：怎么回收handler已发送的message(或者说是Runnable)？
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(mRunnable);
    }
}
