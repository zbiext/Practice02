package com.zbie.wavesidebardemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbie.wavesidebardemo.R;
import com.zbie.wavesidebardemo.bean.City;
import com.zbie.wavesidebardemo.turborecyclerviewhelper_lib.BaseTurboAdapter;
import com.zbie.wavesidebardemo.turborecyclerviewhelper_lib.BaseViewHolder;

import java.util.List;

/**
 * Created by 涛 on 2017/5/8 0008.
 * 项目名           Practice02
 * 包名             com.zbie.wavesidebardemo.adapter
 * 创建时间         2017/05/08 20:07
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class CityAdapter extends BaseTurboAdapter<City, BaseViewHolder> {

    private static final String TAG = "CityAdapter";

    public CityAdapter(Context context) {
        super(context);
    }

    public CityAdapter(Context context, List<City> data) {
        super(context, data);
    }

    @Override
    protected int getDefItemViewType(int position) {
        City city = getItem(position);
        //Log.d(TAG + 1111, "city.type === " + city.type);
        Log.d(TAG + 1111, "city === " + city);
        return city.type;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new CityHolder(inflateItemView(R.layout.item_city, parent));
        else
            return new PinnedHolder(inflateItemView(R.layout.item_pinned_header, parent));
    }

    @Override
    protected void convert(BaseViewHolder holder, City item) {
        if (holder instanceof CityHolder) {
            ((CityHolder) holder).city_name.setText(item.name);
        } else {
            String letter = item.pys.substring(0, 1);
            ((PinnedHolder) holder).city_tip.setText(letter);
        }
    }

    public int getLetterPosition(String letter) {
        Log.d(TAG, "letter === " + letter);
        for (int i = 0; i < getData().size(); i++) {
            City city = getData().get(i);
            Log.d(TAG, "city === " + city.name + ", city.type === " + city.type + ", city.pys === " + city.pys);
            if (getData().get(i).type == 1 && getData().get(i).pys.equals(letter)) {
                return i;
            }
        }
        return -1;
    }

    private class CityHolder extends BaseViewHolder {

        TextView city_name;

        public CityHolder(View view) {
            super(view);
            city_name = findViewById(R.id.city_name);
        }
    }

    private class PinnedHolder extends BaseViewHolder {

        TextView city_tip;

        public PinnedHolder(View view) {
            super(view);
            city_tip = findViewById(R.id.city_tip);
        }
    }
}
