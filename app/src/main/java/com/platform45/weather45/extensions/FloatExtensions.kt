package com.platform45.weather45.extensions

import android.content.Context
import android.util.DisplayMetrics

fun Float.pixelToDp(context: Context): Float {
    val scale = context.resources.displayMetrics.density
    val dp = (this * scale + 0.5f).toInt()
    return dp.toFloat()
}

fun Float.dpToPixel(context: Context): Float {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return this * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Float.pixelToSp(context: Context): Float {
    val sp = this / context.resources.displayMetrics.scaledDensity
    return this
}