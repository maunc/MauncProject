package com.us.maunc.ui.activity.xiecheng

import android.os.Bundle
import com.maunc.jetpackmvvm.base.BaseActivity
import com.us.maunc.databinding.ActivityKotlinXcactivityBinding

class KotlinXCActivity :BaseActivity<KotlinXCVM,ActivityKotlinXcactivityBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.mmm.setOnClickListener {
            mViewModel.getData()
        }
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }

    override fun onScreenStateChanged(screenState: Boolean) {
    }

    override fun onFrontAndBackStateChanged(frontAndBackState: Boolean) {
    }

}