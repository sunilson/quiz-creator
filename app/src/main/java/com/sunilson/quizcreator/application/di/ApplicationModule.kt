package com.sunilson.quizcreator.application.di

import android.app.Application
import android.speech.SpeechRecognizer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideSpeechRecognizer(application: Application) : SpeechRecognizer {
        return SpeechRecognizer.createSpeechRecognizer(application)
    }
}