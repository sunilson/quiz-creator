package com.sunilson.quizcreator.presentation.quizActivity.di

import android.content.Context
import com.sunilson.quizcreator.application.di.scopes.ActivityScope
import com.sunilson.quizcreator.presentation.quizActivity.QuizActivity
import dagger.Binds
import dagger.Module

@Module
abstract class QuizActivityModule {
    @Binds
    @ActivityScope
    abstract fun provideMainActivity(quizActivity: QuizActivity): Context
}