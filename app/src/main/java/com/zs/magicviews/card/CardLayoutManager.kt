package com.zs.magicviews.card

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @author zs
 * @data 2020/7/25
 */
class CardLayoutManager: RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}