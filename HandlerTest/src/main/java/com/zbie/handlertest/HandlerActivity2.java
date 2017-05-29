package com.zbie.handlertest;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.handlertest
 * 创建时间         2017/05/06 00:03
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            主线程 向 子线程发送消息
 */
public class HandlerActivity2 extends AppCompatActivity {

    private static final String TAG = "HandlerActivity2";

    private static final String UPPER_NUM = "upper";

    EditText  etNum;
    CalThread mCalThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_handler);

        Log.d(TAG + "hand", "onCreate: currentThread " + Thread.currentThread());
        Log.d(TAG + "hand", "onCreate: currentThread().getId() " + Thread.currentThread().getId());

        HandlerThread handlerThread = new HandlerThread("handler_thread");
        handlerThread.start();

        MyHandler myHandler = new MyHandler(handlerThread.getLooper());
        Message   msg       = myHandler.obtainMessage();
        msg.what = 292;

        Bundle bundle = new Bundle();
        bundle.putInt("age", 20);
        bundle.putString("name", "zbie");
        msg.setData(bundle);
        msg.sendToTarget();

        etNum = (EditText) findViewById(R.id.etNum);
        mCalThread = new CalThread();
        mCalThread.start();
    }

    public void cal(View v) {
        Message msg = new Message();
        msg.what = 291;
        Bundle bundle = new Bundle();
        bundle.putInt(UPPER_NUM, Integer.parseInt(TextUtils.isEmpty(etNum.getText().toString()) ? 0+"" : etNum.getText().toString()));
        msg.setData(bundle);
        mCalThread.mHandler.sendMessage(msg);
    }

    private class CalThread extends Thread {

        public Handler mHandler;

        @Override
        public void run() {
            //super.run();
            // 构建一个Looper
            Looper.prepare();
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    //super.handleMessage(msg);
                    Log.d(TAG, "msg.what = " + msg.what);
                    Log.d(TAG, "is mainThread? no! ---- " + getLooper().getThread());
                    Log.d(TAG + "hand11", "onCreate: " + Thread.currentThread());
                    Log.d(TAG + "hand11", "onCreate: " + Thread.currentThread().getId());
                    if (msg.what == 291) {
                        int                upper = msg.getData().getInt(UPPER_NUM);
                        final ArrayList<Integer> nums = new ArrayList<>();
                        outer:
                        for (int i = 2; i <= upper; i++) {
                            for (int j = 2; j < Math.sqrt(i); j++) {
                                if (i != 2 && i%j == 0) {
                                    continue outer;
                                }
                            }
                            nums.add(i);
                        }
                        //region 关于 子线程中弹toast 的两篇文章
                        // Android中Toast如何在子线程中调用
                        // http://blog.csdn.net/qq_28725503/article/details/50763028
                        // Android开发之在子线程中使用Toast
                        // http://www.cnblogs.com/liyiran/p/4635676.html
                        // Android开发之在子线程中使用Toast
                        // http://blog.csdn.net/zlb_lover/article/details/53050020
                        //endregion
                        Toast.makeText(HandlerActivity2.this, nums.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            };
            // 加载这个Looper
            Looper.loop();
        }
    }

    private class MyHandler extends Handler {
        public MyHandler() {
        }

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            Log.d(TAG, "msg.what = " + msg.what);
            Log.d(TAG, "is mainThread? yse! ---- " + getLooper().getThread());
            Bundle bundle = msg.getData();
            int    age    = bundle.getInt("age");
            String name   = bundle.getString("name");
            Log.d(TAG, "handleMessage: age is " + age + ",name is " + name);
            Log.d(TAG + "hand", "onCreate: " + Thread.currentThread());
            Log.d(TAG + "hand", "onCreate: " + Thread.currentThread().getId());
        }
    }
}
