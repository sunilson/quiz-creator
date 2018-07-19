package com.sunilson.quizcreator.presentation.MainActivity.di

import android.content.Context
import com.sunilson.quizcreator.Application.di.ActivityScope
import com.sunilson.quizcreator.presentation.MainActivity.MainActivity
import dagger.Binds
import dagger.Module

@Module
abstract class MainActivityModule {

    @Binds
    @ActivityScope
    abstract fun provideMainActivity(mainActivity: MainActivity) : Context

}