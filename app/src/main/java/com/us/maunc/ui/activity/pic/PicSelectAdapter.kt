package com.us.maunc.ui.activity.pic

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luck.picture.lib.entity.LocalMedia
import com.maunc.jetpackmvvm.utils.LogUtils
import com.us.maunc.R
import java.util.Collections


/**
 *ClsFunction：
 *CreateDate：2024/5/13
 *Author：TimeWillRememberUs
 */


@SuppressLint("InflateParams", "NotifyDataSetChanged")
class PicSelectAdapter(private val context: Context, private val data: MutableList<LocalMedia>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var pos: Int = -1
    var targetPos: Int = -1

    fun notifyPicList(picList: MutableList<LocalMedia>) {
        data.addAll(picList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PicItemViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_pic_select, null, false)
        )
    }

    override fun getItemCount(): Int {
        return if (data.size == 0) PicConstant.minSize
        else (data.size + 1).coerceAtMost(PicConstant.maxSize)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val picItemViewHolder = holder as PicItemViewHolder
        val delView = picItemViewHolder.delView
        val picImage = picItemViewHolder.picImage
        val addView = picItemViewHolder.addView
        if (position >= data.size && position <= PicConstant.maxSize - 1) {
            picImage.visibility = View.GONE
            addView.visibility = View.VISIBLE
            delView.visibility = View.GONE
        } else {
            picImage.visibility = View.VISIBLE
            addView.visibility = View.GONE
            delView.visibility = View.VISIBLE
            Glide.with(context).load(data[position].path)
                .into(picItemViewHolder.picImage)
        }
        picItemViewHolder.addView.setOnClickListener {
            if (picSelectEventListener != null)
                picSelectEventListener!!.addPicEvent()
        }
        picItemViewHolder.delView.setOnClickListener {
            LogUtils.e("点击pos::$position")
            data.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, data.size - position)
        }
    }

    fun getPicList(): List<LocalMedia> {
        return data
    }

    fun onItemMove(pos: Int, targetPos: Int) {
        if (targetPos > data.size - 1) {
            return
        }
        Collections.swap(data, pos, targetPos)
        notifyItemMoved(pos, targetPos)
        this.pos = pos
        this.targetPos  = targetPos
        //放开没有bug  有动画卡顿
        notifyItemRangeChanged(Math.min(pos, targetPos), Math.abs(pos - targetPos) + 1)
    }
    interface PicSelectEventListener {
        fun addPicEvent()
    }

    private var picSelectEventListener: PicSelectEventListener? = null

    fun setPicSelectEventListener(picSelectEventListener: PicSelectEventListener?) {
        this.picSelectEventListener = picSelectEventListener
    }

    class PicItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val picImage: ImageView
        val delView: ImageView
        val addView: RelativeLayout

        init {
            picImage = itemView.findViewById(R.id.picImageView)
            delView = itemView.findViewById(R.id.picDelBtu)
            addView = itemView.findViewById(R.id.addImageView)
        }
    }

}