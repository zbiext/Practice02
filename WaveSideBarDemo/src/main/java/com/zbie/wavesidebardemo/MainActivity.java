package com.zbie.wavesidebardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zbie.wavesidebardemo.adapter.CityAdapter;
import com.zbie.wavesidebardemo.bean.City;
import com.zbie.wavesidebardemo.decoration.PinnedHeaderDecoration;
import com.zbie.wavesidebardemo.view.WaveSideBarView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView    mRecyclerView;
    private WaveSideBarView mWaveSideBarView;
    private CityAdapter     adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mWaveSideBarView = (WaveSideBarView) findViewById(R.id.side_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(decoration);
        int i = 1 << 1;
        Log.d(TAG, "onCreate: 1 << 5  = " + i);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Type             listType = new TypeToken<ArrayList<City>>() {}.getType();
                Gson             gson     = new Gson();
                final List<City> list     = gson.fromJson(City.DATA, listType);
                for (City city: list) {
                    Log.d(TAG, "city === " + city.name + ", city.type === " + city.type + ", city.pys === " + city.pys);
                }
                Collections.sort(list, new LetterComparator());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new CityAdapter(MainActivity.this, list);
                        mRecyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();

        mWaveSideBarView.setOnTouchLetterChangedListener(new WaveSideBarView.OnTouchLetterChangedListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = adapter.getLetterPosition(letter);
                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });
    }

    private class LetterComparator implements Comparator<City> {
        @Override
        public int compare(City l, City r) {
            if (l == null || r == null) {
                return 0;
            }
            String lhsSortLetters = l.pys.substring(0, 1).toUpperCase();
            String rhsSortLetters = r.pys.substring(0, 1).toUpperCase();
            if (lhsSortLetters == null || rhsSortLetters == null) {
                return 0;
            }
            return lhsSortLetters.compareTo(rhsSortLetters);
        }
    }
}
