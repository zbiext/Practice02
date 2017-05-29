package com.zbie.other.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zbie.other.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 涛 on 2017/5/5 0005.
 * 项目名           Practice02
 * 包名             com.zbie.other.view
 * 创建时间         2017/05/05 23:41
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class ShiningStarWithValueAnimator extends View {

    private static final String TAG = "ShiningStarWithValue";

    /**
     * 星星的坐标及大小
     */
    private static final int[][] starPosition = new int[][]{
            {80, 80, 66}, {160, 80, 80}, {240, 160, 100}, {120, 240, 120}, {360, 480, 66}, {600, 600, 120}, {720, 500, 120},
            {360, 100, 66}, {600, 160, 120}, {720, 240, 120}, {860, 80, 80}
    };

    /**
     * 星星容器
     */
    private List<Star> stars = new ArrayList<Star>();

    /**
     * 星星资源
     */
    private Bitmap bitmap1 = null;
    private Bitmap bitmap2 = null;
    private Bitmap bitmap3 = null;
    private Bitmap bitmap4 = null;
    private Bitmap[] bitmaps;

    /**
     * 画笔
     */
    private Paint paint = null;

    public ShiningStarWithValueAnimator(Context context) {
        super(context);
        init();
    }

    public ShiningStarWithValueAnimator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShiningStarWithValueAnimator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initStars();
        initAnimation();
    }

    /**
     * 初始化所有星星对象
     */
    private void initStars() {
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.star_icon_01);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.star_icon_02);
        bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.star_icon_03);
        bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.star_icon_04);
        bitmaps = new Bitmap[]{bitmap1, bitmap2, bitmap3, bitmap4};

        for (int i = 0; i < starPosition.length; i++) {
            final Star star = new Star();
            star.x = starPosition[i][0];
            star.y = starPosition[i][1];
            star.size = starPosition[i][2];
            star.bitmap = bitmaps[new Random().nextInt(bitmaps.length)];
            stars.add(star);
        }
    }

    /**
     * 初始化动画及绘制元素的对象
     */
    private void initAnimation() {
        paint = new Paint();
        paint.setAlpha(255);

        ValueAnimator scaleAnim = ValueAnimator.ofFloat(0, 255, 0);
        scaleAnim.setInterpolator(new LinearInterpolator());
        scaleAnim.setDuration(1000);
        scaleAnim.setRepeatCount(ValueAnimator.INFINITE);
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                boolean flag = false;
                for (int i = 0; i < stars.size(); i++) {
                    if (flag) {
                        stars.get(i).scale = ((float) animation.getAnimatedValue()) / 255;
                        stars.get(i).alpha = (float) animation.getAnimatedValue();
                    } else {
                        stars.get(i).scale = 1 - ((float) animation.getAnimatedValue()) / 255;
                        stars.get(i).alpha = 255 - (float) animation.getAnimatedValue();
                    }
                    flag = !flag;
                }
                postInvalidate(); // 具体绘制逻辑需要在onDraw中实现
            }
        });
        scaleAnim.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmaps != null) {
            for (int i = 0; i < stars.size(); i++) {
                canvas.save();// 这样使每一个星星的状态独立
                Rect dst = new Rect(stars.get(i).x, stars.get(i).y, stars.get(i).x + stars.get(i).size, stars.get(i).y + stars.get(i).size);
                canvas.scale(stars.get(i).scale, stars.get(i).scale, stars.get(i).x + stars.get(i).size / 2, stars.get(i).y + stars.get(i).size / 2);
                paint.setAlpha((int) stars.get(i).alpha);
                canvas.drawBitmap(stars.get(i).bitmap, null, dst, paint);
                canvas.restore();
            }
        }
    }

    private class Star {
        public int    x;
        public int    y;
        public int    size;
        public float  scale;
        public float  alpha;
        public Bitmap bitmap;
    }
}
