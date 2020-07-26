package com.zs.card

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs


/**
 * @author zs
 * @data 2020/7/25
 */
class CardHelperCallback : ItemTouchHelper.Callback() {

    private var mListener: OnItemTouchCallbackListener? = null

    fun setListener(listener: OnItemTouchCallbackListener?) {
        mListener = listener
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = 0
        var swipeFlags = 0
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is CardLayoutManager) {
            //允许上下滑动
            swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN
        }
        return makeMovementFlags(
            dragFlags,
            swipeFlags
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }
    
    /**
     * itemView滑出了屏幕
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (mListener != null) {
            mListener!!.onSwiped(viewHolder.adapterPosition, direction)
        }
    }

    /**
     * 是否是长按的时候进行拖拽操作,
     * 返回true长按可以进行拖拽
     * 返回false可以进行手动触发拖拽
     * @return
     */
    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    /**
     * 是否可以被滑动
     * @return
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        //以偏移量大的方向为标准
        val trans: Float = if (abs(dX) > abs(dY)) {
            abs(dX)
        } else {
            abs(dY)
        }
        //滑动比例
        var ratio = trans / getThreshold(recyclerView, viewHolder)
        if (ratio > 1) {
            ratio = 1f
        }
        //获取itemView总量
        val itemCount = recyclerView.childCount

        //移除时为底部显示的View增加动画
        for (index in 1 until CardLayoutManager.maxCount - 1) {
            val view: View = recyclerView.getChildAt(index)
            val t: Float =
                1 / (1 - CardLayoutManager.scaleRatio * ratio) -
                        CardLayoutManager.scaleRatio * (itemCount - index - 1)
            view.scaleX = t
            view.translationY = -CardLayoutManager.transRadius * ratio +
                    CardLayoutManager.transRadius * (itemCount - index - 1)
        }
        //为被拖动的View增加透明度动画
        val view: View = recyclerView.getChildAt(itemCount - 1)
        view.alpha = 1 - abs(ratio) * 0.2f
    }
    

    //获取划出屏幕的距离阈值
    private fun getThreshold(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Float {
        return recyclerView.width * getSwipeThreshold(viewHolder)
    }

    interface OnItemTouchCallbackListener {
        //item被滑动的回调方法
        fun onSwiped(position: Int, direction: Int)
    }
}