package com.zs.card

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * des 卡片层叠式LayoutManager
 * @author zs
 * @data 2020/7/25
 */
class CardLayoutManager: RecyclerView.LayoutManager() {

    companion object{
        /**
         * y轴平移距离
         */
        const val transRadius = 8f

        /**
         * 最大显示个数。其实只能看到三个，第四个在滑出后平滑缩放时出现
         */
        const val maxCount = 4

        /**
         * 缩放系数
         */
        const val scaleRatio = 0.03f

    }

    /**
     * 给子View设置LayoutParams。本例中为自适应WRAP_CONTENT
     */
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    /**
     * 对子View进行摆放
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State?) {
        //将View从ViewHolder中剥离出来，等待重新布局
        detachAndScrapAttachedViews(recycler)
        //item数量
        //如果RecyclerView中只有一个item或者没有，什么都不做
        if (itemCount < 1) {
            return
        }
        //最底部item的角标，如果item数量小于一屏幕摆放量，就取item个数-1
        val bottomPosition = if (itemCount <= maxCount) {
            0
        }
        //如果item数量大于一屏幕摆放量，就取maxCount个,即itemCount - maxCount
        else {
            itemCount - maxCount
        }
        //倒序从下往上摆放。保证第一个最后一个添加并显示在最上层
        for (index in bottomPosition until itemCount) {
            //从缓冲池中获取到itemView
            val view: View = recycler.getViewForPosition(index)
            //将itemView添加到RecyclerView中
            addView(view)
            //通过RecyclerView测量itemView
            measureChildWithMargins(view, 0, 0)
            //recyclerView宽度-itemView宽度
            val widthSpace = width - getDecoratedMeasuredWidth(view)
            val heightSpace = height - getDecoratedMeasuredHeight(view)
            //将itemView水平居中
            layoutDecoratedWithMargins(
                view, widthSpace / 2, heightSpace / 2
                , widthSpace / 2 + getDecoratedMeasuredWidth(view)
                , heightSpace / 2 + getDecoratedMeasuredHeight(view)
            )
//            //如果是最底层的两个itemView
//            if (index > maxCount - 3) {
//                //缩放
//                view.scaleX = 1 - scaleRatio * (maxCount - 2)
//                //往下平移
//                view.translationY = transRadius * (maxCount - 2)
//            } else if (index > 0) {
//                view.scaleX = 1 - scaleRatio * index
//                view.translationY = transRadius * index
//            }
            //改变View的大小跟位置
            val level: Int = abs(index - itemCount + 1)
            //如果是最底层的两个itemView
            //如果是最底层的两个itemView
            if (level > maxCount - 3) {
                //进行缩放
                view.scaleX = 1 - scaleRatio * (maxCount - 2)
                //进行平移
                view.translationY = transRadius * (maxCount - 2)
            } else if (level > 0) {
                view.scaleX = 1 - scaleRatio * level
                view.translationY = transRadius * level
            }
        }
    }
}