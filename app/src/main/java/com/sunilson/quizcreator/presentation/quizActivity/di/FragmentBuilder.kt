package com.sunilson.quizcreator.presentation.quizActivity.di

import com.sunilson.quizcreator.application.di.scopes.FragmentScope
import com.sunilson.quizcreator.presentation.quizActivity.fragments.quizFragment.QuizFragment
import com.sunilson.quizcreator.presentation.quizActivity.fragments.resultFragment.ResultFragment
import com.sunilson.quizcreator.presentation.quizActivity.fragments.singleQuizFragment.SingleQuizFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector()
    @FragmentScope
    abstract fun provideQuizFragment(): QuizFragment

    @ContributesAndroidInjector()
    @FragmentScope
    abstract fun provideResultFragment(): ResultFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun provideSingleQuizFragment(): SingleQuizFragment
}