package com.sunilson. quizcreator.presentation.views.QuizView

import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Question

class QuestionCardViewModel(val question: Question, val callback: ((Answer) -> Unit)) {
    fun answerClicked(answer: Answer) {
        callback(answer)
    }
}