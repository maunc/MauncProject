package com.maunc.jetpackmvvm.receive

import com.kunminx.architecture.ui.callback.UnPeekLiveData

class NetStateManager private constructor() {

    val mNetworkState = UnPeekLiveData<Boolean>()

    companion object {
        val instance: NetStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetStateManager()
        }
    }
}