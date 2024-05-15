package com.us.mytest.ui.activity.pic

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


/**
 *ClsFunction：
 *CreateDate：2024/5/13
 *Author：TimeWillRememberUs
 */
class PicDragCallBack(val activity: Context, private val adapter: PicSelectAdapter) :
    ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(
            if (viewHolder.adapterPosition >= adapter.getPicList().size &&
                viewHolder.adapterPosition <= PicConstant.maxSize - 1
            ) 0 else dragFlags, 0
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //得到当拖拽的viewHolder的Position
        val fromPosition = viewHolder.adapterPosition
        //拿到当前拖拽到的item的viewHolder
        val toPosition = target.adapterPosition
        adapter.onItemMove(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    //按下
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
    }

    //抬起
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
//        adapter.notifyItemRangeChanged(
//            Math.min(adapter.pos, adapter.targetPos),
//            Math.abs(adapter.pos - adapter.targetPos) + 1
//        )
    }
}