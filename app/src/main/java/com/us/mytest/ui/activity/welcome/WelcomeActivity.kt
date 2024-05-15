package com.us.mytest.ui.activity.welcome

import android.os.Bundle
import com.maunc.jetpackmvvm.base.BaseVmActivity
import com.us.mytest.databinding.ActivityWelcomeBinding

class WelcomeActivity : BaseVmActivity<WelcomeVM, ActivityWelcomeBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun createObserver() {
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }

}