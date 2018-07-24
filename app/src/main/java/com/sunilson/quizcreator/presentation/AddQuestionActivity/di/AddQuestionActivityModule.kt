package com.sunilson.quizcreator.presentation.AddQuestionActivity.di

import android.content.Context
import com.sunilson.quizcreator.Application.di.ActivityScope
import com.sunilson.quizcreator.Application.di.FragmentScope
import com.sunilson.quizcreator.presentation.AddQuestionActivity.AddQuestionActivity
import com.sunilson.quizcreator.presentation.AddQuestionActivity.fragments.AddQuestionFragment
import com.sunilson.quizcreator.presentation.AddQuestionActivity.fragments.AddQuestionTypeFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AddQuestionActivityModule {

    @Binds
    @ActivityScope
    abstract fun provideAddQuestionActivity(addQuestionActivity: AddQuestionActivity): Context

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun contributeAddQuestionFragment(): AddQuestionFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun contributeAddQuestionTypeFragment(): AddQuestionTypeFragment

}