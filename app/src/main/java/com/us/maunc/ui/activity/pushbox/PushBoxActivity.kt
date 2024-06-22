package com.us.maunc.ui.activity.pushbox

import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.maunc.jetpackmvvm.base.BaseVmActivity
import com.us.maunc.R
import com.us.maunc.databinding.ActivityPushBoxBinding

class PushBoxActivity : BaseVmActivity<PushBoxVM, ActivityPushBoxBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarColor(R.color.white)
            .statusBarDarkFont(true).init()
        mDatabind.topBtu.setOnClickScaleViewListener {
            mDatabind.pushBoxGameView.moveUp()
        }
        mDatabind.bottomBtu.setOnClickScaleViewListener {
            mDatabind.pushBoxGameView.moveDown()
        }
        mDatabind.leftBtu.setOnClickScaleViewListener {
            mDatabind.pushBoxGameView.moveLeft()
        }
        mDatabind.rightBtu.setOnClickScaleViewListener {
            mDatabind.pushBoxGameView.moveRight()
        }
    }

    override fun createObserver() {
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }

    override fun onScreenStateChanged(screenState: Boolean) {
    }
}