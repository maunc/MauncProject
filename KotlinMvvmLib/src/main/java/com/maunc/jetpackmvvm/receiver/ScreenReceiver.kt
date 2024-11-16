package com.maunc.jetpackmvvm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 *ClsFunction：
 *CreateDate：2024/5/26
 *Author：TimeWillRememberUs
 */
class ScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (Intent.ACTION_SCREEN_OFF == action) {
            ScreenStateManager.instance.mScreenState.value = false
        } else if (Intent.ACTION_SCREEN_ON == action) {
            ScreenStateManager.instance.mScreenState.value = true
        }
    }
}

class ScreenStateManager private constructor() {

    val mScreenState = UnPeekLiveData<Boolean>()

    companion object {
        val instance: ScreenStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ScreenStateManager()
        }
    }
}