package com.sunilson.quizcreator.presentation.QuizActivity.di

import android.content.Context
import com.sunilson.quizcreator.Application.di.scopes.ActivityScope
import com.sunilson.quizcreator.presentation.QuizActivity.QuizActivity
import dagger.Binds
import dagger.Module

@Module
abstract class QuizActivityModule {
    @Binds
    @ActivityScope
    abstract fun provideMainActivity(quizActivity: QuizActivity): Context
}