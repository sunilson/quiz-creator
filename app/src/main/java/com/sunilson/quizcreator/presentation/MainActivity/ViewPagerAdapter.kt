package com.sunilson.quizcreator.presentation.MainActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.sunilson.quizcreator.presentation.MainActivity.fragments.AllQuestionsFragment.AllQuestionsFragment
import com.sunilson.quizcreator.presentation.MainActivity.fragments.HomeFragment
import com.sunilson.quizcreator.presentation.MainActivity.fragments.CreateQuizFragment.CreateQuizFragment


class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AllQuestionsFragment.newInstance()
            1 -> HomeFragment.newInstance()
            2 -> CreateQuizFragment.newInstance()
            else -> CreateQuizFragment.newInstance()
        }
    }

    override fun getCount(): Int = 3
}
