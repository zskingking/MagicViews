package com.zs.magicviews.scala

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zs.magicviews.R
import kotlinx.android.synthetic.main.activity_scala.*

class ScalaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scala)
        scalaView.setImageBitmap(BitmapFactory.decodeResource(resources, R.mipmap.girl_friend))
    }
}