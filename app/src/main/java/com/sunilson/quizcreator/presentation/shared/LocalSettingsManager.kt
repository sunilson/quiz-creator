package com.sunilson.quizcreator.presentation.shared

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalSettingsManager @Inject constructor(context: Application) {

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    var lastUsedCategory: String?
        get() {
            return sharedPrefs.getString("lastCategory", null)
        }
        set(value) {
            if (value != null && value.isNotEmpty()) {
                val editor = sharedPrefs.edit()
                editor.putString("lastCategory", value)
                editor.commit()
            }
        }

    var maxQuestionAmount: Int
        get() {
            return sharedPrefs.getInt("maxQuestionAmount", 10)
        }
        set(value) {
            if (value > 0) {
                val editor = sharedPrefs.edit()
                editor.putInt("maxQuestionAmount", value)
                editor.commit()
            }
        }

    var tutorial: Boolean
        get() {
            return sharedPrefs.getBoolean("tutorial", true)
        }
        set(value) {
            val editor = sharedPrefs.edit()
            editor.putBoolean("tutorial", value)
            editor.commit()
        }
}