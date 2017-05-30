package com.zbie.photoviewdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbie.photoviewdemo.activity.PicassoSampleActivity;
import com.zbie.photoviewdemo.activity.RotationSampleActivity;
import com.zbie.photoviewdemo.activity.SimpleSampleActivity;
import com.zbie.photoviewdemo.activity.ViewPagerActivity;


public class LauncherActivity extends AppCompatActivity {

    public static final String[] options = {"Simple Sample", "ViewPager Sample", "Rotation Sample", "Picasso Sample"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ItemAdapter());
    }


    private static class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return ItemViewHolder.newInstance(parent);
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, final int position) {
            holder.bind(options[position]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Class c;

                    switch (position) {
                        default:
                        case 0:
                            c = SimpleSampleActivity.class;
                            break;
                        case 1:
                            c = ViewPagerActivity.class;
                            break;
                        case 2:
                            c = RotationSampleActivity.class;
                            break;
                        case 3:
                            c = PicassoSampleActivity.class;
                            break;
                    }

                    Context context = holder.itemView.getContext();
                    context.startActivity(new Intent(context, c));
                }
            });
        }

        @Override
        public int getItemCount() {
            return options.length;
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        public static ItemViewHolder newInstance(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_item, parent, false);
            return new ItemViewHolder(view);
        }

        public TextView mTextTitle;

        public ItemViewHolder(View view) {
            super(view);
            mTextTitle = (TextView) view.findViewById(R.id.title);
        }

        private void bind(String title) {
            mTextTitle.setText(title);
        }
    }
}
