package com.zs.magicviews.card

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zs.magicviews.R

/**
 * @author zs
 * @data 2020/7/25
 */
class CardAdapter:BaseQuickAdapter<CardBean,BaseViewHolder>(R.layout.item_card) {

    override fun convert(holder: BaseViewHolder, item: CardBean) {
        holder.setImageResource(R.id.img,item.pic)
        holder.setText(R.id.tv_name,"item.name${holder.adapterPosition}")
        holder.setText(R.id.tv_year,item.ballYear)
        holder.setText(R.id.tv_team,item.team)
    }
}