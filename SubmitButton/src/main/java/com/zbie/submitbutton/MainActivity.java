package com.zbie.submitbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.zbie.submitbutton.view.SubmitButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,
        ValueAnimator.AnimatorUpdateListener,
        Animator.AnimatorListener {

    private static final String TAG = "MainActivity";
    String assetsFile = "abc1.txt";
    String srcFile    = "abc_encrypt.zb";
    String desFile    = "des.txt";

    private SubmitButton sBtnLoading;
    private SubmitButton sBtnProgress;
    private Switch       mSwitch;
    private Button       btnSucceed;
    private Button       btnFailed;
    private Button       btnReset;


    private TextView      mAnimView;
    private Button        mBtnEncrypt;
    private MyTask        task;
    private String        mPath;
    private ValueAnimator mValueAnimator;

    private AnimatorSet mSet_4 = new AnimatorSet();

    private int mCurrentRed   = -1;
    private int mCurrentGreen = -1;
    private int mCurrentBlue  = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();

        initData();
    }

    private void initView() {
        sBtnLoading = (SubmitButton) findViewById(R.id.sbtn_loading);
        sBtnProgress = (SubmitButton) findViewById(R.id.sbtn_progress);
        mSwitch = (Switch) findViewById(R.id.switch1);
        btnFailed = (Button) findViewById(R.id.btn_failed);
        btnSucceed = (Button) findViewById(R.id.btn_succeed);
        btnReset = (Button) findViewById(R.id.btn_reset);

        mAnimView = (TextView) findViewById(R.id.tv_color_backgroound);
        mBtnEncrypt = (Button) findViewById(R.id.encrypt_decode);
    }

    private void initListener() {
        sBtnLoading.setOnClickListener(this);
        sBtnProgress.setOnClickListener(this);
        mSwitch.setOnCheckedChangeListener(this);
        btnFailed.setOnClickListener(this);
        btnSucceed.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        mBtnEncrypt.setOnClickListener(this);
    }

    private void initData() {
        mPath = Environment.getExternalStorageDirectory().getPath();
        initAnimator();
        initAnimListener();
        initAnimData();
    }

    private void initAnimator() {
        mValueAnimator = ValueAnimator.ofFloat(1f, 100f);
    }

    private void initAnimListener() {
        mValueAnimator.addUpdateListener(this);
        mSet_4.addListener(this);
    }

    private void initAnimData() {
        mValueAnimator.setDuration(6 * 1000);
        ValueAnimator animator_1 = ObjectAnimator.ofFloat(mAnimView, "scaleX", 1f, 0.5f);
        ValueAnimator animator_2 = ObjectAnimator.ofFloat(mAnimView, "scaleY", 1f, 0.5f);
        ValueAnimator animator_3 = ObjectAnimator.ofFloat(mAnimView, "scaleX", 0.5f, 1f);
        ValueAnimator animator_4 = ObjectAnimator.ofFloat(mAnimView, "scaleY", 0.5f, 1f);
        AnimatorSet   set_1      = new AnimatorSet();
        set_1.play(animator_1).with(animator_2);

        AnimatorSet set_2 = new AnimatorSet();
        set_2.play(animator_3).with(animator_4);

        AnimatorSet set_3 = new AnimatorSet();
        set_3.play(set_1).before(set_2);
        set_3.setDuration(3 * 1000);

        //mSet_4 = new AnimatorSet();
        mSet_4.play(mValueAnimator).with(set_3);
        mSet_4.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sbtn_loading:
                Toast.makeText(this, "SubmitButton be clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sbtn_progress:
                if (task == null || task.isCancelled()) {
                    task = new MyTask();
                    task.execute();
                }
                break;
            case R.id.btn_succeed:
                if (mSwitch.isChecked()) {
                    sBtnProgress.doResult(true);
                } else {
                    sBtnLoading.doResult(true);
                }
                break;
            case R.id.btn_failed:
                if (mSwitch.isChecked()) {
                    sBtnProgress.doResult(false);
                } else {
                    sBtnLoading.doResult(false);
                }
                break;
            case R.id.btn_reset:
                if (mSwitch.isChecked()) {
                    if (task != null && !task.isCancelled()) {
                        task.cancel(true);
                        sBtnProgress.reset();
                    }
                } else {
                    sBtnLoading.reset();
                }
                break;
            case R.id.encrypt_decode:
                encrypt1();
                break;
        }
    }

    private void encrypt1() {
        File file_src = new File(mPath, srcFile);
        File file_des = new File(mPath, desFile);

        BufferedReader fr = null;
        BufferedWriter fw = null;

        try {
            if (!file_src.exists() && !file_des.exists()) {
                fr = new BufferedReader(new InputStreamReader(getResources().getAssets().open(assetsFile)));
                fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_src)));
            } else {
                if (file_des.exists()) {
                    Toast.makeText(this, "file_src已删除,desFile OK!", Toast.LENGTH_SHORT).show();
                    return;
                }
                fr = new BufferedReader(new FileReader(file_src));
                fw = new BufferedWriter(new FileWriter(new File(mPath, desFile)));
            }
            int ch;
            while ((ch = fr.read()) != -1) {
                ch ^= 3;
                fw.write(ch);
            }
            if (file_src.exists() && file_des.exists()) {
                file_src.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            sBtnLoading.setVisibility(View.GONE);
            sBtnProgress.setVisibility(View.VISIBLE);
            sBtnLoading.reset();
        } else {
            sBtnLoading.setVisibility(View.VISIBLE);
            sBtnProgress.setVisibility(View.GONE);
            if (task != null && !task.isCancelled()) {
                task.cancel(true);
                sBtnProgress.reset();
            }
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (animation == mValueAnimator) {
            Log.d(TAG, "object equals true");
        }
        // 获取当前动画的进度值，1~100
        float currentValue = (float) animation.getAnimatedValue();
        Log.d(TAG, "current animation value : " + currentValue);
        // 获取动画当前时间流逝的百分比，范围在0~1之间
        float fraction = animation.getAnimatedFraction();
        // 直接调用evaluateForColor()方法，通过百分比计算出对应的颜色值
        String colorResult = evaluateForColor(fraction, "#0000ff", "#ff0000");

        /**
         * 通过Color.parseColor(colorResult)解析字符串颜色值，传给ColorDrawable，创建ColorDrawable对象
         */
        /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) target.getLayoutParams();*/
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(colorResult));
        // 把ColorDrawable对象设置为target的背景
        mAnimView.setBackground(colorDrawable);
        mAnimView.invalidate();
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mAnimView.setBackground(new ColorDrawable(Color.parseColor("#0000ff")));
        mAnimView.invalidate();
        mCurrentRed = -1;
        mCurrentGreen = -1;
        mCurrentBlue = -1;
        mSet_4.start();
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    private String evaluateForColor(float fraction, String startValue, String endValue) {
        int startRed   = Integer.parseInt(startValue.substring(1, 3), 16);
        int startGreen = Integer.parseInt(startValue.substring(3, 5), 16);
        int startBlue  = Integer.parseInt(startValue.substring(5, 7), 16);
        int endRed     = Integer.parseInt(endValue.substring(1, 3), 16);
        int endGreen   = Integer.parseInt(endValue.substring(3, 5), 16);
        int endBlue    = Integer.parseInt(endValue.substring(5, 7), 16);

        // 初始化颜色的值
        if (mCurrentRed == -1) {
            mCurrentRed = startRed;
        }
        if (mCurrentGreen == -1) {
            mCurrentGreen = startGreen;
        }
        if (mCurrentBlue == -1) {
            mCurrentBlue = startBlue;
        }

        // 计算初始颜色和结束颜色之间的差值
        int redDiff   = Math.abs(startRed - endRed);
        int greenDiff = Math.abs(startGreen - endGreen);
        int blueDiff  = Math.abs(startBlue - endBlue);
        int colorDiff = redDiff + greenDiff + blueDiff;
        if (mCurrentRed != endRed) {
            mCurrentRed = getCurrentColor(startRed, endRed, colorDiff, 0, fraction);
        } else if (mCurrentGreen != endGreen) {
            mCurrentGreen = getCurrentColor(startGreen, endGreen, colorDiff, redDiff, fraction);
        } else if (mCurrentBlue != endBlue) {
            mCurrentBlue = getCurrentColor(startBlue, endBlue, colorDiff, redDiff + greenDiff, fraction);
        }
        // 将计算出的当前颜色的值组装返回
        return "#" + getHexString(mCurrentRed) + getHexString(mCurrentGreen) + getHexString(mCurrentBlue);
    }

    private int getCurrentColor(int startColor, int endColor, int colorDiff, int offset, float fraction) {
        int currentColor;
        if (startColor > endColor) {
            currentColor = (int) (startColor - (fraction * colorDiff - offset));
            if (currentColor < endColor) {
                currentColor = endColor;
            }
        } else {
            currentColor = (int) (startColor + (fraction * colorDiff - offset));
            if (currentColor > endColor) {
                currentColor = endColor;
            }
        }
        return currentColor;
    }

    private String getHexString(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

    private class MyTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            int i = 0;
            while (i <= 100) {
                if (isCancelled()) {
                    return null;
                }
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
                publishProgress(i);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean == null) {
                sBtnProgress.reset();
            }
            sBtnProgress.doResult(aBoolean);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            sBtnProgress.setProgress(values[0]);
        }
    }
}
