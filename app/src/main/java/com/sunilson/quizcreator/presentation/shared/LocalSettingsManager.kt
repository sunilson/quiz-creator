package com.sunilson.quizcreator.presentation.shared

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalSettingsManager @Inject constructor(context: Application) {

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun getLastUsedCategory(): String? {
        return sharedPrefs.getString("lastCategory", null)
    }

    fun setLastUsedCategory(id: String) {
        if(id.isNotEmpty()) {
            val editor = sharedPrefs.edit()
            editor.putString("lastCategory", id)
            editor.commit()
        }
    }
}