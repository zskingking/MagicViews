package com.zs.magicviews.card

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.zs.card.CardHelperCallback
import com.zs.card.CardLayoutManager
import com.zs.magicviews.R
import kotlinx.android.synthetic.main.activity_card.*


class CardActivity : AppCompatActivity() , CardHelperCallback.OnItemTouchCallbackListener{
    private val mCardAdapter by lazy { CardAdapter() }
    private var mCardBeanList = mutableListOf<CardBean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)
        init()
    }

    private fun init() {
        initTouch()
        loadMore()
        recyclerView.layoutManager = CardLayoutManager()
        mCardAdapter.setNewData(mCardBeanList)
        recyclerView.adapter = mCardAdapter
    }

    //加载更多
    private fun loadMore() {
        val cardBeenList = mutableListOf<CardBean>()
        val bean0 = CardBean()
        bean0.name = "拉塞尔-韦斯特布鲁克"
        bean0.ballYear = "球龄：11年"
        bean0.team = "球队：俄克拉荷马雷霆"
        bean0.pic = R.drawable.ic_launcher_background
        val bean1 = CardBean()
        bean1.name = "科比-布莱恩特"
        bean1.ballYear = "球龄：20年"
        bean1.team = "球队：洛杉矶湖人"
        bean1.pic = R.drawable.ic_launcher_background
        val bean2 = CardBean()
        bean2.name = "勒布朗-詹姆斯"
        bean2.ballYear = "球龄：15年"
        bean2.team = "球队：洛杉矶湖人"
        bean2.pic = R.drawable.ic_launcher_background
        val bean3 = CardBean()
        bean3.name = "保罗-乔治"
        bean3.ballYear = "球龄：9年"
        bean3.team = "球队：俄克拉荷马雷霆"
        bean3.pic = R.drawable.ic_launcher_background
        val bean4 = CardBean()
        bean4.name = "迈克尔-乔丹"
        bean4.ballYear = "球龄：16年"
        bean4.team = "球队：芝加哥公牛"
        bean4.pic = R.drawable.ic_launcher_background
        val bean5 = CardBean()
        bean5.name = "德维恩-韦德"
        bean5.ballYear = "球龄：15年"
        bean5.team = "球队：迈阿密热火"
        bean5.pic = R.drawable.ic_launcher_background
        val bean6 = CardBean()
        bean6.name = "德克-诺维斯基"
        bean6.ballYear = "球龄：21年"
        bean6.team = "球队：达拉斯小牛"
        bean6.pic = R.drawable.ic_launcher_background
        val bean7 = CardBean()
        bean7.name = "达米安-利拉德"
        bean7.ballYear = "球龄：7年"
        bean7.team = "球队：波特兰开拓者"
        bean7.pic = R.drawable.ic_launcher_background
        val bean8 = CardBean()
        bean8.name = "史蒂芬-库里"
        bean8.ballYear = "球龄：10年"
        bean8.team = "球队：金州勇士"
        bean8.pic = R.drawable.ic_launcher_background
        val bean9 = CardBean()
        bean9.name = "凯里-欧文"
        bean9.ballYear = "球龄：7年"
        bean9.team = "球队：波士顿凯尔特人"
        bean9.pic = R.drawable.ic_launcher_background
        cardBeenList.add(bean0)
        cardBeenList.add(bean1)
//        cardBeenList.add(bean2)
//        cardBeenList.add(bean3)
//        cardBeenList.add(bean4)
//        cardBeenList.add(bean5)
//        cardBeenList.add(bean6)
//        cardBeenList.add(bean7)
//        cardBeenList.add(bean8)
//        cardBeenList.add(bean9)
        cardBeenList.reverse()
        Log.i("mCardBeanList","cardBeenList${cardBeenList[0].name}")
        mCardBeanList.addAll(0, cardBeenList)
    }

    private fun initTouch() {
        CardHelperCallback().apply {
            setListener(this@CardActivity)
            val helper = ItemTouchHelper(this)
            //将ItemTouchHelper和RecyclerView进行绑定
            helper.attachToRecyclerView(recyclerView)
        }
    }

    override fun onSwiped(position: Int, direction: Int) {
        if (direction == CardLayoutManager.maxCount) { //左滑
            //Toast.makeText(this,"left",Toast.LENGTH_SHORT).show();
        } else { //右滑
            //Toast.makeText(this,"right",Toast.LENGTH_SHORT).show();
        }
        for (i in 0 until recyclerView.childCount) {
            val view: View = recyclerView.getChildAt(i)
            view.alpha = 1F
        }
        mCardBeanList.removeAt(position)
        //加载更多
        if (mCardBeanList.size < CardLayoutManager.maxCount) {
            loadMore()
        }
        mCardAdapter.notifyDataSetChanged()
    }
}