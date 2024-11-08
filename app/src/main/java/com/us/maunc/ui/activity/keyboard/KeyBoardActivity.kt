package com.us.maunc.ui.activity.keyboard

import android.os.Bundle
import com.maunc.jetpackmvvm.base.BaseVmActivity
import com.us.maunc.databinding.ActivityKeyBoardBinding

class KeyBoardActivity: BaseVmActivity<KeyBoardVM, ActivityKeyBoardBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.virtualKeyboardView.changeShowEditText(this, mDatabind.textAmount)
        mDatabind.textAmount.setOnClickListener { _ ->
            mDatabind.virtualKeyboardView.showEditText()
        }
    }

    override fun createObserver() {
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }

    override fun onScreenStateChanged(screenState: Boolean) {
    }

    override fun onBackPressed() {
        if (mDatabind.virtualKeyboardView.isShow()) {
            mDatabind.virtualKeyboardView.closeEditText()
        } else {
            super.onBackPressed()
        }
    }
}