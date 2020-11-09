package com.sunilson.quizcreator.presentation.shared.dialogs

interface DialogWithResult<T> {
    var listener: DialogListener<T>?
}

interface DialogListener<in T> {
    fun onResult(result: T?)
}