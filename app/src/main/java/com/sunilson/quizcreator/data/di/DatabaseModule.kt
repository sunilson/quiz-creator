package com.sunilson.quizcreator.data.di

import androidx.room.Room
import com.sunilson.quizcreator.data.QuizDatabase
import com.sunilson.quizcreator.presentation.shared.LocalSettingsManager
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single { LocalSettingsManager(androidApplication()) }
    single {
        Room
            .databaseBuilder(androidApplication(), QuizDatabase::class.java, "quiz-database")
            .fallbackToDestructiveMigration().build()
    }
}