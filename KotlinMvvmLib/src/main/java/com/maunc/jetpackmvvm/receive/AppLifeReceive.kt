package com.maunc.jetpackmvvm.receive

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

object AppLifeReceive : LifecycleObserver {

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        AppLifeReceive.callback = callback
    }

    //在前台
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onForeground() {
        callback?.setState(true)
    }

    //在后台
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onBackground() {
        callback?.setState(false)
    }

    interface Callback {
        fun setState(state: Boolean)
    }

}