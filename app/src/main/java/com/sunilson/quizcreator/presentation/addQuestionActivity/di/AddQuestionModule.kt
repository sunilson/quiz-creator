package com.sunilson.quizcreator.presentation.addQuestionActivity.di

import com.sunilson.quizcreator.presentation.addQuestionActivity.fragments.AddQuestionFragment
import com.sunilson.quizcreator.presentation.addQuestionActivity.fragments.AddQuestionViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val addQuestionModule = module {
    scope<AddQuestionFragment> {
        scoped { AddQuestionViewModel(androidApplication(), get(), get()) }
    }
}