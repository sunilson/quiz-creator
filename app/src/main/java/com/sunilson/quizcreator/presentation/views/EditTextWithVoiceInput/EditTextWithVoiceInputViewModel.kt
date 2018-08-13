package com.sunilson.quizcreator.presentation.views.EditTextWithVoiceInput

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.sunilson.quizcreator.BR
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.NotifyPropertyChangedDelegate

class EditTextWithVoiceInputViewModel(answer: Answer?, optional: Boolean = false, correctToggable: Boolean = false, voiceEnabled: Boolean = true, hint: String = "") : BaseObservable() {

    var text: String = answer?.text ?: ""
        set(value) {
            field = value
            answer?.text = value
            notifyPropertyChanged(BR.text)
        }
        @Bindable
        get() = field

    @get:Bindable
    var answer: Answer? by NotifyPropertyChangedDelegate(answer, BR.showDivider)

    @get:Bindable
    var optional: Boolean by NotifyPropertyChangedDelegate(optional, BR.optional)

    @get:Bindable
    var voiceEnabled: Boolean by NotifyPropertyChangedDelegate(voiceEnabled, BR.voiceEnabled)

    @get:Bindable
    var correctToggable: Boolean by NotifyPropertyChangedDelegate(correctToggable, BR.correctToggable)

    @get:Bindable
    var hint: String by NotifyPropertyChangedDelegate(hint, BR.hint)
}