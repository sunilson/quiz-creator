package com.sunilson.quizcreator.application.di

import com.sunilson.quizcreator.application.di.scopes.ActivityScope
import com.sunilson.quizcreator.application.di.scopes.DialogFragmentScope
import com.sunilson.quizcreator.presentation.addQuestionActivity.AddQuestionActivity
import com.sunilson.quizcreator.presentation.addQuestionActivity.di.AddQuestionActivityModule
import com.sunilson.quizcreator.presentation.mainActivity.MainActivity
import com.sunilson.quizcreator.presentation.mainActivity.di.FragmentBuilder
import com.sunilson.quizcreator.presentation.mainActivity.di.MainActivityModule
import com.sunilson.quizcreator.presentation.quizActivity.QuizActivity
import com.sunilson.quizcreator.presentation.quizActivity.di.QuizActivityModule
import com.sunilson.quizcreator.presentation.shared.dialogs.importExportDialog.ImportExportDialog
import com.sunilson.quizcreator.presentation.shared.dialogs.numberSelectionDialog.NumberSelectionDialog
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

    @ContributesAndroidInjector(modules = [com.sunilson.quizcreator.presentation.quizActivity.di.FragmentBuilder::class, QuizActivityModule::class])
    @ActivityScope
    abstract fun provideQuizActivity(): QuizActivity

    @ContributesAndroidInjector()
    @DialogFragmentScope
    abstract fun provideNumberSelectionDialog(): NumberSelectionDialog

    @ContributesAndroidInjector()
    @DialogFragmentScope
    abstract fun provideImportDialog(): ImportExportDialog

}