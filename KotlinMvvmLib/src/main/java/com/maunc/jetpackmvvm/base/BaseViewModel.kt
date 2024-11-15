package com.maunc.jetpackmvvm.base

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseViewModel<M : BaseModel?> : ViewModel(), BaseLifecycle {
    var model: M? = null

    init {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        val actualTypeArguments = parameterizedType.actualTypeArguments
        val mClass = actualTypeArguments[0] as Class<M>
        try {
            model = mClass.newInstance()
        } catch (e: IllegalAccessException) {
            Log.e("BaseViewModel", e.toString())
        } catch (e: InstantiationException) {
            Log.e("BaseViewModel", e.toString())
        }
    }

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {
    }

    override fun onCreate() {
    }

    override fun onDestroy() {
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

}