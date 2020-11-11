package com.zs.magicviews.nav

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zs.magicviews.R
import kotlinx.android.synthetic.main.activity_navigate.*


class NavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigate)
        val selectIcons = mutableListOf<Int>().apply {
            add(R.mipmap.personal_tab_pack_selected)
            add(R.mipmap.personal_tab_book_selected)
            add(R.mipmap.personal_tab_learn_selected)
            add(R.mipmap.personal_tab_me_selected)
            add(R.mipmap.personal_tab_me_selected)
        }

        val normalIcons = mutableListOf<Int>().apply {
            add(R.mipmap.personal_tab_pack_unselected)
            add(R.mipmap.personal_tab_book_unselected)
            add(R.mipmap.personal_tab_learn_unselected)
            add(R.mipmap.personal_tab_me_unselected)
            add(R.mipmap.personal_tab_me_unselected)
        }

        val tabTexts = mutableListOf<String>().apply {
            add("听课")
            add("读书")
            add("学习")
            add("我的")
            add("我的")
        }
        nav.setTabItems(selectIcons,normalIcons,tabTexts)
        nav.setMsg(1,true)
        nav.setMsgNumber(2,3)
        nav.setTabSelectListener {
            Log.i("nav", tabTexts[it])
        }
    }
}