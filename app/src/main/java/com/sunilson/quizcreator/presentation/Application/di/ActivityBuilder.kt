package com.sunilson.quizcreator.presentation.Application.di

import com.sunilson.quizcreator.presentation.SingleActivity.MainActivity
import com.sunilson.quizcreator.presentation.SingleActivity.di.FragmentBuilder
import com.sunilson.quizcreator.presentation.SingleActivity.di.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [FragmentBuilder::class, MainActivityModule::class])
    @ActivityScope
    abstract  fun provideMainActivity():MainActivity

}