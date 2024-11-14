package com.us.maunc.ui.dialog.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.maunc.jetpackmvvm.base.BaseDialog
import com.us.maunc.databinding.DialogCommonBinding

/**
 *ClsFunction：
 *CreateDate：2024/6/22
 *Author：TimeWillRememberUs
 */
class CommonDialog : BaseDialog<CommonDialogVM, DialogCommonBinding>() {
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