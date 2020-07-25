package com.zs.magicviews.tag

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zs.magicviews.R
import kotlinx.android.synthetic.main.activity_tag.*

/**
 * @author zs
 * @data 2020/7/25
 */
class TagActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)
        val list = mutableListOf<String>()
        list.add("当秋天")
        list.add("洒下")
        list.add("最后一把")
        list.add("枫叶时")
        list.add("正是")
        list.add("我")
        list.add("要离开")
        list.add("的时候")
        list.add("看着")
        list.add("收拾好的")
        list.add("行李")
        list.add("想起")
        list.add("远方的你")
        list.add("心里")
        list.add("竟然")
        list.add("有")
        list.add("一股")
        list.add("幸福的")
        list.add("感觉")
        list.add("就在")
        list.add("这个时候")
        list.add("地上的")
        list.add("枫叶")
        list.add("刹那间...")
        list.add("🍁🍁🍁")
        tagLayout.setTag(list)
    }
}