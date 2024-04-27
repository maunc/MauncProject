package com.maunc.jetpackmvvm.receive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.blankj.utilcode.util.NetworkUtils
import com.maunc.jetpackmvvm.network.manager.NetworkStateManager

class NetworkReceive : BroadcastReceiver() {

    var isInit = true

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            if (!isInit) {
                if (!NetworkUtils.isConnected()) {
                    //收到没有网络时判断之前的值是不是有网络，如果有网络才提示通知 ，防止重复通知
                    NetworkStateManager.instance.mNetworkState.value?.let {
                        if (it) {
                            //没网
                            NetworkStateManager.instance.mNetworkState.value = false
                        }
                        return
                    }
                    NetworkStateManager.instance.mNetworkState.value = false
                } else {
                    //收到有网络时判断之前的值是不是没有网络，如果没有网络才提示通知 ，防止重复通知
                    NetworkStateManager.instance.mNetworkState.value?.let {
                        if (!it) {
                            //有网络了
                            NetworkStateManager.instance.mNetworkState.value = true
                        }
                        return
                    }
                    NetworkStateManager.instance.mNetworkState.value = true
                }
            }
            isInit = false
        }
    }
}