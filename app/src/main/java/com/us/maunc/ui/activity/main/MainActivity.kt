package com.us.maunc.ui.activity.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.maunc.jetpackmvvm.base.BaseVmActivity
import com.maunc.jetpackmvvm.utils.AppUtils
import com.maunc.jetpackmvvm.utils.DeviceUtils
import com.maunc.jetpackmvvm.utils.LogUtils

import com.us.maunc.R
import com.us.maunc.databinding.ActivityMainBinding
import com.us.utilslib.ConvertUtils
import kotlin.math.max


class MainActivity : BaseVmActivity<MainVM, ActivityMainBinding>() {

    private lateinit var mainRecAdapter: MainRecAdapter

    override fun initView(savedInstanceState: Bundle?) {
        LogUtils.e("X:" + AppUtils.getDimens(R.dimen.x50))
        LogUtils.e("Y:" + AppUtils.getDimens(R.dimen.y50))
        mDatabind.myRec.setLayoutManager(LinearLayoutManager(this))
        mainRecAdapter = MainRecAdapter()
        mDatabind.myRec.setAdapter(mainRecAdapter)
        mDatabind.mybgShow.setOnClickListener { view ->
            MainShowBitmapActivity.bitmap =
                ConvertUtils.drawable2Bitmap(mDatabind.mybgShow.getDrawable())
            startActivity(
                Intent(
                    this@MainActivity,
                    MainShowBitmapActivity::class.java
                )
            )
            overridePendingTransition(R.anim.bitmap_enter, R.anim.main_exit)
        }
        val sheetBehavior =
            BottomSheetBehavior.from<NestedScrollView>(mDatabind.testNestedView)

        //是否可以完全隐藏
        sheetBehavior.isHideable = false

        //设置为协调状态
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        //缩到最短露头的高度
        sheetBehavior.peekHeight = 360
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                //如果不设置模式带上状态栏的高度
                //减去的这个值越小 bottomSheet的高度就越高，要控制高度合法
                val targetBottomHeight = DeviceUtils.getDeviceHeight(false) - 50
                val layoutParams = bottomSheet.layoutParams
                if (layoutParams.height != targetBottomHeight) {
                    layoutParams.height = targetBottomHeight
                    bottomSheet.layoutParams = layoutParams
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                LogUtils.e("slideOffset:", slideOffset)
                mDatabind.backMainRl.setAlpha(max(-(slideOffset - 1).toDouble(), 0.66).toFloat())
                mDatabind.backMainRl.setTranslationY(-(450 * slideOffset))
            }
        })

    }

    override fun createObserver() {
        mViewModel.mainRecData.observe(this, Observer {
            mainRecAdapter.setList(it)
        })
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }

    override fun onScreenStateChanged(screenState: Boolean) {
    }
}