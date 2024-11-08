package com.us.maunc.ui.activity.linkage

import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.maunc.jetpackmvvm.utils.AppUtils
import com.us.maunc.R

/**
 *ClsFunction：
 *CreateDate：2024/6/20
 *Author：TimeWillRememberUs
 */
class LinkAgeChangeDataAdapter(data: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_link_age_change, data) {
    private var mCurrentPosition = 0

    override fun convert(holder: BaseViewHolder, item: String) {
        val tabTv = holder.getView<TextView>(R.id.item_link_age_change_tv)
        tabTv.text = item
        if (mCurrentPosition == holder.layoutPosition) {
            tabTv.textSize = AppUtils.getDimens(R.dimen.x23).toFloat()
            tabTv.setBackgroundColor(Color.BLACK)
            tabTv.setTextColor(Color.WHITE)
        } else {
            tabTv.textSize = AppUtils.getDimens(R.dimen.x18).toFloat()
            tabTv.setBackgroundColor(Color.WHITE)
            tabTv.setTextColor(Color.BLACK)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedPosition(changeContent: String) {
        data.forEachIndexed { index, s ->
            if (s == changeContent) {
                mCurrentPosition = index
                notifyDataSetChanged()
                return
            }
        }
    }
}