package com.sunilson.quizcreator.data.di

import android.app.Application
import android.arch.persistence.room.Room
import com.sunilson.quizcreator.data.QuizDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(application: Application): QuizDatabase {
        return Room.databaseBuilder(application, QuizDatabase::class.java, "quiz-database").fallbackToDestructiveMigration().build()
    }
}