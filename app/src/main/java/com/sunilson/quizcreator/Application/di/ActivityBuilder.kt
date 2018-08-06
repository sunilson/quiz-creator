package com.sunilson.quizcreator.Application.di

import com.sunilson.quizcreator.Application.di.scopes.ActivityScope
import com.sunilson.quizcreator.Application.di.scopes.DialogFragmentScope
import com.sunilson.quizcreator.presentation.AddQuestionActivity.AddQuestionActivity
import com.sunilson.quizcreator.presentation.AddQuestionActivity.di.AddQuestionActivityModule
import com.sunilson.quizcreator.presentation.MainActivity.MainActivity
import com.sunilson.quizcreator.presentation.MainActivity.di.FragmentBuilder
import com.sunilson.quizcreator.presentation.MainActivity.di.MainActivityModule
import com.sunilson.quizcreator.presentation.QuizActivity.QuizActivity
import com.sunilson.quizcreator.presentation.shared.Dialogs.ImportExportDialog.ImportExportDialog
import com.sunilson.quizcreator.presentation.shared.Dialogs.NumberSelectionDialog.NumberSelectionDialog
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [FragmentBuilder::class, MainActivityModule::class])
    @ActivityScope
    abstract fun provideMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [AddQuestionActivityModule::class])
    @ActivityScope
    abstract fun provideAddQuestionActivity(): AddQuestionActivity

    @ContributesAndroidInjector(modules = [com.sunilson.quizcreator.presentation.QuizActivity.di.FragmentBuilder::class])
    @ActivityScope
    abstract fun provideQuizActivity(): QuizActivity

    @ContributesAndroidInjector()
    @DialogFragmentScope
    abstract fun provideNumberSelectionDialog(): NumberSelectionDialog

    @ContributesAndroidInjector()
    @DialogFragmentScope
    abstract fun provideImportDialog(): ImportExportDialog

}