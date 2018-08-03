package com.sunilson.quizcreator.presentation.MainActivity.di

import com.sunilson.quizcreator.Application.di.scopes.FragmentScope
import com.sunilson.quizcreator.presentation.MainActivity.fragments.AllQuestionsFragment.AllQuestionsFragment
import com.sunilson.quizcreator.presentation.MainActivity.fragments.CategoriesFragment.CategoriesFragment
import com.sunilson.quizcreator.presentation.MainActivity.fragments.CreateQuizFragment.CreateQuizFragment
import com.sunilson.quizcreator.presentation.MainActivity.fragments.StatisticsFragment.StatisticsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun contributeHomeFragment(): StatisticsFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun contributeAllQuestionFragment(): AllQuestionsFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun contributeQuizFragment(): CreateQuizFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun contributeCategoryFragment(): CategoriesFragment

}