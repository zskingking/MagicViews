package com.example.scala

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import kotlin.math.max
import kotlin.math.min

/**
 * des 加载的图片可以放大缩小
 * @author zs
 * @data 2020/8/29
 */
class ScalableView(context: Context, attrs: AttributeSet) : View(context, attrs)
    , GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private val paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }
    private var bitmap: Bitmap? = null

    /**
     * 额外放缩系数
     */
    private val EXTRA_SCALE = 1.3f

    /**
     * 横纵向初始偏移量。使图片居中显示
     */
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f


    /**
     * 做手势监听
     */
    private val gestureDetector = GestureDetectorCompat(context, this)

    /**
     * 是否为大图状态
     */
    private var isBig = false

    /**
     * 大图缩放比例
     */
    private var bigScale = 1f

    /**
     * 小图缩放比例
     */
    private var smallScale = 1f

    /**
     * 百分比，用于做属性动画
     */
    var percent = 0f
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 用于做缩放
     */
    private val anim by lazy { ObjectAnimator.ofFloat(this, "percent", 0f, 1f) }

    private val overScroller by lazy { OverScroller(context) }

    private var disX = 0
    private var disY = 0

    /**
     * 宽高改变
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (bitmap != null) {
            originalOffsetX = (width.toFloat() - bitmap!!.width.toFloat()) / 2
            originalOffsetY = (height.toFloat() - bitmap!!.height.toFloat()) / 2
            //图片宽高比大于View宽高比，小图水平方向贴边显示,大图垂直方向贴边
            if (bitmap!!.width / bitmap!!.height.toFloat() > width / height.toFloat()) {
                smallScale = width / bitmap!!.width.toFloat()
                bigScale = height / bitmap!!.height.toFloat()
            }
            //图片宽高小于View宽高比，小图垂直方向贴边显示,大图水平方向贴边
            else {
                smallScale = height / bitmap!!.height.toFloat()
                bigScale = width / bitmap!!.width.toFloat()
            }
            bigScale *= EXTRA_SCALE
        }
    }

    /**
     * 设置图片
     */
    fun setImageBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (bitmap != null) {
            val scale = smallScale + (bigScale - smallScale) * percent
            canvas.scale(scale, scale, width / 2f, height / 2f)
            if (!isBig) {
                scrollTo((disX * percent).toInt(), (disY* percent).toInt())
                if (percent == 0f){
                    disX = 0
                    disY = 0
                }
            }
            canvas.drawBitmap(bitmap!!, originalOffsetX, originalOffsetY, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    /**
     * 为按下状态。比如按下时需要加一个背景，此处加即可
     */
    override fun onShowPress(e: MotionEvent) {}

    /**
     * 此处触发点击事件
     */
    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        performClick()
        return false
    }

    /**
     * 接收Down事件。
     */
    override fun onDown(e: MotionEvent?): Boolean {
        //按下停止scroller，避免继续惯性滑动
        if (!overScroller.isFinished) {
            overScroller.abortAnimation()
        }
        //返回true,表示需要消费事件序列
        return true
    }

    /**
     * 快速滚动
     */
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        //大图状态下才能滑动
        if (isBig) {
            val minX = -(bitmap!!.width * bigScale - width).toInt() / 2
            val minY = -(bitmap!!.height * bigScale - height).toInt() / 2
            overScroller.fling(
                disX, disY,
                -velocityX.toInt(), -velocityY.toInt(),
                minX, -minX,
                minY, -minY
            )
            invalidate()
        }
        return true
    }

    override fun computeScroll() {
        //动画未结束
        if (overScroller.computeScrollOffset()) {
            disX = overScroller.currX
            disY = overScroller.currY
            scrollTo(disX, disY)
            invalidate()
        }
        super.computeScroll()
    }

    /**
     * 滚动
     */
    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        //大图状态下才能滑动
        if (isBig) {
            //设置边界值
            disX += distanceX.toInt()
            disX = min(disX, (bitmap!!.width * bigScale - width).toInt() / 2)
            disX = max(disX, -(bitmap!!.width * bigScale - width).toInt() / 2)
            //设置边界值
            disY += distanceY.toInt()
            disY = min(disY, (bitmap!!.height * bigScale - height).toInt() / 2)
            disY = max(disY, -(bitmap!!.height * bigScale - height).toInt() / 2)
            scrollTo(disX, disY)
            invalidate()
        }
        return false
    }

    /**
     * 长按
     */
    override fun onLongPress(e: MotionEvent?) {}

    /**
     * 双击
     */
    override fun onDoubleTap(e: MotionEvent?): Boolean {
        if (isBig) {
            anim.reverse()
        } else {
            anim.start()
        }
        isBig = !isBig
        return true
    }

    /**
     *
     */
    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }

    /**
     * 点击事件。两次点击事件间隔小于300毫秒时视为一次单击，可防止重复点击抖动
     */
    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        return false
    }
}