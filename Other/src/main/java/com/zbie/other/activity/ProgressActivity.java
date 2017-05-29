package com.zbie.other.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.zbie.other.R;
import com.zbie.other.view.ProgressView;

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
 * 描述            三层 进度加载
 */
public class ProgressActivity extends AppCompatActivity implements Handler.Callback {

    private static final String TAG = "ProgressActivity";

    private static final int MSG_PROGRESS  = 0;
    private static final int MSG_START     = 1;
    private static final int MSG_STOP      = 2;
    private static final int MSG_SPROGRESS = 3;
    private static final int MSG_DONE      = 4;

    private ProgressView progressView;
    private Handler      mHandler;
    private boolean      isStop;
    private Runnable     mRunnable;
    private Runnable     mFProRunnable;
    private Runnable     mSProRunnable;
    private Runnable     mDoneRunnable;
    private boolean      isFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_activity);
        progressView = (ProgressView) findViewById(R.id.progress_view_id);
        mHandler = new Handler(this);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                ProgressActivity.this.finish();
                ProgressActivity.this.startActivity(new Intent(ProgressActivity.this, RingRotateAcitivty.class));
            }
        };

        mFProRunnable = new Runnable() {
            @Override
            public void run() {
                Message message = mHandler.obtainMessage();
                message.what = MSG_START;
                message.sendToTarget();
                int progress = 0;
                while (!isFinish && progress <= 100 && !isStop) {
                    if (!isFinish) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = MSG_PROGRESS;
                        msg.arg1 = progress;
                        msg.sendToTarget();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        progress++;
                    } else {
                        break;
                    }
                }
                if (!isFinish) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = MSG_STOP;
                    msg.sendToTarget();
                }
            }
        };

        mSProRunnable = new Runnable() {
            @Override
            public void run() {
                int sprogress = 0;
                while (sprogress <= 100 && !isStop) {
                    if (!isFinish) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = MSG_SPROGRESS;
                        msg.arg1 = sprogress;
                        msg.sendToTarget();
                        try {
                            Thread.sleep(80);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sprogress++;
                    } else {
                        break;
                    }
                }
            }
        };

        mDoneRunnable = new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                do {
                    if (!isFinish)
                        progress = progressView.getProgress();
                    else
                        break;
                } while (progress < 100);
                if (!isFinish) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = MSG_DONE;
                    msg.sendToTarget();
                }
            }
        };

        isStop = false;

        onStart(findViewById(R.id.btn_start));
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case MSG_START:
                progressView.setVisibility(View.VISIBLE);
                break;
            case MSG_STOP:
                progressView.setVisibility(View.GONE);
                break;
            case MSG_PROGRESS:
                progressView.setProgress(message.arg1);
                break;
            case MSG_SPROGRESS:
                progressView.setSecondaryProgress(message.arg1);
                break;
            case MSG_DONE:
                Toast.makeText(this, "累死了，跑完了...", Toast.LENGTH_SHORT).show();
                //finish();
                mHandler.postDelayed(mRunnable, 5000);
                // TODO: 2017/5/5 有个疑问：界面一出现，就马上按返回键(的默认逻辑是finish方法)，可是界面没有立即销毁(onDestroy)，handler的消息是已经发送出去了
                break;
        }
        return false;
    }

    public void onStart(View view) {
        isStop = false;

        new Thread(mFProRunnable).start();

        new Thread(mSProRunnable).start();

        new Thread(mDoneRunnable).start();
    }

    public void onStop(View view) {
        isStop = true;
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_STOP;
        msg.sendToTarget();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isFinish = true;
        mHandler.removeCallbacks(mFProRunnable);
        mHandler.removeCallbacks(mSProRunnable);
        mHandler.removeCallbacks(mDoneRunnable);
        mHandler.removeCallbacks(mRunnable);
    }
}
