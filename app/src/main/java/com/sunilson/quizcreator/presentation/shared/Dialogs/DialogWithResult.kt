package com.sunilson.quizcreator.presentation.shared.Dialogs

interface DialogWithResult<T> {
    var listener: DialogListener<T>?
}

interface DialogListener<in T> {
    fun onResult(result: T?)
}