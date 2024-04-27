package com.maunc.jetpackmvvm

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.maunc.jetpackmvvm.lifecycle.KtxAppLifeObserver
import com.maunc.jetpackmvvm.lifecycle.KtxLifeCycleCallBack
import com.maunc.jetpackmvvm.receive.NetworkReceive

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
        //注册全局网络监听广播
        registerReceiver(NetworkReceive(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        //注册Activity的监听
        registerActivityLifecycleCallbacks(KtxLifeCycleCallBack())
        //监听app前后台
        ProcessLifecycleOwner.get().lifecycle.addObserver(KtxAppLifeObserver)
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