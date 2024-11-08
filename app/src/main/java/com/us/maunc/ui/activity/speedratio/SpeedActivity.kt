package com.us.maunc.ui.activity.speedratio

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maunc.jetpackmvvm.base.BaseVmActivity
import com.us.maunc.databinding.ActivitySpeedBinding

/**
 * 滑动阻尼Activity
 */
class SpeedActivity : BaseVmActivity<SpeedVM, ActivitySpeedBinding>() {

    private lateinit var speedBgRecAdapter: SpeedBgRecAdapter
    private lateinit var speedRecAdapter: SpeedRecAdapter

    override fun initView(savedInstanceState: Bundle?) {
        //背景rec
        val speedRatioLayoutManager = SpeedRatioLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false, 0.45
        )
        mDatabind.speedBgRec.setLayoutManager(speedRatioLayoutManager)
        speedBgRecAdapter = SpeedBgRecAdapter()
        mDatabind.speedBgRec.setAdapter(speedBgRecAdapter)
        //前置rec
        mDatabind.speedDataRec.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        speedRecAdapter = SpeedRecAdapter()
        mDatabind.speedDataRec.setAdapter(speedRecAdapter)
        mDatabind.speedDataRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mDatabind.speedBgRec.scrollBy(dx + mDatabind.speedBgRec.getScrollX(), dy)
            }
        })

    }

    override fun createObserver() {
        mViewModel.speedBgRecData.observe(this, Observer {
            speedBgRecAdapter.setList(it)
        })
        mViewModel.speedRecData.observe(this, Observer {
            speedRecAdapter.setList(it)
        })
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }

    override fun onScreenStateChanged(screenState: Boolean) {
    }
}