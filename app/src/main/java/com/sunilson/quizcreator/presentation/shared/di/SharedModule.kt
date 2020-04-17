package com.sunilson.quizcreator.presentation.shared.di

import com.sunilson.quizcreator.presentation.shared.AudioService
import com.sunilson.quizcreator.presentation.shared.CategorySpinnerAdapter
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharedModule = module {
    factory { CategorySpinnerAdapter(androidContext()) }
    single { AudioService(androidApplication()) }
}