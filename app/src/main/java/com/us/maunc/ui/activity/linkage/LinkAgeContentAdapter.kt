package com.us.maunc.ui.activity.linkage

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.maunc.jetpackmvvm.ext.screenWidth
import com.maunc.jetpackmvvm.ext.setWidth
import com.us.maunc.App
import com.us.maunc.R

/**
 *ClsFunction：
 *CreateDate：2024/6/20
 *Author：TimeWillRememberUs
 */
class LinkAgeContentAdapter(data: MutableList<LinkAgeContentBean>) :
    BaseMultiItemQuickAdapter<LinkAgeContentBean, BaseViewHolder>(data) {

    init {
        addItemType(LinkAgeMultiItemType.TYPE_ONE, R.layout.item_link_age_content_one)
        addItemType(LinkAgeMultiItemType.TYPE_TWO, R.layout.item_link_age_content_two)
    }

    override fun convert(holder: BaseViewHolder, item: LinkAgeContentBean) {
        when (getItemViewType(holder.layoutPosition)) {
            LinkAgeMultiItemType.TYPE_ONE -> {
                val oneImageRec = holder.getView<RecyclerView>(R.id.item_link_age_one_imageRec)
                val textView = holder.getView<TextView>(R.id.item_link_age_one_tv)
                val titleTv = holder.getView<TextView>(R.id.item_link_age_one_title)
                oneImageRec.layoutManager =
                    LinearLayoutManager(App.getInstance(), LinearLayoutManager.HORIZONTAL, false)
                oneImageRec.adapter = LinkAgeContentOneImageAdapter(item.imageRes)
                textView.text = item.content
                titleTv.text = item.title
            }

            LinkAgeMultiItemType.TYPE_TWO -> {
                val textView = holder.getView<TextView>(R.id.item_link_age_two_tv)
                val titleTv = holder.getView<TextView>(R.id.item_link_age_two_title)
                textView.text = item.content
                titleTv.text = item.title
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].itemType
    }

}

class LinkAgeContentOneImageAdapter(data: MutableList<Int>) :
    BaseQuickAdapter<Int, BaseViewHolder>(R.layout.item_link_age_content_one_item, data) {
    override fun convert(holder: BaseViewHolder, item: Int) {
        val imageView = holder.getView<ImageView>(R.id.item_link_age_one_item_image)
        imageView.setWidth(context.screenWidth() / 3)
        imageView.setImageResource(item)
    }
}