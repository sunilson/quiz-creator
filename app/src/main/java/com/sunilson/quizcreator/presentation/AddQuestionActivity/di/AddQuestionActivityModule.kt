package com.sunilson.quizcreator.presentation.AddQuestionActivity.di

import android.content.Context
import com.sunilson.quizcreator.Application.di.ActivityScope
import com.sunilson.quizcreator.presentation.AddQuestionActivity.AddQuestionActivity
import dagger.Binds
import dagger.Module

@Module
abstract class AddQuestionActivityModule {

    @Binds
    @ActivityScope
    abstract fun provideAddQuestionActivity(addQuestionActivity: AddQuestionActivity) : Context

}