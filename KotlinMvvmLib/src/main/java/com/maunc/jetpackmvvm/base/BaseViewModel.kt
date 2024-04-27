package com.maunc.jetpackmvvm.base

import androidx.lifecycle.ViewModel
import com.maunc.jetpackmvvm.livedata.BooleanLiveData

open class BaseViewModel : ViewModel() {

    val mediumFace = BooleanLiveData() //中等字重

    init {
        mediumFace.value = true
    }

}