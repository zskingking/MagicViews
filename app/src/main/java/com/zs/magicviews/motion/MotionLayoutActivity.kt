package com.zs.magicviews.motion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.zs.magicviews.R
import kotlinx.android.synthetic.main.activity_motion_layout.*

class MotionLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_layout)
        seekBar.max = 1000
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {
                tvTime.text = "${(p1*0.1).toInt()}"
                motionLayout2.progress = p1.toFloat() * 0.001f
            }

            override fun onStartTrackingTouch(p0: SeekBar) {}

            override fun onStopTrackingTouch(p0: SeekBar) {}
        })
    }
}