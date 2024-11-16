package com.us.maunc.ui.activity.xiecheng

import android.util.Log
import com.maunc.jetpackmvvm.base.BaseModel
import com.maunc.jetpackmvvm.base.BaseViewModel
import com.maunc.jetpackmvvm.ext.requestNoCheck

class KotlinXCVM : BaseViewModel<BaseModel>() {


    fun getData() {
        requestNoCheck({
            apiService.getTestNetData()
        }, {
            Log.e("ee", it.content)
        }, {
            Log.e("ww", "${it.message}")
        })
    }

}