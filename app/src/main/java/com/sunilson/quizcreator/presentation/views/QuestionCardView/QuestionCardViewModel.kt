package com.sunilson.quizcreator.presentation.views.QuestionCardView

import android.databinding.ObservableBoolean
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.QuestionType
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.ItemSelectedListener

class QuestionCardViewModel(val question: Question, val callback: ((Answer) -> Unit)) {
    val answerClicked = object : ItemSelectedListener {
        override fun itemSelected(item: Any) {
            callback(item as Answer)
            if (multipleAnswersPossible) showConfirmButton.set(true)
        }
    }

    val showConfirmButton = ObservableBoolean(false)
    val multipleAnswersPossible = question.type == QuestionType.MULTIPLE_CHOICE
}