package com.sunilson.quizcreator.presentation.quizActivity.di

import com.sunilson.quizcreator.presentation.quizActivity.QuizActivity
import com.sunilson.quizcreator.presentation.quizActivity.QuizViewModel
import com.sunilson.quizcreator.presentation.quizActivity.fragments.singleQuizFragment.SingleQuizRecyclerAdapter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val quizModule = module {
    scope<QuizActivity> {
        scoped { QuizViewModel(get()) }
    }

    factory { SingleQuizRecyclerAdapter(androidContext()) }
}