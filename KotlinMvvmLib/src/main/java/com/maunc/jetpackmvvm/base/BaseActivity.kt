package com.maunc.jetpackmvvm.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.maunc.jetpackmvvm.ext.getVmClazz
import com.maunc.jetpackmvvm.ext.inflateBindingWithGeneric
import com.maunc.jetpackmvvm.receiver.FrontAndBackStateManager
import com.maunc.jetpackmvvm.receiver.NetWorkStateManager
import com.maunc.jetpackmvvm.receiver.ScreenStateManager

abstract class BaseActivity<VM : BaseViewModel<*>, DB : ViewDataBinding> : AppCompatActivity() {

    lateinit var mViewModel: VM

    lateinit var mDatabind: DB

    var mFrontAndBackStateObserver = Observer<Boolean> {
        onFrontAndBackStateChanged(it)
    }

    /**
     * 加载view
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 创建LiveData数据观察者
     */
    open fun createObserver() {
    }

    /**
     * 网络变化监听
     */
    abstract fun onNetworkStateChanged(netState: Boolean)

    /**
     * 息屏亮屏变化监听
     */
    abstract fun onScreenStateChanged(screenState: Boolean)

    /**
     * 前后台变化监听
     */
    abstract fun onFrontAndBackStateChanged(frontAndBackState: Boolean)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabind = inflateBindingWithGeneric(layoutInflater)
        setContentView(mDatabind.root)
        mViewModel = createViewModel()
        lifecycle.addObserver(mViewModel)
        initView(savedInstanceState)
        createObserver()
        NetWorkStateManager.instance.mNetworkState.observeInActivity(this, Observer {
            onNetworkStateChanged(it)
        })
        ScreenStateManager.instance.mScreenState.observeInActivity(this, Observer {
            onScreenStateChanged(it)
        })
        //后台也要监听 用observeForever 需要release
        FrontAndBackStateManager.instance.mFrontAndBackState.observeForever(
            mFrontAndBackStateObserver
        )
    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }

    /**
     * 获取ViewModel
     */
    fun <T : BaseViewModel<*>> getViewModel(quickViewModel: Class<T>): T {
        return ViewModelProvider(this)[quickViewModel]
    }

    override fun onDestroy() {
        FrontAndBackStateManager.instance.mFrontAndBackState.removeObserver(
            mFrontAndBackStateObserver
        )
        super.onDestroy()
    }
}