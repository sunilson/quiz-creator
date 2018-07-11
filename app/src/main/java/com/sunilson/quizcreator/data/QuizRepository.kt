package com.sunilson.quizcreator.data

import com.sunilson.quizcreator.data.models.Quiz

interface QuizRepository {
    fun generateQuiz(quizLength: Int) : Quiz
}