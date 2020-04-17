package com.sunilson.quizcreator.presentation.shared.kotlinExtensions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.DisplayMetrics
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.presentation.views.editTextWithVoiceInput.EditTextWithVoiceInput
import java.util.*

fun Int.convertToPx(context: Context): Int {
    return (this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}

fun Int.convertToDp(context: Context): Int {
    return (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}

fun Float.convertToPx(context: Context): Float {
    return (this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
}

fun Float.convertToDp(context: Context): Float {
    return (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
}

fun Int.clamp(min: Int, max: Int): Int {
    return Math.max(min, Math.min(this, max))
}

fun Context.hasPermission(permission: String): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}

fun Context.showToast(message: String? = "No message given!", duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun MutableList<Answer>.popRandomElement(id: String, chance: Float = 0.5f): Answer {
    val random = Random()
    return if (random.nextFloat() <= chance) {
        val randomInt = random.nextInt(this.size)
        val result = this[randomInt]
        this.removeAt(randomInt)
        result
    } else {
        val correspondingAnswer = this.find { it.id == id }
        this.remove(correspondingAnswer)
        correspondingAnswer!!
    }
}

fun LinearLayout.getAnswers(): MutableList<Answer> {
    val result = mutableListOf<Answer>()
    for (i in 0 until this.childCount) {
        val answerEditText = this.getChildAt(i) as EditTextWithVoiceInput
        result.add(answerEditText.answer ?: Answer())
    }
    return result
}