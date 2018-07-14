package com.sunilson.quizcreator.presentation.util

import android.content.Context
import android.util.DisplayMetrics

fun Int.convertToPx(context: Context) : Int {
    return (this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}

fun Int.convertToDp(context: Context) : Int {
    return (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}

fun Int.clamp(min: Int, max: Int) : Int {
    return Math.max(min, Math.min(this, max))
}