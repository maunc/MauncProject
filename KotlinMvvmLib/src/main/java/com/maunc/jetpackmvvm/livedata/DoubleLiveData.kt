package com.maunc.jetpackmvvm.livedata

import androidx.lifecycle.MutableLiveData

class DoubleLiveData : MutableLiveData<Double>() {
    override fun getValue(): Double {
        return super.getValue() ?: 0.0
    }
}