package com.zbie.magicindicatordemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;
import android.view.View;
import android.widget.Toast;

import com.zbie.magicindicatordemo.zoomhoverview_lib.ZoomHoverAdapterImp;
import com.zbie.magicindicatordemo.zoomhoverview_lib.ZoomHoverView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo
 * 创建时间         2017/05/06 01:55
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class ZoomHoverActivity extends BaseActivity {

    private static final String       TAG   = "ZoomHoverActivity";
    private              List<String> mList = new ArrayList<>();
    private ZoomHoverAdapterImp mAdapter;
    private ZoomHoverView       mZoomHoverView;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_zoom_hover;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mZoomHoverView = (ZoomHoverView) findViewById(R.id.zoom_hover_view);
        mList.add("test 1");
        mList.add("test 2");
        mList.add("test 3");
        mList.add("test 4");
        mList.add("test 5");
        mList.add("test 6");
        mList.add("test 7");
        mList.add("test 8");
        mAdapter = new ZoomHoverAdapterImp(mList);
        final SimpleArrayMap<Integer, Integer> map = new SimpleArrayMap<>();
        map.put(0, 2);
        mAdapter.setSpan(map);
        mZoomHoverView.setAdapter(mAdapter);

        mZoomHoverView.setOnZoomAnimatorListener(new ZoomHoverView.OnZoomAnimatorListener() {
            @Override
            public void onZoomInStart(View view) {
                view.setBackground(getResources().getDrawable(android.R.drawable.dialog_holo_light_frame));
            }

            @Override
            public void onZoomInEnd(View view) {

            }

            @Override
            public void onZoomOutStart(View view) {

            }

            @Override
            public void onZoomOutEnd(View view) {
                view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        });

        mZoomHoverView.setOnItemSelectedListener(new ZoomHoverView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position) {
                Toast.makeText(ZoomHoverActivity.this,"selected position="+position,Toast.LENGTH_SHORT).show();
            }
        });

        mZoomHoverView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mZoomHoverView.setSelectedItem(1);
            }
        },2000);

        mZoomHoverView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mZoomHoverView.setSelectedItem(3);
            }
        },3000);

        mZoomHoverView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mZoomHoverView.setSelectedItem(0);
            }
        },4000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
