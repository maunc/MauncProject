package com.us.mytest.ui.activity.kt

import android.os.Bundle
import com.maunc.jetpackmvvm.base.BaseVmActivity
import com.us.mytest.databinding.ActivityKtBinding

class KtActivity : BaseVmActivity<KtVM, ActivityKtBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.testTv.setOnClickListener {
            mViewModel.count.value++
            mDatabind.testTv.text = mViewModel.count.value.toString()
        }
    }

    override fun createObserver() {
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }

}