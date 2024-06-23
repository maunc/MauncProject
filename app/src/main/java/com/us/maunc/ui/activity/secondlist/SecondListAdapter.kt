package com.us.maunc.ui.activity.secondlist

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.maunc.jetpackmvvm.ext.gone
import com.maunc.jetpackmvvm.ext.visible
import com.us.maunc.R
import com.us.maunc.databinding.ItemSecondListOneBinding
import com.us.maunc.databinding.ItemSecondListTwoBinding
import java.util.Collections

class FirstListAdapter(
    private var mContext: Context,
    mData: MutableList<String>,
    private var secondListData: MutableList<Discount>?
) : BaseQuickAdapter<String, BaseDataBindingHolder<ItemSecondListOneBinding>>(
    R.layout.item_second_list_one, mData
) {

    private var helper: ItemTouchHelper? = null

    override fun convert(holder: BaseDataBindingHolder<ItemSecondListOneBinding>, item: String) {
        holder.dataBinding?.itemTvTitle?.text = item
        val tvMore = holder.dataBinding?.itemTvMore
        val secondRv = holder.dataBinding?.itemRvSecond

        secondRv?.layoutManager = LinearLayoutManager(mContext)
        secondRv?.adapter = SecondListAdapter(mContext, secondListData)
        //设置二级列表拖拽
        setSecondListDrag(secondListData, secondRv)

        tvMore?.setOnClickListener {
            if (secondRv?.visibility == View.VISIBLE) {
                secondRv.gone()
                tvMore.setRightDrawableWithRes(mContext, R.drawable.icon_jr)
            } else {
                secondRv?.visible()
                tvMore.setRightDrawableWithRes(mContext, R.drawable.img_arrow_down)
            }
        }
    }

    private fun setSecondListDrag(secondListData: MutableList<Discount>?, secondRv: RecyclerView?) {
        //设置拖拽
        helper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                var dragFlag = 0
                if (recyclerView.layoutManager is GridLayoutManager) {
                    dragFlag =
                        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                } else if (recyclerView.layoutManager is LinearLayoutManager) {
                    dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                }
                val swipeFlags = 0
                return makeMovementFlags(dragFlag, swipeFlags);
            }

            override fun isLongPressDragEnabled(): Boolean {
                return true  //长按启用拖拽
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return false //不启用拖拽删除
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                //通过接口传递拖拽交换数据的起始位置和目标位置的ViewHolder
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                secondListData?.let {
                    if (fromPosition < secondListData.size && toPosition < secondListData.size) {
                        //交换数据位置
                        Collections.swap(secondListData, fromPosition, toPosition);
                        //刷新位置交换
                        val adapter = recyclerView.adapter as SecondListAdapter
                        adapter.notifyItemMoved(fromPosition, toPosition)
                    }
                    return true
                } ?: return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //移动删除回调,如果不用可以不用理
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    //当滑动或者拖拽view的时候通过接口返回该ViewHolder
                    //当拖拽选中时放大选中的view
                    viewHolder?.itemView?.scaleX = 1.2f;
                    viewHolder?.itemView?.scaleY = 1.2f;
//                    viewHolder?.itemView?.setBackgroundColor(Color.Green);
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                if (!recyclerView.isComputingLayout) {
                    //当需要清除之前在onSelectedChanged或者
                    // onChildDraw,onChildDrawOver设置的状态或者动画时通过接口返回该ViewHolder
                    viewHolder.itemView.scaleX = 1.0f;
                    viewHolder.itemView.scaleY = 1.0f;
                }
            }
        })
        helper?.attachToRecyclerView(secondRv)
    }
}

class SecondListAdapter(var mContext: Context, mData: MutableList<Discount>?) :
    BaseQuickAdapter<Discount, BaseDataBindingHolder<ItemSecondListTwoBinding>>(
        R.layout.item_second_list_two, mData
    ) {
    override fun convert(holder: BaseDataBindingHolder<ItemSecondListTwoBinding>, item: Discount) {
        holder.dataBinding?.itemTvTitle2?.text = item.discountName
    }
}