package com.maunc.jetpackmvvm.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.maunc.jetpackmvvm.ext.getVmClazz
import com.maunc.jetpackmvvm.ext.inflateBindingWithGeneric
import com.maunc.jetpackmvvm.receiver.NetWorkStateManager
import com.maunc.jetpackmvvm.utils.LogUtils

abstract class BaseFragment<VM : BaseViewModel<*>, DB : ViewDataBinding> : Fragment() {

    private val TAG: String = this.javaClass.simpleName

    lateinit var mViewModel: VM

    //该类绑定的ViewDataBinding
    private var _binding: DB? = null
    val mDatabind: DB get() = _binding!!

    lateinit var mActivity: AppCompatActivity

    private val handler = Handler()
    private var isFirst: Boolean = true //是否第一次加载

    /**
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载
     */
    abstract fun initData()

    /**
     * 加载后返回Fragment回调
     */
    abstract fun onRestart()

    /**
     * 创建观察者
     */
    abstract fun createObserver()

    /**
     * 网络变化监听
     */
    abstract fun onNetworkStateChanged(netState: Boolean)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logLifecycle("onAttach")
        mActivity = context as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logLifecycle("onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        logLifecycle("onCreateView")
        _binding = inflateBindingWithGeneric(inflater, container, false)
        return mDatabind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logLifecycle("onViewCreated")
        isFirst = true
        mViewModel = createViewModel()
        initView(savedInstanceState)
        createObserver()
    }

    override fun onStart() {
        super.onStart()
        logLifecycle("onStart")
        if (isFirst) {
            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
            handler.postDelayed({
                initData()
                //在Fragment中，只有懒加载过了才能开启网络变化监听
                NetWorkStateManager.instance.mNetworkState.observeInFragment(this) {
                    //不是首次订阅时调用方法，防止数据第一次监听错误
                    if (!isFirst) {
                        onNetworkStateChanged(it)
                    }
                }
                isFirst = false
            }, lazyLoadTime())
        }
    }

    override fun onResume() {
        super.onResume()
        logLifecycle("onResume")
        if (!isFirst) {
            onRestart()
        }
        isFirst = false
    }

    override fun onPause() {
        super.onPause()
        logLifecycle("onPause")
    }

    override fun onStop() {
        super.onStop()
        logLifecycle("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logLifecycle("onDestroyView")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        logLifecycle("onDestroy")
        isFirst = true
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDetach() {
        super.onDetach()
        logLifecycle("onDetach")
    }

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     */
    open fun lazyLoadTime(): Long {
        return 300
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

    private fun logLifecycle(lifecycle: String) {
        LogUtils.i(TAG, lifecycle)
    }
}