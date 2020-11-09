package com.sunilson.quizcreator.presentation.mainActivity.di

import com.sunilson.quizcreator.application.di.scopes.FragmentScope
import com.sunilson.quizcreator.presentation.mainActivity.fragments.allQuestionsFragment.AllQuestionsFragment
import com.sunilson.quizcreator.presentation.mainActivity.fragments.categoriesFragment.CategoriesFragment
import com.sunilson.quizcreator.presentation.mainActivity.fragments.createQuizFragment.CreateQuizFragment
import com.sunilson.quizcreator.presentation.mainActivity.fragments.statisticsFragment.StatisticsFragment
import com.sunilson.quizcreator.presentation.mainActivity.fragments.tutorialFragment.TutorialFragment
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

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun contributeTutorialragment(): TutorialFragment

}