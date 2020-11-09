package com.sunilson.quizcreator.presentation.mainActivity.di

import android.content.Context
import com.sunilson.quizcreator.application.di.scopes.ActivityScope
import com.sunilson.quizcreator.presentation.mainActivity.MainActivity
import dagger.Binds
import dagger.Module

@Module
abstract class MainActivityModule {

    @Binds
    @ActivityScope
    abstract fun provideMainActivity(mainActivity: MainActivity) : Context

}