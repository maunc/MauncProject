package com.maunc.jetpackmvvm.receive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.blankj.utilcode.util.NetworkUtils

class NetReceive : BroadcastReceiver() {

    private var isInit = true

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            if (!isInit) {
                if (!NetworkUtils.isConnected()) {
                    //收到没有网络时判断之前的值是不是有网络，如果有网络才提示通知 ，防止重复通知
                    NetStateManager.instance.mNetworkState.value?.let {
                        if (it) {
                            //没网
                            NetStateManager.instance.mNetworkState.value = false
                        }
                        return
                    }
                    NetStateManager.instance.mNetworkState.value = false
                } else {
                    //收到有网络时判断之前的值是不是没有网络，如果没有网络才提示通知 ，防止重复通知
                    NetStateManager.instance.mNetworkState.value?.let {
                        if (!it) {
                            //有网络了
                            NetStateManager.instance.mNetworkState.value = true
                        }
                        return
                    }
                    NetStateManager.instance.mNetworkState.value = true
                }
            }
            isInit = false
        }
    }
}