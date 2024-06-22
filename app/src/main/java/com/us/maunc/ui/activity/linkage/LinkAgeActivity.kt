package com.us.maunc.ui.activity.linkage

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gyf.immersionbar.ImmersionBar
import com.maunc.jetpackmvvm.base.BaseVmActivity
import com.us.maunc.R
import com.us.maunc.databinding.ActivityLinkAgeBinding
import com.us.maunc.ui.dialog.common.CommonDialog

class LinkAgeActivity : BaseVmActivity<LinkAgeVM, ActivityLinkAgeBinding>() {

    private val linkAgeTabAdapter by lazy {
        LinkAgeTabAdapter(mutableListOf()).apply {
            setOnItemClickListener { _, _, pos ->
                tabChangeEvent(pos)
            }
        }
    }
    private val linkAgeContentAdapter by lazy {
        LinkAgeContentAdapter(mutableListOf())
    }

    private val linkAgeChangeDataAdapter by lazy {
        LinkAgeChangeDataAdapter(mutableListOf()).apply {
            setOnItemClickListener { _, _, pos ->
                setSelectedPosition(data[pos])
                mDatabind.drawerLayoutRoot.tag = data[pos]
                mDatabind.drawerLayout.closeDrawers()
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).statusBarColor(R.color.white)
            .statusBarDarkFont(true).init()
        mDatabind.tabRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mDatabind.tabRecyclerView.adapter = linkAgeTabAdapter
        linkAgeTabAdapter.setList(mViewModel.tabData)
        mDatabind.contentRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mDatabind.contentRecyclerView.adapter = linkAgeContentAdapter
        linkAgeContentAdapter.setList(mViewModel.contentDataHeiQiYiHu)
        mDatabind.changeRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mDatabind.changeRecyclerView.adapter = linkAgeChangeDataAdapter
        linkAgeChangeDataAdapter.setList(mViewModel.changeData)

        mDatabind.contentRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val firstVisibleItemPosition =
                    linearLayoutManager?.findFirstVisibleItemPosition() as Int
                linkAgeTabAdapter.setSelectedPosition(firstVisibleItemPosition)
            }
        })
        mDatabind.linkAgeChangeMoreTv.setOnClickListener {
            mDatabind.drawerLayout.openDrawer(mDatabind.drawerLayoutRoot)
        }
        mDatabind.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                Log.w("ww", "onDrawerSlide")
            }

            override fun onDrawerOpened(drawerView: View) {
                Log.w("ww", "onDrawerOpened")
            }

            override fun onDrawerClosed(drawerView: View) {
                val commonDialog = CommonDialog()
                commonDialog.show(supportFragmentManager, "tg")
                Handler(Looper.myLooper()!!).postDelayed({
                    commonDialog.dismissAllowingStateLoss()
                    when (drawerView.tag as String) {
                        "黑崎一护" -> {
                            linkAgeContentAdapter.setList(mViewModel.contentDataHeiQiYiHu)
                        }

                        "蒙奇·D·路飞" -> {
                            linkAgeContentAdapter.setList(mViewModel.contentDataLvFei)
                        }

                        "漩涡鸣人" -> {
                            linkAgeContentAdapter.setList(mViewModel.contentDataXanWoMingRen)
                        }
                    }
                    tabChangeEvent(0)
                }, 1000)
            }

            override fun onDrawerStateChanged(newState: Int) {
                Log.w("ww", "onDrawerStateChanged")
            }
        })
    }

    override fun createObserver() {
        mViewModel.changeFlag.observe(this) {

        }
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }

    override fun onScreenStateChanged(screenState: Boolean) {
    }

    private fun tabChangeEvent(pos: Int) {
        linkAgeTabAdapter.setSelectedPosition(pos)
        mDatabind.contentRecyclerView.scrollToPosition(pos)
    }
}