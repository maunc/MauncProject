package com.us.maunc.ui.activity.welcome

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.us.maunc.R
import com.us.maunc.databinding.ItemWelcomeBinding

/**
 *ClsFunction：
 *CreateDate：2024/5/15
 *Author：TimeWillRememberUs
 */
class WelcomeRecAdapter(data: MutableList<WelcomeData>) :
    BaseQuickAdapter<WelcomeData, BaseDataBindingHolder<ItemWelcomeBinding>>(
        R.layout.item_welcome, data
    ) {

    var isBlack: Boolean = false

    override fun convert(holder: BaseDataBindingHolder<ItemWelcomeBinding>, item: WelcomeData) {
        holder.dataBinding?.itemWelcomeTv?.text = item.title
        if (isBlack) {
            holder.dataBinding?.itemWelcomeTv?.setTextColor(context.resources.getColor(R.color.white))
            holder.dataBinding?.itemWelcomeLine?.setBackgroundColor(context.resources.getColor(R.color.white))
        } else {
            holder.dataBinding?.itemWelcomeTv?.setTextColor(context.resources.getColor(R.color.black))
            holder.dataBinding?.itemWelcomeLine?.setBackgroundColor(context.resources.getColor(R.color.black))
        }
    }
}