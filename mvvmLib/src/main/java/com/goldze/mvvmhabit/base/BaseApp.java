package com.goldze.mvvmhabit.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.goldze.mvvmhabit.message.receiver.LifeReceiver;
import com.goldze.mvvmhabit.message.receiver.NetReceiver;

import com.goldze.mvvmhabit.message.receiver.ScreenReceiver;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okserver.OkDownload;

/**
 * Created by goldze on 2017/6/15.
 */
public class BaseApp extends Application {

    private static Application instance;

    private static final String TAG = "BaseApp";

    public static Application getInstance() {
        if (instance == null) {
            throw new NullPointerException("please inherit BaseApplication or call setApplication.");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        super.onCreate();
        initApp();
        initOkGo();
        initCrash();
        initReceiver();
    }

    @Override
    public void onTerminate() {
        Log.e(TAG, "onTerminate");
        unReceiver();
        super.onTerminate();
    }

    /**
     * 当主工程没有继承BaseApplication时，可以使用setApp设置全局instance
     */
    public static void setApp(Application application) {
        instance = application;
    }

    private void initApp() {
        instance = this;
        //注册监听每个activity的生命周期,便于堆栈式管理
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
                BaseAppManager.getAppManager().addActivity(activity);
                Log.e(TAG, activity.getClass().getSimpleName() + "-->onCreate");
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                Log.e(TAG, activity.getClass().getSimpleName() + "-->onStart");
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                Log.e(TAG, activity.getClass().getSimpleName() + "-->onResume");
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                Log.e(TAG, activity.getClass().getSimpleName() + "-->onPause");
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                Log.e(TAG, activity.getClass().getSimpleName() + "-->onStop");
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                Log.e(TAG, activity.getClass().getSimpleName() + "-->SaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                BaseAppManager.getAppManager().removeActivity(activity);
                Log.e(TAG, activity.getClass().getSimpleName() + "-->onDestroy");
            }
        });
    }

    private void initOkGo() {
        OkGo.getInstance().init(this);
        OkDownload.getInstance().getThreadPool().setCorePoolSize(2);
        /*应用启动的时候恢复任务*/
        OkDownload.getInstance().getThreadPool().execute(() -> OkDownload.restore(
                DownloadManager.getInstance().getAll()));
    }

    private void initCrash() {
        BaseCrashHandler.getInstance().init();
    }

    private NetReceiver netReceiver = null;

    private ScreenReceiver screenReceiver = null;

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private void initReceiver() {
        //添加LifecycleObserver感知前后台
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new LifeReceiver());
        netReceiver = new NetReceiver();
        IntentFilter netFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netReceiver, netFilter);
        screenReceiver = new ScreenReceiver();
        IntentFilter screenFilter = new IntentFilter();
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
        screenFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenReceiver, screenFilter);
    }

    private void unReceiver() {
        if (netReceiver != null) {
            unregisterReceiver(netReceiver);
        }
        if (screenReceiver != null) {
            unregisterReceiver(screenReceiver);
        }
    }
}
