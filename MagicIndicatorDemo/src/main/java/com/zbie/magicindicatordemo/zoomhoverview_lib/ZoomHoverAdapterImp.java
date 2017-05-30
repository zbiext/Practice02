package com.zbie.magicindicatordemo.zoomhoverview_lib;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbie.magicindicatordemo.R;

import java.util.List;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.magicindicatordemo.zoomhoverview_lib
 * 创建时间         2017/05/06 01:51
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            ZoomHoverView的适配器 实现
 */
public class ZoomHoverAdapterImp extends ZoomHoverAdapter<String> {

    public ZoomHoverAdapterImp(List<String> list) {
        super(list);
    }

    public ZoomHoverAdapterImp(String[] datas) {
        super(datas);
    }

    @Override
    public View getView(ViewGroup parent, int position, String s) {
        View     contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_overlay_card, parent, false);
        TextView textView    = (TextView) contentView.findViewById(R.id.tv_item);
        textView.setText(s);
        return contentView;
    }
}
