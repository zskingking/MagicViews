package com.zs.magicviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = mutableListOf<String>()
        list.add("ruler")
        list.add("tag")
        list.add("MotionLayout")
        list.add("ScalaView")
        MainAdapter().apply {
            setNewData(list)
            recyclerView.adapter = this
        }
    }
}