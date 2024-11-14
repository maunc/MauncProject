package com.us.maunc.ui.activity.main

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.maunc.jetpackmvvm.base.BaseModel
import com.maunc.jetpackmvvm.base.BaseViewModel

class MainVM : BaseViewModel<BaseModel>() {

    var mainRecData = MutableLiveData<List<String>>()
    var handler: Handler = Handler(Looper.getMainLooper())

    init {
        handler.post {
            val recData: MutableList<String> =
                ArrayList()
            for (i in 0..24) {
                recData.add("my_test$i")
            }
            mainRecData.value = recData
        }
    }

    fun testString() = "my name isMainVm"
}