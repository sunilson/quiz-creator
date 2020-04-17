package com.sunilson.quizcreator.presentation.shared.baseClasses

import android.app.Application
import com.sunilson.quizcreator.application.di.applicationModule
import com.sunilson.quizcreator.data.di.databaseModule
import com.sunilson.quizcreator.data.di.repositoryModule
import com.sunilson.quizcreator.presentation.addQuestionActivity.di.addQuestionModule
import com.sunilson.quizcreator.presentation.mainActivity.di.mainActivityModule
import com.sunilson.quizcreator.presentation.quizActivity.di.quizModule
import com.sunilson.quizcreator.presentation.shared.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BaseApplication)
            modules(
                listOf(
                    applicationModule,
                    databaseModule,
                    repositoryModule,
                    addQuestionModule,
                    mainActivityModule,
                    sharedModule,
                    quizModule
                )
            )
        }
    }
}