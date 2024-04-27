package com.maunc.jetpackmvvm.network.manager

import com.kunminx.architecture.ui.callback.UnPeekLiveData

class NetworkStateManager private constructor() {

    val mNetworkState = UnPeekLiveData<Boolean>()

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }

}