package com.us.mytest.ui.dialog.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.maunc.jetpackmvvm.base.BaseVmDialog
import com.us.mytest.databinding.DialogCommonBinding

/**
 *ClsFunction：
 *CreateDate：2024/6/22
 *Author：TimeWillRememberUs
 */
class CommonDialog : BaseVmDialog<CommonDialogVM, DialogCommonBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        //透明
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun lazyLoadData() {
    }

    override fun createObserver() {
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }
}