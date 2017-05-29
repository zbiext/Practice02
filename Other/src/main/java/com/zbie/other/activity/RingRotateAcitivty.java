package com.zbie.other.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.zbie.other.R;
import com.zbie.other.view.RingRotateView;

/**
 * Created by 涛 on 2017/5/5 0005.
 * 项目名           Practice02
 * 包名             com.zbie.other.activity
 * 创建时间         2017/05/05 23:43
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            小球在圆圈内滚动
 */
public class RingRotateAcitivty extends AppCompatActivity {

    private Runnable mRunnable;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ringrotate_activity);
        ((RingRotateView) findViewById(R.id.ringrotate_view_id)).startAnimation();
        //finish();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                RingRotateAcitivty.this.finish();
                RingRotateAcitivty.this.startActivity(new Intent(RingRotateAcitivty.this, TempContrlActivity.class));
            }
        };
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 5000);
        // TODO: 2017/5/5 有个疑问：界面一出现，就马上按返回键(的默认逻辑是finish方法)，可是界面没有立即销毁(onDestroy)，handler的消息是已经发送出去了
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(mRunnable);
    }
}
