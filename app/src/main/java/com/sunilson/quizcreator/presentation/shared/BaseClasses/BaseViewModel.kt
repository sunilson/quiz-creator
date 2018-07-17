package com.sunilson.quizcreator.presentation.shared.BaseClasses

import android.databinding.ObservableBoolean
import android.databinding.ObservableField

abstract class BaseViewModel()  {
    val errorMessage: ObservableField<String> = ObservableField("")
    val loading: ObservableBoolean = ObservableBoolean(true)
    val working: ObservableBoolean = ObservableBoolean(false)
}