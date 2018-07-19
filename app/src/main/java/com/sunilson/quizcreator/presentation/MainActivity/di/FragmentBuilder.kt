package com.sunilson.quizcreator.presentation.MainActivity.di

import com.sunilson.quizcreator.Application.di.FragmentScope
import com.sunilson.quizcreator.presentation.MainActivity.fragments.AllQuestionsFragment.AllQuestionsFragment
import com.sunilson.quizcreator.presentation.MainActivity.fragments.CreateQuizFragment.CreateQuizFragment
import com.sunilson.quizcreator.presentation.MainActivity.fragments.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun contributeAllQuestionFragment(): AllQuestionsFragment

    @ContributesAndroidInjector
    @FragmentScope
    abstract fun contributeQuizFragment(): CreateQuizFragment

}