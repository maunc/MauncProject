package com.maunc.jetpackmvvm

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.maunc.jetpackmvvm.base.BaseAppManager
import com.maunc.jetpackmvvm.base.BaseCrashHandler
import com.maunc.jetpackmvvm.receive.AppLifeReceive
import com.maunc.jetpackmvvm.receive.NetReceive
import com.maunc.jetpackmvvm.receive.ScreenReceiver

open class BaseApp : Application(), ViewModelStoreOwner {

    companion object {
        lateinit var instance: BaseApp
    }

    private lateinit var mAppViewModelStore: ViewModelStore

    private var mFactory: ViewModelProvider.Factory? = null

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        mAppViewModelStore = ViewModelStore()
        initReceiver()
        BaseCrashHandler.getInstance().init()
    }

    private fun initReceiver() {
        registerReceiver(NetReceive(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        val screenFilter = IntentFilter()
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF)
        screenFilter.addAction(Intent.ACTION_SCREEN_ON)
        registerReceiver(ScreenReceiver(), screenFilter)
        //注册Activity的监听
        registerActivityLifecycleCallbacks(KtxLifeCycleCallBack())
        //监听app前后台
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifeReceive)
    }

    fun getApp(): Application {
        return instance
    }

    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }
}

class KtxLifeCycleCallBack : Application.ActivityLifecycleCallbacks {

    companion object {
        private const val TAG = "KtxLifeCycleCallBack"
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        BaseAppManager.pushActivity(activity)
        Log.d(TAG, "onActivityCreated: ${activity.localClassName}")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(TAG, "onActivityStarted: ${activity.localClassName}")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(TAG, "onActivityResumed: ${activity.localClassName}")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d(TAG, "onActivityPaused: ${activity.localClassName}")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(TAG, "onActivityDestroyed: ${activity.localClassName}")
        BaseAppManager.popActivity(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(TAG, "onActivityStopped: ${activity.localClassName}")
    }

}