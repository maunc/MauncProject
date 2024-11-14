package com.maunc.jetpackmvvm.receive

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.kunminx.architecture.ui.callback.UnPeekLiveData

object FrontAndBackReceive : LifecycleObserver {

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

    val mFrontAndBackState = UnPeekLiveData<Boolean>()

    companion object {
        val instance: FrontAndBackStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            FrontAndBackStateManager()
        }
    }
}