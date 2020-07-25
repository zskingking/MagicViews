package com.zs.tag

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.zs.base.common.dip2px
import kotlin.math.max

/**
 * des 根据多个tag进行平铺，空间不够进行换行。
 * 该Demo侧重于描述自定义Layout流程，所以一些细节比如Padding之类的未作处理
 * 如想用到项目中建议去搜寻成熟的TagLayout
 *
 * @date 2020/7/23
 * @author zs
 */
class TagLayout(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    /**
     * 水平间距
     */
    private var hSpace = dip2px(context, 10f)

    /**
     * tag底边距
     */
    private var vSpace = dip2px(context, 10f)

    /**
     * 用于存储子View的摆放位置
     */
    private val bounds = mutableListOf<Rect>()

    /**
     * tag劣币哦啊
     */
    private val tagList = mutableListOf<String>()

    /**
     * 关于自定义Layout，一般核心内容都在onMeasure。
     * 测量前必须要知道如何摆放子View，所以在该阶段基本已经计算出子View将如何摆放也即是layout
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //当前View总宽度
        val totalWidth = MeasureSpec.getSize(widthMeasureSpec)
        //使用的宽度。初始值为水平间距，此处可当作左边距
        var widthUsed = hSpace
        //使用的高度
        var heightUsed = vSpace
        //最大一行的宽度
        var maxWidth = 0
        //一行的高度
        var tagHeight = 0
        //遍历子View
        for (index in 0 until childCount) {
            val childView = getChildAt(index)
            //无特殊需求可直接通过该方法对子View进行测量，与View的resolveSize基本类似 源码很简单，感兴趣可自行翻读
            //五个参数对应为：当前子View、widthMeasureSpec，可用宽度(给0,无限制)，heightMeasureSpec，可用高度(给0,无限制)
            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0)
            //处理换行。当 当前剩余宽度不足以存放下一个View，换行
            //注意点：
            if (widthUsed + childView.measuredWidth + hSpace > totalWidth) {
                widthUsed = vSpace
                heightUsed += childView.measuredHeight + vSpace
            }
            //一行的高度。原则上只赋值一次即可
            tagHeight = childView.measuredHeight
            //原则上View是会被测量多次，所以必须加该限制，否则可能无限制添加
            if (bounds.size <= index) {
                bounds.add(Rect())
            }
            val childBound = bounds[index]
            //设置子View位置,四个参数对应上下左右
            childBound.set(
                widthUsed, heightUsed,
                widthUsed + childView.measuredWidth,
                heightUsed + childView.measuredHeight
            )
            //更新可用宽度
            widthUsed += childView.measuredWidth + hSpace
            //更新最大宽度
            maxWidth = max(maxWidth,widthUsed)

        }
        //当前layout宽度
        val tagLayoutWidth = maxWidth
        //当前layout高度。最后也加了个底边距
        val stagLayoutHeight = heightUsed + tagHeight + vSpace
        //完成测量
        setMeasuredDimension(tagLayoutWidth, stagLayoutHeight)
    }

    /**
     * 摆放子View
     */
    override fun onLayout(p0: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (index in 0 until childCount) {
            //获取当前子view的位置
            val bound = bounds[index]
            Log.i("TagLayout", "${bound.left}--${bound.top}--${bound.right}--${bound.bottom}")
            //对子View进行摆放
            getChildAt(index).layout(bound.left, bound.top, bound.right, bound.bottom)
        }
    }

    /**
     * 设置tag
     */
    fun setTag(list: MutableList<String>) {
        post {
            removeAllViews()
            tagList.apply {
                clear()
                addAll(list)
                forEach {
                    val marginLayoutParams =
                        MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    val textView = TextView(context)
                    textView.text = it
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white))
                    textView.textSize = 12f
                    textView.gravity = Gravity.CENTER
                    textView.setBackgroundResource(R.drawable.tag_bg)
                    addView(textView, marginLayoutParams)
                }
            }
        }
    }

    /**
     * 需要转乘MarginLayoutParams,因为子View可能存在Margin
     */
    override fun generateLayoutParams(attrs: AttributeSet?): MarginLayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}