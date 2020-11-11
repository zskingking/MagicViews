package com.example.navigate

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.navitate_tab.view.*

/**
 * des 导航tab
 * @author zs
 * @date 2020/11/10
 */
class NavigateTab : FrameLayout {

    /**
     * 选中图标
     */
    private var selectIcon: Int? = null

    /**
     * 默认图标
     */
    private var normalIcon: Int? = null

    /**
     * tab文案
     */
    private var tabText: String? = null

    /**
     * tab文字选中颜色
     */
    private var selectTextColor = ContextCompat.getColor(context, R.color.selectTabColor)

    /**
     * tab文字默认颜色
     */
    private var normalTextColor = ContextCompat.getColor(context, R.color.normalTabColor)

    /**
     * tab文字大小
     */
    private var tabTextSize: Int? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.navitate_tab, this)
    }

    /**
     * 显示隐藏消息
     */
    fun setMessage(isShow: Boolean) {
        vMsg.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    /**
     * 显示隐藏消息数量
     */
    fun setMsgNumber(msgNumber: Int) {
        tvMsgNumber.visibility = if (msgNumber > 0) {
            View.VISIBLE
        } else {
            View.GONE
        }
        tvMsgNumber.text = "$msgNumber"
    }

    /**
     * 设置tab文字颜色
     */
    fun setTabTextColor(selectColor: Int, normalColor: Int) {
        this.selectTextColor = selectColor
        this.normalTextColor = normalColor
    }

    /**
     * 设置tab文字大小
     */
    fun setTabTextSize(tabTextSize: Int) {
        this.tabTextSize = tabTextSize
        tvTabText.textSize = tabTextSize.toFloat()
    }

    /**
     * 初始化数据
     * @param selectIcon 选中icon
     * @param normalIcon 默认icon
     * @param tabText tab文案
     */
    fun initData(selectIcon: Int, normalIcon: Int, tabText: String) {
        this.selectIcon = selectIcon
        this.normalIcon = normalIcon
        this.tabText = tabText
        tvTabText.text = tabText
    }

    /**
     * 选中状态
     * @param isSelect 是否选中
     */
    fun select(isSelect: Boolean) {
        if (isSelect) {
            //设置图标
            selectIcon?.let { ivIcon.setImageResource(it) }
            //设置tab文字颜色
            tvTabText.setTextColor(selectTextColor)
        } else {
            normalIcon?.let { ivIcon.setImageResource(it) }
            tvTabText.setTextColor(normalTextColor)
        }
    }
}