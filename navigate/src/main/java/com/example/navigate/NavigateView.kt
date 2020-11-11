package com.example.navigate

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.zs.base.common.dip2px


/**
 * des
 * @author zs
 * @date 2020/11/10
 */
class NavigateView : LinearLayout {

    /**
     * tab点击事件
     */
    private var _tabSelectListener: ((Int) -> Unit)? = null

    /**
     * 默认显示0角标
     */
    private var defaultTabPosition = 0

    /**
     * 选中icon列表
     */
    private var selectIconList: List<Int>? = null

    /**
     * 默认icon列表
     */
    private var normalIconList: List<Int>? = null

    /**
     * tab文案列表
     */
    private var tabTextList: List<String>? = null

    /**
     * tab文字选中颜色
     */
    private var selectTextColor = ContextCompat.getColor(context, R.color.selectTabColor)

    /**
     * tab文字默认颜色
     */
    private var normalTextColor = ContextCompat.getColor(context, R.color.normalTabColor)

    /**
     * tab文字默认大小
     */
    private var tabTextSize = 10

    /**
     * 分割线高度
     */
    private var lineHeight = dip2px(context, 1f)

    /**
     * 分割线颜色
     */
    private var lineColor = ContextCompat.getColor(context, R.color.lineColor)

    /**
     * 当前角标
     */
    private var currentPosition = defaultTabPosition

    private val paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) :  this(context, attrs, 0){
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        getXMLValue(context,attrs)
        initView()
    }

    /**
     * 获取xml中属性
     */
    private fun getXMLValue(context: Context, attributeSet: AttributeSet){
        val array = context.obtainStyledAttributes(attributeSet, R.styleable.NavigateView)
        //选中颜色
        selectTextColor = array.getColor(R.styleable.NavigateView_selectTextColor
            , ContextCompat.getColor(context, R.color.selectTabColor))
        //默认颜色
        normalTextColor = array.getColor(R.styleable.NavigateView_normalTextColor
            , ContextCompat.getColor(context, R.color.normalTabColor))
        //tab文字大小
        tabTextSize = array.getDimension(R.styleable.NavigateView_tabTextSize, 10f).toInt()
        //分割线高度
        lineHeight = array.getDimension(R.styleable.NavigateView_lineHeight, dip2px(context, 1f).toFloat()).toInt()
        //分割线颜色
        lineColor = array.getColor(R.styleable.NavigateView_lineColor
            , ContextCompat.getColor(context, R.color.lineColor))

    }

    private fun initView() {
        //水平摆放
        orientation = HORIZONTAL
        paint.color = lineColor
        paint.strokeWidth = lineHeight.toFloat()
        //开启绘制
        setWillNotDraw(false)
        //预留出分割线
        setPadding(0, lineHeight, 0, 0)
    }

    fun setTabSelectListener(tabClickListener: (Int) -> Unit) {
        this._tabSelectListener = tabClickListener
    }

    /**
     * 设置tab列表
     * @param selectIconList 选中图标列表
     * @param normalIconList 默认图标列表
     * @param tabTextList 文案列表
     */
    fun setTabItems(
        selectIconList: List<Int>,
        normalIconList: List<Int>,
        tabTextList: List<String>
    ) {
        if (selectIconList.size != normalIconList.size
            || selectIconList.size != tabTextList.size
            || normalIconList.size != tabTextList.size
        ) {
            throw IllegalArgumentException("列表长度不一致..")
        }
        this.selectIconList = selectIconList
        this.normalIconList = normalIconList
        this.tabTextList = tabTextList

        //先将所有子View移除
        removeAllViews()
        //逐个添加tab
        for (index in selectIconList.indices) {
            addTab(index)
        }
        //选中默认tab
        if (defaultTabPosition < selectIconList.size) {
            val tab = getChildAt(defaultTabPosition) as NavigateTab
            tab.select(true)
        }
        //注册点击事件
        setChildClickListener()
    }

    /**
     * 添加tab
     */
    private fun addTab(index: Int) {
        if (selectIconList == null
            || normalIconList == null
            || tabTextList == null
        ) return
        addView(
            NavigateTab(context).apply {
                initData(
                    selectIconList?.get(index)!!,
                    normalIconList?.get(index)!!,
                    tabTextList?.get(index)!!
                )
                select(false)
                setTabTextColor(selectTextColor, normalTextColor)
                setTabTextSize(tabTextSize)
            }, getTabLayoutParams()
        )
    }

    /**
     * 获取通用LayoutParams
     */
    private fun getTabLayoutParams(): LayoutParams {
        //宽为0，通过权重比布局
        return LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT).apply {
            weight = 1f
        }
    }

    private fun setChildClickListener() {
        for (index in 0 until childCount) {
            get(index).setOnClickListener {
                //重复点击当前 tab 直接return
                if (currentPosition == index) return@setOnClickListener
                currentPosition = index
                selectTab(index)
                _tabSelectListener?.invoke(index)
            }
        }
    }

    /**
     * 选择tab
     * @param i 选中的角标
     */
    fun selectTab(i: Int) {
        for (index in 0 until childCount) {
            val tab = get(index) as NavigateTab
            if (i == index) {
                tab.select(true)
            } else {
                tab.select(false)
            }
        }
    }

    /**
     * 设置红点状态
     * @param index 位置
     * @param isShow 是否显示
     */
    fun setMsg(index: Int, isShow: Boolean) {
        if (index >= childCount) return
        val tab = getChildAt(index) as NavigateTab
        tab.setMessage(isShow)
    }

    /**
     * 设置消息数量
     * @param index 位置
     * @param msgNumber 消息数量
     */
    fun setMsgNumber(index: Int, msgNumber: Int) {
        if (index >= childCount) return
        val tab = getChildAt(index) as NavigateTab
        tab.setMsgNumber(msgNumber)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制分割线
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, paint)
    }
}