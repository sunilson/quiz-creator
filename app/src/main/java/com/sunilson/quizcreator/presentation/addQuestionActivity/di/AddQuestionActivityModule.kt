package com.sunilson.quizcreator.presentation.addQuestionActivity.di

import android.content.Context
import com.sunilson.quizcreator.application.di.scopes.ActivityScope
import com.sunilson.quizcreator.application.di.scopes.FragmentScope
import com.sunilson.quizcreator.presentation.addQuestionActivity.AddQuestionActivity
import com.sunilson.quizcreator.presentation.addQuestionActivity.fragments.AddQuestionFragment
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

}