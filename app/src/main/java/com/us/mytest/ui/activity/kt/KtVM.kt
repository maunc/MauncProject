package com.us.mytest.ui.activity.kt

import com.maunc.jetpackmvvm.base.BaseViewModel
import com.maunc.jetpackmvvm.livedata.IntLiveData

/**
 *ClsFunction：
 *CreateDate：2024/4/27
 *Author：TimeWillRememberUs
 */
class KtVM : BaseViewModel() {

    var count = IntLiveData()

    init {
        count.value = 0
    }
}