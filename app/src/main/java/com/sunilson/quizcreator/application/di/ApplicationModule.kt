package com.sunilson.quizcreator.application.di

import android.speech.SpeechRecognizer
import com.sunilson.quizcreator.presentation.shared.EventBus
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val applicationModule = module {
    single { SpeechRecognizer.createSpeechRecognizer(androidApplication()) }
    single { EventBus() }
}