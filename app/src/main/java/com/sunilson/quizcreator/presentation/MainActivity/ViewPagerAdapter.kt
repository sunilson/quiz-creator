package com.sunilson.quizcreator.presentation.MainActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.sunilson.quizcreator.presentation.MainActivity.fragments.AllQuestionsFragment.AllQuestionsFragment
import com.sunilson.quizcreator.presentation.MainActivity.fragments.CategoriesFragment.CategoriesFragment
import com.sunilson.quizcreator.presentation.MainActivity.fragments.CreateQuizFragment.CreateQuizFragment
import com.sunilson.quizcreator.presentation.MainActivity.fragments.StatisticsFragment.StatisticsFragment


class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> CategoriesFragment.newInstance()
            1 -> AllQuestionsFragment.newInstance()
            2 -> StatisticsFragment.newInstance()
            3 -> CreateQuizFragment.newInstance()
            else -> CreateQuizFragment.newInstance()
        }
    }

    override fun getCount(): Int = 4
}
