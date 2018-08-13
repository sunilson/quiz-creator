package com.sunilson.quizcreator.presentation.shared.KotlinExtensions

import android.databinding.BaseObservable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class NotifyPropertyChangedDelegate<T>(private var value: T, private val propertyId: Int) : ReadWriteProperty<BaseObservable, T> {
    override fun getValue(thisRef: BaseObservable, property: KProperty<*>): T = value
    override fun setValue(thisRef: BaseObservable, property: KProperty<*>, value: T) {
        if (this.value !== value) {
            this.value = value
            thisRef.notifyPropertyChanged(propertyId)
        }
    }
}