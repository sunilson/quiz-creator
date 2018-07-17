package com.sunilson.quizcreator.data.di

import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.QuizRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideRepository(quizRepository: QuizRepository) : IQuizRepository

}