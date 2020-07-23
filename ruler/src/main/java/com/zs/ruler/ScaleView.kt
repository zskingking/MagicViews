package com.zs.ruler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.*
import android.widget.OverScroller
import androidx.core.content.ContextCompat
import com.zs.base.common.dip2px
import com.zs.base.common.getScreenWidthPx
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * des 刻度view
 * @date 2020/7/22
 * @author zs
 */
class ScaleView : View {

    /**
     * 刻度回调方法
     */
    var scrollListener: ((Int) -> Unit)? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) :
            super(context, attributeSet, defStyleAttr) {
        //去除锯齿
        paint.isAntiAlias = true
    }

    /**
     * 画笔
     */
    private val paint by lazy { Paint() }

    /**
     * 刻度颜色，默认为白色
     */
    var scaleColor = ContextCompat.getColor(context, R.color.white)

    /**
     * 刻度宽度，默认为1dp
     */
    var scaleWidth = dip2px(context, 1f)

    /**
     * 刻度高度，默认为15dp
     */
    var scaleHeight = dip2px(context, 15f)

    /**
     * 刻度上方是否是圆角
     */
    var isRadius = false

    /**
     * 刻度间隔。默认10dp
     */
    var scaleInterval = dip2px(context, 10f)

    /**
     * 刻度数量，默认为200
     */
    var maxScale = 200

    /**
     * 刻度文字大小，默认为12dp
     */
    var scaleTextSize = dip2px(context, 12f)

    /**
     * 刻度文字颜色，默认为白色
     */
    var scaleTextColor = ContextCompat.getColor(context, R.color.white)

    /**
     * 指示器X轴坐标
     */
    private var indicatorPointX = 0


    /**
     * 速度采集器
     */
    private var velocityTracker: VelocityTracker? = null

    /**
     * 最大滑动速度
     */
    private val maxVelocity by lazy {
        ViewConfiguration.get(context)
            .scaledMaximumFlingVelocity
    }

    /**
     * 最小滑动速度，低于时不滑动
     */
    private val minVelocity by lazy {
        ViewConfiguration.get(context)
            .scaledMinimumFlingVelocity
    }

    /**
     * 滑动帮助类
     */
    private var scroller = OverScroller(context)

    /**
     * 最小滚动距离
     */
    private var minScrollX = 0

    /**
     * 最大滚动距离
     */
    private var maxScrollX = 0

    /**
     * 当前偏移量。不能直接用scrollX，因为scrollX是int类型，会导致精度丢失
     */
    private var currentScrollX = 0f

    /**
     * 默认宽度为屏幕宽度
     */
    private val defaultWidth = getScreenWidthPx(context)

    /**
     * 默认高度为120dp
     */
    private val defaultHeight = dip2px(context, 120f)

    /**
     * 给画笔赋初始值
     */
    fun initPaint() {
        paint.color = scaleColor
        paint.textSize = scaleTextSize.toFloat()
        if (isRadius) {
            paint.strokeCap = Paint.Cap.ROUND
        }
    }

    /**
     * 对宽高没有特殊要求，直接使用View提供的resolveSize进行测量
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val rulerWidth = resolveSize(defaultWidth, widthMeasureSpec)
        val rulerHeight = resolveSize(defaultHeight, heightMeasureSpec)
        setMeasuredDimension(rulerWidth, rulerHeight)
    }

    /**
     * 尺寸改变时
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //中心坐标,最多可见刻度 / 2 * 刻度间隔，得到指示器位置,即scrollX的最小值
        indicatorPointX = (width / scaleInterval) / 2 * scaleInterval
        //最小滚动距离,-指示器x轴偏移量
        minScrollX = -indicatorPointX
        //最大滚动距离,刻度数 * 间隔 - width + width - indicatorPointX。换算后结果如下
        maxScrollX = maxScale * scaleInterval - indicatorPointX
        //给一个初始值
        changeScale()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawScale(canvas)
    }

    /**
     * 绘制刻度
     */
    private fun drawScale(canvas: Canvas) {
        for (index in 0..maxScale) {
            //每十个刻度有一个粗长刻度
            if (index % 10 == 0) {
                paint.strokeWidth = (scaleWidth * 2).toFloat()
                //当前线段x轴起点
                val drawX = (index * scaleInterval).toFloat()
                //高度是普通刻度两倍
                canvas.drawLine(
                    drawX, 0f,
                    drawX, scaleHeight.toFloat() * 2, paint
                )
                //绘制文字
                canvas.drawText("$index", drawX-scaleTextSize/2, scaleHeight.toFloat() * 3, paint)
            } else {
                paint.strokeWidth = scaleWidth.toFloat()
                //当前线段x轴起点
                val drawX = (index * scaleInterval).toFloat()
                canvas.drawLine(
                    drawX, 0f,
                    drawX, scaleHeight.toFloat(), paint
                )
            }
        }
    }

    /**
     * 记录上一次move时的坐标，用于计算每次move的差量
     */
    private var lastX = 0
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //开始速度检测,每个事件序列创建一个
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
        velocityTracker?.addMovement(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x.toInt()
                //按下停止scroller，避免继续惯性滑动
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                }
                //按下时通知父View不要对事件进行拦截
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                currentScrollX -= (event.x - lastX)
                //超出最大滑动范围
                if (currentScrollX > maxScrollX) {
                    currentScrollX = maxScrollX.toFloat()
                }
                //超出最小滑动范围
                if (currentScrollX < minScrollX) {
                    currentScrollX = minScrollX.toFloat()
                }
                scrollTo(currentScrollX.roundToInt(), 0)
                //记录当前坐标
                lastX = event.x.toInt()
                postInvalidate()
                changeScale()
            }
            MotionEvent.ACTION_UP -> {
                velocityTracker?.computeCurrentVelocity(1000, maxVelocity.toFloat())
                val velocityX = velocityTracker!!.xVelocity.toInt()
                //大于可滑动速度
                if (abs(velocityX) > minVelocity) {
                    fling(-velocityX)
                }
                //没有触发惯性滑动，矫正刻度
                else {
                    correctScale()
                }
                //VelocityTracker回收
                velocityTracker?.recycle()
                velocityTracker = null
            }
        }
        return true
    }

    /**
     * 矫正刻度
     */
    private fun correctScale() {
        //x轴偏移量与间隔区域
        val remainder = (currentScrollX % scaleInterval).toInt()
        //如果跟指示器未对其
        if (remainder != 0) {
            //矫正目标刻度
            val correctScrollX =
                if (remainder > (scaleInterval / 2)) {
                    scaleInterval - remainder
                } else {
                    -remainder
                }
            scroller.startScroll(currentScrollX.toInt(), 0, correctScrollX, 0)
        }
    }

    /**
     * 惯性滑动
     * @param vX 单位时间内x轴位移
     */
    private fun fling(vX: Int) {
        scroller.fling(
            currentScrollX.toInt(), 0,
            vX, 0,
            minScrollX, maxScrollX,
            0, 0
        )
        invalidate()
    }

    /**
     * draw内部会调用，专门用户处理滑动。
     */
    override fun computeScroll() {
        //滚动未完成完成，已完成就停止刷新界面
        if (scroller.computeScrollOffset()) {
            currentScrollX = scroller.currX.toFloat()
            //滚动view
            scrollTo(currentScrollX.toInt(), 0)
            //刷新界面
            postInvalidate()
            //最后一次惯性滑动,进行矫正刻度
            if (!scroller.computeScrollOffset()) {
                correctScale()
            }
            //改变当前刻度
            changeScale()
        }
        super.computeScroll()
    }

    /**
     * 改变刻度
     */
    private fun changeScale() {
        val scale = currentScrollX / scaleInterval + indicatorPointX / scaleInterval
        scrollListener?.invoke(scale.toInt())
    }

    /**
     * 添加滚动方法
     */
    fun addScrollListener(scrollListener: (Int) -> Unit) {
        this.scrollListener = scrollListener
    }
}