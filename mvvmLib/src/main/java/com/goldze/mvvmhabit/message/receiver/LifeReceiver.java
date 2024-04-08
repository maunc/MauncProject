package com.goldze.mvvmhabit.message.receiver;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.goldze.mvvmhabit.message.bus.RxBus;
import com.goldze.mvvmhabit.message.status._LifeStatus;

/**
 * ClsFunction：是否处于前台
 * CreateDate：2024/3/20
 * Author：TimeWillRememberUs
 */
public class LifeReceiver implements LifecycleObserver {

    //在前台
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onForeground() {
        RxBus.getDefault().post(new _LifeStatus(true));
    }

    //在后台
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onBackground() {
        RxBus.getDefault().post(new _LifeStatus(false));
    }

}
