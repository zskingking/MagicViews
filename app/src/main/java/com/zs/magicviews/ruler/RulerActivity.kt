package com.zs.magicviews.ruler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zs.magicviews.R
import kotlinx.android.synthetic.main.activity_ruler.*

/**
 * @author zs
 * @data 2020/7/25
 */
class RulerActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruler)
        ruler.addScrollListener {
            tvScale.text = String.format("体重 %s 吨",it)
        }
    }
}