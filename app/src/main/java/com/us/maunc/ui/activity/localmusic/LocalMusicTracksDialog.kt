package com.us.maunc.ui.activity.localmusic

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maunc.mvvmhabit.utils.DeviceUtils
import com.us.maunc.R

/**
 *ClsFunction：
 *CreateDate：2024/6/23
 *Author：TimeWillRememberUs
 */
class LocalMusicTracksDialog(private val data: MutableList<LocalMusicFileData>) :
    BottomSheetDialogFragment() {

    private val localMusicTracksAdapter by lazy {
        LocalMusicTracksAdapter(mutableListOf())
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_local_music_list, null)
        dialog.setContentView(view)
        val viewGroup = view.parent as ViewGroup
        val behavior = BottomSheetBehavior.from(viewGroup)
        //设置弹出高度
        behavior.peekHeight = DeviceUtils.getDeviceHeight(false) / 5 * 3
        view.layoutParams.height = DeviceUtils.getDeviceHeight(false) / 5 * 3
        behavior.isHideable = true

        val recyclerView = view.findViewById<RecyclerView>(R.id.dialog_local_music_track_recycler)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = localMusicTracksAdapter
        return dialog
    }

    fun setData(data: MutableList<LocalMusicFileData>, tagPath: String) {
        var pos = 0
        for ((index, localMusicFileData) in data.withIndex()) {
            if (tagPath == localMusicFileData.path) {
                pos = index
                Log.e("LocalMusicTracksDialog", "yyes ${data[index].path}+++$tagPath")
                break
            } else {
                Log.e("LocalMusicTracksDialog", "not found: ${data[index].path}")
            }
        }
        localMusicTracksAdapter.setLists(data, pos)
    }

    inner class LocalMusicTracksAdapter(data: MutableList<LocalMusicFileData>) :
        BaseQuickAdapter<LocalMusicFileData, BaseViewHolder>(
            R.layout.item_local_music_tracks,
            data
        ) {

        private var tagPos = 0

        override fun convert(holder: BaseViewHolder, item: LocalMusicFileData) {
            val textView = holder.getView<TextView>(R.id.item_local_music_tv)
            val musicLineView = holder.getView<LocalMusicLineView>(R.id.item_local_music_line)
            val relativeLayout = holder.getView<RelativeLayout>(R.id.item_local_music_root_view)
            textView.isSelected = true
            textView.text = item.name
            textView.setTextColor(
                if (tagPos == holder.layoutPosition) {
                    Color.GREEN
                } else {
                    Color.BLACK
                }
            )
            musicLineView.visibility = if (tagPos == holder.layoutPosition) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        fun setLists(data: MutableList<LocalMusicFileData>, tagPos: Int) {
            this.tagPos = tagPos
            setList(data)
        }
    }

}