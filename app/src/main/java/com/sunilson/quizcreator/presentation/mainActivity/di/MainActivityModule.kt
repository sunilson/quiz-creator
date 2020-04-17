package com.sunilson.quizcreator.presentation.mainActivity.di

import com.sunilson.quizcreator.presentation.mainActivity.fragments.allQuestionsFragment.AllQuestionsFragment
import com.sunilson.quizcreator.presentation.mainActivity.fragments.allQuestionsFragment.AllQuestionsViewModel
import com.sunilson.quizcreator.presentation.mainActivity.fragments.allQuestionsFragment.QuestionsRecyclerAdapterFactory
import com.sunilson.quizcreator.presentation.mainActivity.fragments.categoriesFragment.CategoriesFragment
import com.sunilson.quizcreator.presentation.mainActivity.fragments.categoriesFragment.CategoriesRecyclerAdapterFactory
import com.sunilson.quizcreator.presentation.mainActivity.fragments.categoriesFragment.CategoriesViewModel
import com.sunilson.quizcreator.presentation.mainActivity.fragments.createQuizFragment.CreateQuizFragment
import com.sunilson.quizcreator.presentation.mainActivity.fragments.createQuizFragment.CreateQuizViewModel
import com.sunilson.quizcreator.presentation.mainActivity.fragments.createQuizFragment.QuizArchiveRecyclerAdapterFactory
import com.sunilson.quizcreator.presentation.mainActivity.fragments.statisticsFragment.StatisticsFragment
import com.sunilson.quizcreator.presentation.mainActivity.fragments.statisticsFragment.StatisticsFragmentViewModel
import com.sunilson.quizcreator.presentation.shared.CategorySpinnerAdapter
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mainActivityModule = module {
    scope<AllQuestionsFragment> {
        scoped { AllQuestionsViewModel(androidApplication(), get(), get()) }
        scoped { QuestionsRecyclerAdapterFactory(androidContext()) }
    }
    scope<CategoriesFragment> {
        scoped { CategoriesViewModel(get(), get()) }
        scoped { CategoriesRecyclerAdapterFactory(androidContext()) }
    }
    scope<StatisticsFragment> {
        scoped { StatisticsFragmentViewModel(get()) }
    }
    scope<CreateQuizFragment> {
        scoped { CreateQuizViewModel(get(), get()) }
        scoped { QuizArchiveRecyclerAdapterFactory(androidContext()) }
    }
}