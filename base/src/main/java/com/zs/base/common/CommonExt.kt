package com.zs.base.common

import android.content.Context

/**
 * des 公用扩展函数
 * @date 2020/7/22
 * @author zs
 */


/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun dip2px(context: Context, dpValue: Float): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun px2dip(context: Context, dpValue: Float): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (dpValue / scale + 0.5f).toInt()

}

/**
 * 获得屏幕宽度,单位dx
 *
 * @param context
 * @return
 */
fun getScreenWidthPx(context: Context): Int {
    return context.resources.displayMetrics.widthPixels
}

/**
 * 获得屏幕宽度,单位px
 *
 * @param context
 * @return
 */
fun getScreenHeightPx(context: Context): Int {
    return context.resources.displayMetrics.heightPixels
}


/**
 * 获得屏幕宽度,单位dp
 *
 * @param context
 * @return
 */
fun getScreenWidthDp(context: Context): Int {
    return px2dip(context, getScreenWidthPx(context).toFloat())
}

/**
 * 获得屏幕宽度,单位dp
 *
 * @param context
 * @return
 */
fun getScreenHeightDp(context: Context): Int {
    return px2dip(context, getScreenHeightPx(context).toFloat())
}