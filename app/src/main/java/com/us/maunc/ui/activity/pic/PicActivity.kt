package com.us.maunc.ui.activity.pic

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gyf.immersionbar.ImmersionBar
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.maunc.jetpackmvvm.base.BaseVmActivity
import com.maunc.mvvmhabit.utils.AppUtils
import com.us.maunc.R
import com.us.maunc.databinding.ActivityPicBinding

class PicActivity : BaseVmActivity<PicVM, ActivityPicBinding>(),
    PicSelectAdapter.PicSelectEventListener {

    private val list = mutableListOf<LocalMedia>()
    private val picSelectAdapter by lazy {
        PicSelectAdapter(this, list).apply {
            setPicSelectEventListener(this@PicActivity)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarColor(R.color.white)
            .statusBarDarkFont(true).init()
        mDatabind.picRec.layoutManager = object : GridLayoutManager(this, 3) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        mDatabind.picRec.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.left = AppUtils.getDimens(R.dimen.x5)
                outRect.top = AppUtils.getDimens(R.dimen.x5)
                outRect.bottom = AppUtils.getDimens(R.dimen.x5)
                outRect.right = AppUtils.getDimens(R.dimen.x5)
            }
        })
        mDatabind.picRec.adapter = picSelectAdapter
        (mDatabind.picRec.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        val picDragCallBack = PicDragCallBack(this, picSelectAdapter)
        val itemTouchHelper = ItemTouchHelper(picDragCallBack)
        itemTouchHelper.attachToRecyclerView(mDatabind.picRec)
    }

    override fun createObserver() {

    }

    override fun onNetworkStateChanged(netState: Boolean) {

    }

    override fun onScreenStateChanged(screenState: Boolean) {

    }

    override fun addPicEvent() {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setMaxSelectNum(PicConstant.maxSize - list.size)
            .setMinSelectNum(PicConstant.minSize)
            .setImageSpanCount(PicConstant.imageSpanCount)
            .setImageEngine(glideEngine)
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            val obtainSelectorList = PictureSelector.obtainSelectorList(data)
            picSelectAdapter.notifyPicList(obtainSelectorList)
        }
    }
}