package com.zs.ruler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.zs.base.common.dip2px
import kotlinx.android.synthetic.main.ruler_layout.view.*

/**
 * des 刻度尺,包含一个子View ScaleView。
 * 没有将指示器跟ScaleView画到一块的原因：ScaleView滑动的是画布，
 * 指示器会跟着刻度走，就算一直矫正也会左右漂浮不定
 *
 * @date 2020/7/23
 * @author zs
 */
class RulerView : RelativeLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) :
            super(context, attributeSet, defStyleAttr) {
        //获取xml中的属性
        val array = context.obtainStyledAttributes(attributeSet, R.styleable.RulerView)
        scaleColor = array.getColor(R.styleable.RulerView_scaleColor, Color.WHITE)

        scaleWidth = array.getDimension(R.styleable.RulerView_scaleWidth,
            dip2px(context, 1f).toFloat()).toInt()

        scaleHeight = array.getDimension(R.styleable.RulerView_scaleHeight,
            dip2px(context, 15f).toFloat()).toInt()

        scaleInterval = array.getDimension(R.styleable.RulerView_scaleInterval,
            dip2px(context, 10f).toFloat()).toInt()

        isRadius = array.getBoolean(R.styleable.RulerView_isRadius, false)
        maxScale = array.getInteger(R.styleable.RulerView_maxScale, 200)
        scaleTextSize = array.getInteger(R.styleable.RulerView_scaleTextSize, dip2px(context, 11f))
        scaleTextColor = array.getColor(R.styleable.RulerView_scaleTextColor, Color.WHITE)
        indicatorColor = array.getColor(R.styleable.RulerView_indicatorColor, Color.WHITE)
        //用完回收掉
        array.recycle()
        //去除锯齿
        paint.isAntiAlias = true
        //指示器颜色
        paint.color = indicatorColor
        //选用xml布局
        View.inflate(context, R.layout.ruler_layout, this)
        setScaleView()
    }

    /**
     * 设置scaleView属性
     */
    private fun setScaleView() {
        scaleView.scaleColor = scaleColor
        scaleView.scaleWidth = scaleWidth
        scaleView.scaleHeight = scaleHeight
        scaleView.scaleInterval = scaleInterval
        scaleView.isRadius = isRadius
        scaleView.maxScale = maxScale
        scaleView.scaleTextSize = scaleTextSize
        scaleView.scaleTextColor = scaleTextColor
        scaleView.initPaint()
    }

    /**
     * 刻度颜色，默认为白色
     */
    private var scaleColor = ContextCompat.getColor(context, R.color.white)

    /**
     * 刻度宽度，默认为1dp
     */
    private var scaleWidth = dip2px(context, 1f)

    /**
     * 刻度高度，默认为15dp
     */
    private var scaleHeight = dip2px(context, 15f)

    /**
     * 刻度间隔。默认10dp
     */
    private var scaleInterval = dip2px(context, 10f)

    /**
     * 刻度上方是否是圆角
     */
    private var isRadius = false


    /**
     * 刻度数量，默认为200
     */
    private var maxScale = 200

    /**
     * 刻度文字大小，默认为12dp
     */
    private var scaleTextSize = dip2px(context, 12f)

    /**
     * 刻度文字颜色，默认为白色
     */
    private var scaleTextColor = ContextCompat.getColor(context, R.color.white)

    /**
     * 刻度文字颜色，默认为白色
     */
    private var indicatorColor = ContextCompat.getColor(context, R.color.white)

    /**
     * 指示器X轴坐标
     */
    private var indicatorPointX = 0

    /**
     * 指示器左右偏移量
     */
    private var indicatorOffset = dip2px(context, 10f)

    /**
     * 画笔，只用来画指示器
     */
    private val paint by lazy { Paint() }

    /**
     * 指示器path
     */
    private val path by lazy { Path() }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //中心坐标
        indicatorPointX = (width / scaleInterval) / 2 * scaleInterval
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        path.moveTo((indicatorPointX - indicatorOffset).toFloat(), 0f)
        path.lineTo((indicatorPointX + indicatorOffset).toFloat(), 0f)
        path.lineTo(indicatorPointX.toFloat(), (indicatorOffset * 1.2).toFloat())
        path.close()
        canvas.drawPath(path, paint)
    }

    /**
     * 添加滚动方法
     */
    fun addScrollListener(scrollListener: (Int) -> Unit) {
        scaleView.addScrollListener(scrollListener)
    }

}