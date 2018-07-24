package com.sunilson.quizcreator.presentation.views.AnswerView

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.sunilson.quizcreator.BR
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.NotifyPropertyChangedDelegate

class AnswerViewModel(answer: Answer, showDivider: Boolean = false) : BaseObservable() {

    @get:Bindable
    var showDivider: Boolean by NotifyPropertyChangedDelegate(showDivider, BR.showDivider)

    @get:Bindable
    var answer: Answer by NotifyPropertyChangedDelegate(answer, BR.showDivider)

    @get:Bindable
    var correct: Boolean? by NotifyPropertyChangedDelegate(null, BR.correct)

    @get:Bindable
    var marked: Boolean by NotifyPropertyChangedDelegate(false, BR.marked)
}