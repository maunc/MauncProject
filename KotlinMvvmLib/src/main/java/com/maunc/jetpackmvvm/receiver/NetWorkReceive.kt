package com.maunc.jetpackmvvm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.blankj.utilcode.util.NetworkUtils
import com.kunminx.architecture.ui.callback.UnPeekLiveData

class NetWorkReceiver : BroadcastReceiver() {

    private var isInit = true

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            if (isInit) {
                isInit = false
                return
            }
            if (!NetworkUtils.isConnected()) {
                //收到没有网络时判断之前的值是不是有网络，如果有网络才提示通知 ，防止重复通知
                NetWorkStateManager.instance.mNetworkState.value?.let {
                    if (it) {
                        NetWorkStateManager.instance.mNetworkState.value = false
                    }
                    return
                }
                NetWorkStateManager.instance.mNetworkState.value = false
            } else {
                //收到有网络时判断之前的值是不是没有网络，如果没有网络才提示通知 ，防止重复通知
                NetWorkStateManager.instance.mNetworkState.value?.let {
                    if (!it) {
                        NetWorkStateManager.instance.mNetworkState.value = true
                    }
                    return
                }
                NetWorkStateManager.instance.mNetworkState.value = true
            }
        }
    }
}

class NetWorkStateManager private constructor() {

    val mNetworkState = UnPeekLiveData<Boolean>()

    companion object {
        val instance: NetWorkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetWorkStateManager()
        }
    }
}