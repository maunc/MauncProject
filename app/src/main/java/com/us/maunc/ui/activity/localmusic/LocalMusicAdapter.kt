package com.us.maunc.ui.activity.localmusic

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.us.maunc.R

/**
 *ClsFunction：
 *CreateDate：2024/6/23
 *Author：TimeWillRememberUs
 */
class LocalMusicAdapter(data: MutableList<LocalMusicFileData>) :
    BaseMultiItemQuickAdapter<LocalMusicFileData, BaseViewHolder>(data) {

    init {
        addItemType(LocalMusicAdapterConstant.FOLDER, R.layout.item_local_file_list)
        addItemType(LocalMusicAdapterConstant.AUDIO, R.layout.item_local_music_list)
    }

    override fun convert(holder: BaseViewHolder, item: LocalMusicFileData) {
        when (data[holder.layoutPosition].itemType) {
            LocalMusicAdapterConstant.FOLDER -> {
                val titleTv = holder.getView<TextView>(R.id.file_name_item)
                titleTv.text = item.name
                titleTv.isSelected = true
            }

            LocalMusicAdapterConstant.AUDIO -> {
                val titleTv = holder.getView<TextView>(R.id.file_name_item)
                titleTv.text = item.name
                titleTv.isSelected = true
                holder.setText(R.id.duration_music_item, item.duration.convertDuration())
                Glide.with(context)
                    .load(LocalMusicBimapCache.getBitmap(item.coverKey) ?: R.drawable.icon_yyzt)
                    .apply(
                        LocalMusicComm.setAudioCorners(
                            if (LocalMusicBimapCache.getBitmap(item.coverKey) == null) {
                                0f
                            } else {
                                26f
                            }
                        )
                    )
                    .into(holder.getView(R.id.file_icon_item))
            }
        }

        holder.getView<ConstraintLayout>(R.id.item_local_root_view).setOnClickListener {
            if (localMusicItemClickListener != null) {
                localMusicItemClickListener?.onItemClick(item)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return data[position].itemType
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: MutableList<LocalMusicFileData>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    private var localMusicItemClickListener: OnLocalMusicItemClickListener? = null

    interface OnLocalMusicItemClickListener {
        fun onItemClick(musicFileData: LocalMusicFileData)
    }

    fun setOnLocalMusicItemClickListener(onItemClick: (musicFileData: LocalMusicFileData) -> Unit = {}) {
        localMusicItemClickListener = object : OnLocalMusicItemClickListener {
            override fun onItemClick(musicFileData: LocalMusicFileData) = onItemClick(musicFileData)
        }
    }
}