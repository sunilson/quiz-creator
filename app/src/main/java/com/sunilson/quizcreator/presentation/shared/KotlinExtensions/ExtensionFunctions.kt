package com.sunilson.quizcreator.presentation.shared.KotlinExtensions

import android.content.Context
import android.util.DisplayMetrics
import android.widget.Toast

fun Int.convertToPx(context: Context) : Int {
    return (this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}

fun Int.convertToDp(context: Context) : Int {
    return (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}

fun Float.convertToPx(context: Context) : Float {
    return (this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
}

fun Float.convertToDp(context: Context) : Float {
    return (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
}

fun Int.clamp(min: Int, max: Int) : Int {
    return Math.max(min, Math.min(this, max))
}

fun Context.showToast(message: String? = "No message given!", duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}