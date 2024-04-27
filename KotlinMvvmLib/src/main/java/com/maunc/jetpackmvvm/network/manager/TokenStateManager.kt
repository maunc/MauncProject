package com.maunc.jetpackmvvm.network.manager

import com.kunminx.architecture.ui.callback.UnPeekLiveData

class TokenStateManager private constructor() {

    val mTokenState = UnPeekLiveData<Boolean>()

    companion object {
        val instance: TokenStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            TokenStateManager()
        }
    }

}