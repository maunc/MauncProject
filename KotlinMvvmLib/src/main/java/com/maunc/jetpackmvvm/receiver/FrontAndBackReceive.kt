package com.maunc.jetpackmvvm.receiver

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.maunc.jetpackmvvm.livedata.BooleanLiveData

object FrontAndBackReceiver : LifecycleObserver {

    //在前台
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onForeground() {
        FrontAndBackStateManager.instance.mFrontAndBackState.value = true
    }

    //在后台
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onBackground() {
        FrontAndBackStateManager.instance.mFrontAndBackState.value = false
    }
}

class FrontAndBackStateManager private constructor() {

    val mFrontAndBackState = BooleanLiveData()

    companion object {
        val instance: FrontAndBackStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            FrontAndBackStateManager()
        }
    }
}