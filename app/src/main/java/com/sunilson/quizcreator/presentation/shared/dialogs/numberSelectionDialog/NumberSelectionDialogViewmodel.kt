package com.sunilson.quizcreator.presentation.shared.dialogs.numberSelectionDialog

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.sunilson.quizcreator.application.di.scopes.DialogFragmentScope
import com.sunilson.quizcreator.BR
import com.sunilson.quizcreator.presentation.shared.extensions.NotifyPropertyChangedDelegate
import javax.inject.Inject

@DialogFragmentScope
class NumberSelectionDialogViewmodel @Inject constructor() : BaseObservable() {

    @get:Bindable
    var currentValue: Int by NotifyPropertyChangedDelegate(0, BR.currentValue)

    @get:Bindable
    var title: String by NotifyPropertyChangedDelegate("", BR.title)

    @get:Bindable
    var max: Int by NotifyPropertyChangedDelegate(20, BR.max)

}