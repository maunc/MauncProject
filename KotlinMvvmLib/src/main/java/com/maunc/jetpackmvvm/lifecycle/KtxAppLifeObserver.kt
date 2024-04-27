package com.maunc.jetpackmvvm.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

object KtxAppLifeObserver : LifecycleObserver {

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
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