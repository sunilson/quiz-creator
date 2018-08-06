package com.sunilson.quizcreator.Application.di

import android.app.Application
import com.sunilson.quizcreator.data.di.DatabaseModule
import com.sunilson.quizcreator.data.di.RepositoryModule
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    DatabaseModule::class,
    ActivityBuilder::class,
    ApplicationModule::class,
    RepositoryModule::class
])
interface ApplicationComponent {
    @dagger.Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(application: BaseApplication)
}