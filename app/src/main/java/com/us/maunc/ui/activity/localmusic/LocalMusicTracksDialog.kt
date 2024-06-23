package com.us.maunc.ui.activity.localmusic

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maunc.mvvmhabit.utils.DeviceUtils
import com.us.maunc.R

/**
 *ClsFunction：
 *CreateDate：2024/6/23
 *Author：TimeWillRememberUs
 */
class LocalMusicTracksDialog : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_common, null)
        dialog.setContentView(view)
        val viewGroup = view.parent as ViewGroup
        val behavior = BottomSheetBehavior.from(viewGroup)
        //设置弹出高度
        behavior.peekHeight = DeviceUtils.getDeviceHeight(false) / 5 * 3
        view.layoutParams.height = DeviceUtils.getDeviceHeight(false) / 5 * 3
        behavior.isHideable = true
        return dialog
    }

}