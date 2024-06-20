package com.us.mytest.ui.activity.linkage

import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.maunc.mvvmhabit.utils.AppUtils
import com.us.mytest.R

/**
 *ClsFunction：
 *CreateDate：2024/6/20
 *Author：TimeWillRememberUs
 */
class LinkAgeTabAdapter(data: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_link_age_tab, data) {

    private var mCurrentPosition = 0

    override fun convert(holder: BaseViewHolder, item: String) {
        val tabTv = holder.getView<TextView>(R.id.item_link_age_tab_tv)
        tabTv.text = item
        if (mCurrentPosition == holder.layoutPosition) {
            tabTv.textSize = AppUtils.getDimens(R.dimen.x23).toFloat()
            tabTv.background = AppUtils.getDrawable(R.drawable.orange_corners_25)
            tabTv.setTextColor(Color.WHITE)
        } else {
            tabTv.textSize = AppUtils.getDimens(R.dimen.x18).toFloat()
            tabTv.background = AppUtils.getDrawable(R.drawable.white_corners_25)
            tabTv.setTextColor(Color.BLACK)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedPosition(position: Int) {
        mCurrentPosition = position
        notifyDataSetChanged()
    }
}