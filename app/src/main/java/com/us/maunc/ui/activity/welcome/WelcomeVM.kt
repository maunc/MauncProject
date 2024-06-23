package com.us.maunc.ui.activity.welcome

import com.maunc.jetpackmvvm.base.BaseViewModel
import com.maunc.jetpackmvvm.livedata.BooleanLiveData
import com.maunc.mvvmhabit.utils.AppUtils
import com.us.maunc.R

/**
 *ClsFunction：
 *CreateDate：2024/5/15
 *Author：TimeWillRememberUs
 */
class WelcomeVM : BaseViewModel() {

    var isBlack = BooleanLiveData()
    var recData = mutableListOf<WelcomeData>()

    init {
        isBlack.value = false
        recData.add(WelcomeData(AppUtils.getString(R.string.welcome_start_main_tv),"main"))
        recData.add(WelcomeData(AppUtils.getString(R.string.welcome_start_keyboard_tv),"keyboard"))
        recData.add(WelcomeData(AppUtils.getString(R.string.welcome_start_pic_tv),"pic"))
        recData.add(WelcomeData(AppUtils.getString(R.string.welcome_start_speed_ratio_tv),"speed_ratio"))
        recData.add(WelcomeData(AppUtils.getString(R.string.welcome_start_push_box_tv),"push_box"))
        recData.add(WelcomeData(AppUtils.getString(R.string.welcome_start_link_age_tv),"link_age"))
        recData.add(WelcomeData(AppUtils.getString(R.string.welcome_start_second_list),"second_list"))
        recData.add(WelcomeData(AppUtils.getString(R.string.welcome_start_local_music),"local_music"))
    }
}