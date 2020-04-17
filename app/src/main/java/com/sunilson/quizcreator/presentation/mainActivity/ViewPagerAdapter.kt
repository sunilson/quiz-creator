package com.sunilson.quizcreator.presentation.mainActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sunilson.quizcreator.presentation.mainActivity.fragments.allQuestionsFragment.AllQuestionsFragment
import com.sunilson.quizcreator.presentation.mainActivity.fragments.categoriesFragment.CategoriesFragment
import com.sunilson.quizcreator.presentation.mainActivity.fragments.createQuizFragment.CreateQuizFragment
import com.sunilson.quizcreator.presentation.mainActivity.fragments.statisticsFragment.StatisticsFragment


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
