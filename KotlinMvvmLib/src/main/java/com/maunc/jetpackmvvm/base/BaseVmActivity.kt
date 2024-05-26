package com.maunc.jetpackmvvm.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.maunc.jetpackmvvm.ext.getVmClazz
import com.maunc.jetpackmvvm.receive.NetStateManager
import com.maunc.jetpackmvvm.ext.inflateBindingWithGeneric
import com.maunc.jetpackmvvm.receive.ScreenStateManager

abstract class BaseVmActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    lateinit var mViewModel: VM

    lateinit var mDatabind: DB

    /**
     * 加载view
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    /**
     * 网络变化监听
     */
    abstract fun onNetworkStateChanged(netState: Boolean)

    abstract fun onScreenStateChanged(screenState: Boolean)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabind = inflateBindingWithGeneric(layoutInflater)
        setContentView(mDatabind.root)
        mViewModel = createViewModel()
        initView(savedInstanceState)
        createObserver()
        NetStateManager.instance.mNetworkState.observeInActivity(this, Observer {
            onNetworkStateChanged(it)
        })
        ScreenStateManager.instance.mScreenState.observeInActivity(this, Observer {
            onScreenStateChanged(it)
        })
    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

}