package com.zbie.other.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zbie.other.R;
import com.zbie.other.view.TempControlView;

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
 * 描述            温度旋钮自定义控件(类似音量调节)
 */
public class TempContrlActivity extends AppCompatActivity {

    private TempControlView tempControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tempcontrl_activity);

        tempControl = (TempControlView) findViewById(R.id.temp_control);
        tempControl.setTemp(15, 30, 15);
        tempControl.setOnTempChangeListener(new TempControlView.OnTempChangeListener() {
            @Override
            public void change(int temp) {
                Toast.makeText(TempContrlActivity.this, temp + "°", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
