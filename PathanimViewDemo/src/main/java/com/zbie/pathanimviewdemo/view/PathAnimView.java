package com.zbie.pathanimviewdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.zbie.pathanimviewdemo.helper.PathAnimHelper;

/**
 * Created by 涛 on 2017/5/6 0006.
 * 项目名           Practice02
 * 包名             com.zbie.pathanimviewdemo.view
 * 创建时间         2017/05/06 00:20
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * Github:         https://github.com/zbiext
 * 简书:           http://www.jianshu.com/
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class PathAnimView extends View {

    protected Paint mPaint;
    protected Path  mAnimPath; // 用于绘制动画的Path
    protected Path  mSourcePath; // 需要做动画的源Path

    protected int mColorBg = Color.GRAY; // 背景色
    protected int mColorFg = Color.WHITE; // 前景色 填充色

    protected PathAnimHelper mPathAnimHelper; // Path动画工具帮助类

    protected int mPaddingLeft;
    protected int mPaddingTop;

    public PathAnimView(Context context) {
        this(context, null);
    }

    public PathAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        // 画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);

        // 动画路径只要初始化即可
        mAnimPath = new Path();

        // 初始化动画帮助类
        initPathAnimHelper();

    }

    /**
     * 初始化动画帮助类
     */
    protected void initPathAnimHelper() {
        mPathAnimHelper = getInitAnimHelper();
        // mPathAnimHelper = new PathAnimHelper(this, mSourcePath, mAnimPath, 1500, true);
    }

    /**
     * 子类可通过重写这个方法，返回自定义的AnimHelper
     *
     * @return
     */
    protected PathAnimHelper getInitAnimHelper() {
        return new PathAnimHelper(this, mSourcePath, mAnimPath);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 平移
        canvas.translate(mPaddingLeft, mPaddingTop);

        // 先绘制底，
        mPaint.setColor(mColorBg);
        canvas.drawPath(mSourcePath, mPaint);

        // 再绘制前景，mAnimPath不断变化，不断重绘View的话，就会有动画效果。
        mPaint.setColor(mColorFg);
        canvas.drawPath(mAnimPath, mPaint);
    }

    public Paint getPaint() {
        return mPaint;
    }

    public PathAnimView setPaint(Paint paint) {
        mPaint = paint;
        return this;
    }

    public Path getSourcePath() {
        return mSourcePath;
    }

    public PathAnimView setSourcePath(Path sourcePath) {
        mSourcePath = sourcePath;
        initPathAnimHelper(); // TODO 漏掉
        return this;
    }

    public int getColorBg() {
        return mColorBg;
    }

    public PathAnimView setColorBg(int colorBg) {
        mColorBg = colorBg;
        return this;
    }

    public int getColorFg() {
        return mColorFg;
    }

    public PathAnimView setColorFg(int colorFg) {
        mColorFg = colorFg;
        return this;
    }

    public PathAnimHelper getPathAnimHelper() {
        return mPathAnimHelper;
    }

    public PathAnimView setPathAnimHelper(PathAnimHelper pathAnimHelper) {
        mPathAnimHelper = pathAnimHelper;
        return this;
    }

    public Path getAnimPath() {
        return mAnimPath;
    }

    /**
     * 设置动画是否无限循环
     */
    public PathAnimView setAnimInfinite(boolean infinite) {
        mPathAnimHelper.setInfinite(infinite);
        return this;
    }

    /**
     * 设置动画播放时长
     */
    public PathAnimView setAnimTime(long animTime) {
        mPathAnimHelper.setAnimTime(animTime);
        return this;
    }

    /**
     * 执行循环动画
     */
    public void startAnim() {
        mPathAnimHelper.startAnim();
    }

    /**
     * 停止动画
     */
    public void stopAnim() {
        mPathAnimHelper.stopAnim();
    }

    /**
     * 清除并停止动画
     */
    public void clearAnim() {
        stopAnim();
        mAnimPath.reset();
        mAnimPath.lineTo(0, 0);
        invalidate();
    }
}
