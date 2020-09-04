package com.zs.magicviews

import android.content.Intent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zs.magicviews.card.CardActivity
import com.zs.magicviews.motion.MotionLayoutActivity
import com.zs.magicviews.ruler.RulerActivity
import com.zs.magicviews.scala.ScalaActivity
import com.zs.magicviews.tag.TagActivity

/**
 * des 自定义View列表
 * @author zs
 * @data 2020/7/25
 */
class MainAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_layout) {

    init {
        setOnItemClickListener { _, _, position ->
            when (position) {
                0 -> context.startActivity(Intent(context, RulerActivity::class.java))
                1 -> context.startActivity(Intent(context, TagActivity::class.java))
                2 -> context.startActivity(Intent(context, MotionLayoutActivity::class.java))
                3 -> context.startActivity(Intent(context, ScalaActivity::class.java))
            }
        }
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tvTitle, item)
    }
}