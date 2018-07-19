package com.sunilson.quizcreator.Application.di

import com.sunilson.quizcreator.presentation.AddQuestionActivity.AddQuestionActivity
import com.sunilson.quizcreator.presentation.AddQuestionActivity.di.AddQuestionActivityModule
import com.sunilson.quizcreator.presentation.MainActivity.MainActivity
import com.sunilson.quizcreator.presentation.MainActivity.di.FragmentBuilder
import com.sunilson.quizcreator.presentation.MainActivity.di.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [FragmentBuilder::class, MainActivityModule::class])
    @ActivityScope
    abstract  fun provideMainActivity():MainActivity

    @ContributesAndroidInjector(modules = [AddQuestionActivityModule::class])
    @ActivityScope
    abstract fun provideAddQuestionActivity():AddQuestionActivity

}