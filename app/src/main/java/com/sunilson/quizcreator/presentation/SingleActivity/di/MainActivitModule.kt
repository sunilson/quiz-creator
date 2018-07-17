package com.sunilson.quizcreator.presentation.SingleActivity.di

import android.content.Context
import com.sunilson.quizcreator.presentation.Application.di.ActivityScope
import com.sunilson.quizcreator.presentation.SingleActivity.MainActivity
import dagger.Binds
import dagger.Module

@Module
abstract class MainActivityModule {

    @Binds
    @ActivityScope
    abstract fun provideMainActivity(mainActivity: MainActivity) : Context

}