package com.zbie.magicindicatordemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.zbie.magicindicatordemo.view.NumberProgressBar;

/**
 * Created by 涛 on 2017/05/06.
 * 包名             com.zbie.magicindicatordemo
 * 创建时间         2017/05/06 0:58
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class SplashActivity extends AppCompatActivity implements NumberProgressBar.OnProgressBarListener {

    private static final String TAG = "SplashActivity";
    private NumberProgressBar loadProgress;
    private ImageView         mIvLogo;
    private LoadAsyncTask     mLoadAsyncTask;
    //    public static int sysAppSize = 0; // 用sp保存


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mIvLogo = (ImageView) findViewById(R.id.load_iv_logo);
        loadProgress = (NumberProgressBar) findViewById(R.id.load_progressbar);
        loadProgress.setOnProgressBarListener(this);
        // loadProgress.setPrefix("**");
        // loadProgress.setSuffix("%@");
        // loadProgress.setTextSize(20);
        // loadProgress.setTextColor(Color.WHITE);
        loadProgress.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Visible);
        // loadProgress.setReachedBarHeight(10);
        // loadProgress.setUnreachedBarHeight(5);
        mLoadAsyncTask = new LoadAsyncTask();
        mLoadAsyncTask.execute();
    }

    @Override
    public void onProgressChange(int current, int max) {

    }

    /** 后台任务,预加载一些app信息数据 */
    private class LoadAsyncTask extends AsyncTask<Void, Integer, Void> {

        public LoadAsyncTask() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < 100; i++) {
                if (i % 3 == 0) {
                    SystemClock.sleep(100);
                } else if (i % 6 == 0) {
                    SystemClock.sleep(80);
                }
                publishProgress((int) (100.0f * i / (100 - 1)));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onFinished("结束了！！！");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            onProgress(values[0]);
        }

        //@Override
        //protected List<AppInfo> doInBackground(Void... params) {
        //    List<AppInfo> list             = new ArrayList<AppInfo>();
        //    List<AppInfo> sysAppInfoList   = new ArrayList<AppInfo>();
        //    List<AppInfo> thirdAppInfoList = new ArrayList<AppInfo>();
        //    // 获得所有安装信息
        //    List<PackageInfo> installedPackages = mContext.getPackageManager().getInstalledPackages(
        //            PackageManager.GET_DISABLED_COMPONENTS
        //                    | PackageManager.GET_ACTIVITIES
        //                    | PackageManager.GET_RECEIVERS
        //                    | PackageManager.GET_INSTRUMENTATION
        //                    | PackageManager.GET_SERVICES);
        //    // Log.d(TAG, "installedPackages.size() === " + installedPackages.size());
        //    int sysAppSize = 0;
        //    for (int i = 0; i < installedPackages.size(); i++) {
        //        PackageInfo packageInfo = installedPackages.get(i);
        //        // app分类
        //        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) { // 系统app
        //            sysAppSize++;
        //            //                     Log.d(TAG, "填充第" + sysAppSize + "个系统app信息 :::" + packageInfo.packageName);
        //            sysAppInfoList.add(Utils.fillAppInfo(packageInfo, mContext));
        //            SystemClock.sleep(10);
        //        } else if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { // 第三方app
        //            // Log.d(TAG, "填充第" + thirdAppSize + "个第三方app信息");
        //            thirdAppInfoList.add(Utils.fillAppInfo(packageInfo, mContext));
        //            SystemClock.sleep(8);
        //        }
        //        publishProgress((int) (100.0f * i / (installedPackages.size() - 1)));
        //    }
        //    Utils.saveSp(SplashActivity.this, Utils.SYSAPPCOUNT, sysAppSize);
        //    list.addAll(sysAppInfoList);
        //    list.addAll(thirdAppInfoList);
        //    return list;
        //}

        //@Override
        //protected void onPostExecute(List<AppInfo> appInfos) {
        //    onFinished(appInfos);
        //}

        //@Override
        //protected void onProgressUpdate(Integer[] progress) {
        //    onProgress(progress[0]);
        //}
    }

    private void onFinished(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

    private void onProgress(int paramInt) {
        // loadProgress.incrementProgressBy(paramInt);
        loadProgress.setProgress(paramInt);
        // animationStart();
    }
}