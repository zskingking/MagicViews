package com.zs.ruler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import com.zs.base.common.dip2px
import kotlin.math.roundToInt

/**
 * des 刻度尺
 * @date 2020/7/22
 * @author zs
 */
class RulerView(context: Context, attrs: AttributeSet) :
    View(context, attrs), GestureDetector.OnGestureListener {

    /**
     * 画笔
     */
    private val paint by lazy { Paint() }

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
     * 刻度上方是否是圆角
     */
    private var isRadius = false

    /**
     * 刻度间隔。默认10dp
     */
    private var scaleInterval = dip2px(context, 10f)

    /**
     * 刻度数量，默认为200
     */
    private var maxScale = 200

    /**
     * 用作手势监听
     */
    private var gestureDetector = GestureDetectorCompat(context, this)

    /**
     * 滑动帮助类
     */
    private var scroller = OverScroller(context)

    /**
     * 超出最大/最小值允许偏移量
     */
    private var allowOffsetX = 0

    /**
     * 最小滚动距离
     */
    private var minScrollX = 0

    /**
     * 最大滚动距离
     */
    private var maxScrollX = 0

    /**
     * 当前偏移量
     */
    private var currentScrollX = 0f

    /**
     *  初始化
     */
    init {
        //去除锯齿
        paint.isAntiAlias = true
        paint.color = scaleColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //因为指示器在中间，所以最大最小偏移量都为宽度一半
        allowOffsetX = width / 2

        //最小滚动距离,-刻度数 * 间隔 + width - allowOffsetX
        minScrollX = -allowOffsetX
        //最大滚动距离,刻度数 * 间隔 - width + allowOffsetX
        maxScrollX = maxScale * scaleInterval - width + allowOffsetX

        Log.i("RulerView", "minScrollX::${minScrollX}")
        Log.i("RulerView", "maxScrollX::${maxScrollX}")

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
            //每十个刻度又一个粗长刻度
            if (index % 10 == 0) {
                paint.strokeWidth = (scaleWidth * 2).toFloat()
                //当前线段x轴起点
                val drawX = (index * scaleInterval).toFloat()
                //高度是普通刻度两倍
                canvas.drawLine(
                    drawX, 0f,
                    drawX, scaleHeight.toFloat() * 2, paint
                )
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.i("RulerView", "event::${event.x}")
        //通过gestureDetector接管MotionEvent事件
        return gestureDetector.onTouchEvent(event)
    }


    /**
     * 快速滑动松开后调用
     * @param downEvent 按下时的事件
     * @param currentEvent 当前位置的事件
     * @param distanceX 单位时间内x轴移动速度
     * @param distanceY 单位时间内y轴移动速度
     */
    override fun onFling(
        downEvent: MotionEvent,
        currentEvent: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {

        //通过OverScroller启动惯性滑动
        scroller.fling(
            currentScrollX.toInt(), 0,
            -distanceX.toInt(), 0,
            minScrollX, maxScrollX,
            0, 0
        )
        return false
    }

    /**
     * 滚动。基本与ACTION_MOVE等价
     * @param downEvent 按下时的事件
     * @param currentEvent 当前位置的事件
     * @param distanceX x轴相对于上一次的偏移量
     * @param distanceY y轴相对于上一次的偏移量
     */
    override fun onScroll(
        downEvent: MotionEvent,
        currentEvent: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        currentScrollX += distanceX
        //超出最大滑动范围
        if (currentScrollX > maxScrollX) {
            currentScrollX = maxScrollX.toFloat()
        }
        //超出最小滑动范围
        if (currentScrollX < minScrollX) {
            currentScrollX = minScrollX.toFloat()
        }
        scrollTo(currentScrollX.roundToInt(), 0)
        //已经处于目标位置就不做视图重绘
        if (currentScrollX.toInt() == maxScrollX || currentScrollX.toInt() == minScrollX) {
            return false
        }
        //刷新视图
        postInvalidate()
        return false
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
        }
        super.computeScroll()
    }

    /**
     * down事件，并将事件消费
     */
    override fun onDown(p0: MotionEvent): Boolean {
        //停止scroller
        if (!scroller.isFinished) {
            scroller.abortAnimation()
        }
        //按下时通知父View不要对事件进行拦截
        parent.requestDisallowInterceptTouchEvent(true)
        return true
    }

    /**
     * 单击。触发onClick
     */
    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        return performClick()
    }

    /**
     * 长按
     */
    override fun onLongPress(p0: MotionEvent) {}

    /**
     * Android中预按下方案。关于预按下可自行了解
     */
    override fun onShowPress(p0: MotionEvent) {}


}