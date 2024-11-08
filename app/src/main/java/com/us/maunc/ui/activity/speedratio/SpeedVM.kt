package com.us.maunc.ui.activity.speedratio

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.maunc.jetpackmvvm.base.BaseViewModel
import com.us.maunc.R


class SpeedVM : BaseViewModel() {

    var speedBgRecData = MutableLiveData<List<Int>>()
    var speedRecData = MutableLiveData<List<Int>>()
    var handler: Handler = Handler(Looper.getMainLooper())

    init {
        handler.post {
            val bgData: MutableList<Int> = ArrayList()
            bgData.add(R.drawable.gewei)
            bgData.add(R.drawable.yangguanghao)
            bgData.add(R.drawable.longmao)
            speedBgRecData.value = bgData

            val mainData: MutableList<Int> = ArrayList()
            mainData.add(R.drawable.s1)
            mainData.add(R.drawable.s2)
            mainData.add(R.drawable.s3)
            mainData.add(R.drawable.s4)
            mainData.add(R.drawable.s5)
            mainData.add(R.drawable.s6)
            mainData.add(R.drawable.s7)
            mainData.add(R.drawable.s8)
            mainData.add(R.drawable.s9)
            mainData.add(R.drawable.s10)
            mainData.add(R.drawable.s11)
            mainData.add(R.drawable.s12)
            mainData.add(R.drawable.s13)
            mainData.add(R.drawable.s14)
            mainData.add(R.drawable.s15)
            mainData.add(R.drawable.s16)
            speedRecData.value = mainData
        }
    }
}