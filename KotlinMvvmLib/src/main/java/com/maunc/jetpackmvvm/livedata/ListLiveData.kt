package com.maunc.jetpackmvvm.livedata

import androidx.lifecycle.MutableLiveData

class ListLiveData<T> : MutableLiveData<List<T>>() {
    override fun getValue(): List<T>? {
        return super.getValue()
    }
}