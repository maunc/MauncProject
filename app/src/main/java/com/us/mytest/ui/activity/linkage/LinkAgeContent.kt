package com.us.mytest.ui.activity.linkage

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 *ClsFunction：
 *CreateDate：2024/6/20
 *Author：TimeWillRememberUs
 */
data class LinkAgeContentBean(
    override var itemType: Int,
    val title: String,
    val imageRes: MutableList<Int>,
    val content: String
) : MultiItemEntity

object LinkAgeMultiItemType {
    const val TYPE_ONE = 0
    const val TYPE_TWO = 1
}