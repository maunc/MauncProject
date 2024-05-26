package com.us.mytest.ui.activity.pushbox

import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.maunc.jetpackmvvm.base.BaseVmActivity
import com.us.mytest.R
import com.us.mytest.databinding.ActivityPushBoxBinding

class PushBoxActivity : BaseVmActivity<PushBoxVM, ActivityPushBoxBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarColor(R.color.white)
            .statusBarDarkFont(true).init()
        mDatabind.pushBoxTips.isSelected = true
        mDatabind.topBtu.setClickListener(object : PushBoxScaleView.PushBoxScaleOnClickListener {
            override fun onClick() {
                mDatabind.pushBoxGameView.moveUp()
            }
        })
        mDatabind.bottomBtu.setClickListener(object : PushBoxScaleView.PushBoxScaleOnClickListener {
            override fun onClick() {
                mDatabind.pushBoxGameView.moveDown()
            }
        })
        mDatabind.leftBtu.setClickListener(object : PushBoxScaleView.PushBoxScaleOnClickListener {
            override fun onClick() {
                mDatabind.pushBoxGameView.moveLeft()
            }
        })
        mDatabind.rightBtu.setClickListener(object : PushBoxScaleView.PushBoxScaleOnClickListener {
            override fun onClick() {
                mDatabind.pushBoxGameView.moveRight()
            }
        })
    }

    override fun createObserver() {
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }

    override fun onScreenStateChanged(screenState: Boolean) {
    }
}