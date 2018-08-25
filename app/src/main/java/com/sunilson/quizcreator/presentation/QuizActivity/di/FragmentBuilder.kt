package com.sunilson.quizcreator.presentation.QuizActivity.di

import com.sunilson.quizcreator.Application.di.scopes.FragmentScope
import com.sunilson.quizcreator.presentation.QuizActivity.fragments.QuizFragment.QuizFragment
import com.sunilson.quizcreator.presentation.QuizActivity.fragments.ResultFragment.ResultFragment
import com.sunilson.quizcreator.presentation.QuizActivity.fragments.SingleQuizFragment.SingleQuizFragment
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