package com.us.maunc.ui.activity.secondlist

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.maunc.jetpackmvvm.base.BaseActivity
import com.us.maunc.databinding.ActivitySeconeListBinding
import java.util.Collections

class SecondListActivity : BaseActivity<SecondListVM, ActivitySeconeListBinding>() {

    private var helper: ItemTouchHelper? = null
    private var mAdapter: FirstListAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        //沉浸式
        enableEdgeToEdge()
        val listData = Gson().fromJson(mViewModel.mJson, ListData::class.java)
        val firstList = listData.data.payTypes
        val secondList = listData.data.discounts

        if (mAdapter == null) {
            mAdapter = FirstListAdapter(this, firstList, secondList)
        }
        mDatabind.rvFirst.layoutManager = LinearLayoutManager(this)
        mDatabind.rvFirst.adapter = mAdapter

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
                val swipeFlags = 0;
                return makeMovementFlags(dragFlag, swipeFlags);
            }

            override fun isLongPressDragEnabled(): Boolean {
                return true;  //长按启用拖拽
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return false; //不启用拖拽删除
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                //通过接口传递拖拽交换数据的起始位置和目标位置的ViewHolder
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                if (fromPosition < firstList.size && toPosition < firstList.size) {
                    //交换数据位置
                    Collections.swap(firstList, fromPosition, toPosition);
                    //刷新位置交换
                    val adapter = recyclerView.adapter as FirstListAdapter
                    adapter.notifyItemMoved(fromPosition, toPosition)
                }
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //移动删除回调,如果不用可以不用理
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    //当滑动或者拖拽view的时候通过接口返回该ViewHolder
                    //当拖拽选中时放大选中的view
                    viewHolder?.itemView?.scaleX = 1.1f;
                    viewHolder?.itemView?.scaleY = 1.1f;
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
        helper?.attachToRecyclerView(mDatabind.rvFirst)
    }

    override fun createObserver() {
    }

    override fun onNetworkStateChanged(netState: Boolean) {

    }

    override fun onScreenStateChanged(screenState: Boolean) {

    }
}