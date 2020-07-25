package com.zs.magicviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ruler.addScrollListener {
            tvScale.text = String.format("体重 %s 吨",it)
        }

        val list = mutableListOf<String>()
        list.add("思思1")
        list.add("思思2")
        list.add("思思思3")
        list.add("思思思思4")
        list.add("思思思5")
        list.add("思思思6")
        list.add("思7")
        list.add("思思思8")
        list.add("思思9")
        list.add("思思思10")
        list.add("思思思11")
        list.add("思思12")
        list.add("思思思13")
        list.add("思思14")
        list.add("思15")
        list.add("思思16")
        list.add("思思思17")
        list.add("思思18")
        list.add("思19")
        list.add("思思20")
//        tagLayout.post {
//            Log.i("MainActivity","Height............${tagLayout.height}")
//            Log.i("MainActivity","width............${tagLayout.width}")
//        }
        tagLayout.setTag(list)
    }
}