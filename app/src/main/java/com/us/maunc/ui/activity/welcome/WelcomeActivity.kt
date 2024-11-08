package com.us.maunc.ui.activity.welcome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.maunc.jetpackmvvm.base.BaseVmActivity
import com.maunc.jetpackmvvm.utils.AppUtils
import com.maunc.jetpackmvvm.utils.DeviceUtils
import com.us.maunc.R
import com.us.maunc.databinding.ActivityWelcomeBinding
import com.us.maunc.ui.activity.keyboard.KeyBoardActivity
import com.us.maunc.ui.activity.linkage.LinkAgeActivity
import com.us.maunc.ui.activity.localmusic.LocalMusicActivity
import com.us.maunc.ui.activity.main.MainActivity
import com.us.maunc.ui.activity.pic.PicActivity
import com.us.maunc.ui.activity.pushbox.PushBoxActivity
import com.us.maunc.ui.activity.secondlist.SecondListActivity
import com.us.maunc.ui.activity.speedratio.SpeedActivity

class WelcomeActivity : BaseVmActivity<WelcomeVM, ActivityWelcomeBinding>() {

    private val welcomeRecAdapter by lazy {
        WelcomeRecAdapter(mutableListOf()).apply {
            setOnItemClickListener { _, _, pos ->
                when (getItem(pos).tag) {
                    "main" -> {
                        activityGoto(MainActivity::class.java)
                    }

                    "keyboard" -> {
                        activityGoto(KeyBoardActivity::class.java)
                    }

                    "pic" -> {
                        activityGoto(PicActivity::class.java)
                    }

                    "speed_ratio" -> {
                        activityGoto(SpeedActivity::class.java)
                    }

                    "push_box" -> {
                        activityGoto(PushBoxActivity::class.java)
                    }

                    "link_age" -> {
                        activityGoto(LinkAgeActivity::class.java)
                    }

                    "second_list" -> {
                        activityGoto(SecondListActivity::class.java)
                    }

                    "local_music" -> {
                        activityGoto(LocalMusicActivity::class.java)
                    }
                }
            }
        }
    }

    private lateinit var changeAnimEnter: Animation
    private lateinit var changeAnimExit: Animation

    override fun initView(savedInstanceState: Bundle?) {
        changeAnimEnter = AnimationUtils.loadAnimation(this, R.anim.change_welcome_theme_enter)
        changeAnimExit = AnimationUtils.loadAnimation(this, R.anim.change_welcome_theme_exit)

        val layoutParams = RelativeLayout.LayoutParams(
            AppUtils.getDimens(R.dimen.x150),
            AppUtils.getDimens(R.dimen.x150)
        )
        layoutParams.topMargin = (DeviceUtils.getDeviceHeight(true) / 1.35).toInt()
        layoutParams.rightMargin = AppUtils.getDimens(R.dimen.x20)
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
        mDatabind.welcomeChangeThemesBlack.layoutParams = layoutParams
        mDatabind.welcomeChangeThemesWhite.layoutParams = layoutParams
        mDatabind.welcomeRec.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mDatabind.welcomeRec.adapter = welcomeRecAdapter
        welcomeRecAdapter.setList(mViewModel.recData)
        mDatabind.welcomeChangeThemesBlack.setOnClickListener {
            mViewModel.isBlack.value = !mViewModel.isBlack.value
        }
        mDatabind.welcomeChangeThemesWhite.setOnClickListener {
            mViewModel.isBlack.value = !mViewModel.isBlack.value
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun createObserver() {
        mViewModel.isBlack.observe(this) {
            if (it) {
                mDatabind.welcomeChangeThemesBlack.startAnimation(changeAnimEnter)
                mDatabind.welcomeChangeThemesWhite.startAnimation(changeAnimExit)
                mDatabind.welcomeChangeThemesBlack.visibility = View.VISIBLE
                mDatabind.welcomeChangeThemesWhite.visibility = View.GONE
                mDatabind.welcomeRootView.setBackgroundColor(resources.getColor(R.color.black))
                ImmersionBar.with(this).statusBarColor(R.color.black)
                    .statusBarDarkFont(false).init()
            } else {
                mDatabind.welcomeChangeThemesBlack.startAnimation(changeAnimExit)
                mDatabind.welcomeChangeThemesWhite.startAnimation(changeAnimEnter)
                mDatabind.welcomeChangeThemesBlack.visibility = View.GONE
                mDatabind.welcomeChangeThemesWhite.visibility = View.VISIBLE
                mDatabind.welcomeRootView.setBackgroundColor(resources.getColor(R.color.white))
                ImmersionBar.with(this).statusBarColor(R.color.white)
                    .statusBarDarkFont(true).init()
            }
            welcomeRecAdapter.isBlack = it
            welcomeRecAdapter.notifyDataSetChanged()
        }
    }

    override fun onNetworkStateChanged(netState: Boolean) {
    }

    override fun onScreenStateChanged(screenState: Boolean) {
    }

    private fun activityGoto(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }
}