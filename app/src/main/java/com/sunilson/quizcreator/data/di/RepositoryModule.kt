package com.sunilson.quizcreator.data.di

import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.QuizRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val repositoryModule = module {
    single<IQuizRepository> { QuizRepository(androidApplication(), get()) }
}