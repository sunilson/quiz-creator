package com.sunilson.quizcreator.presentation.SingleActivity.di

import com.sunilson.quizcreator.presentation.Application.di.FragmentScope
import com.sunilson.quizcreator.presentation.SingleActivity.fragments.AddQuestionFragment.AddQuestionFragment
import com.sunilson.quizcreator.presentation.SingleActivity.fragments.AllQuestionsFragment.AllQuestionsFragment
import com.sunilson.quizcreator.presentation.SingleActivity.fragments.HomeFragment
import com.sunilson.quizcreator.presentation.SingleActivity.fragments.QuizFragment.QuizFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun contributeAddQuestionFragment(): AddQuestionFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun contributeAllQuestionFragment(): AllQuestionsFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun contributeQuizFragment(): QuizFragment

}