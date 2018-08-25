package com.sunilson.quizcreator.presentation.MainActivity.fragments.TutorialFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.MainActivity.OnBackPressedListener
import com.sunilson.quizcreator.presentation.MainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.views.TutorialSlider.TutorialSlide
import kotlinx.android.synthetic.main.fragment_tutorial.view.*

class TutorialFragment : BaseFragment(), OnBackPressedListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tutorial, container, false)
        view.slider.slides = listOf(
                TutorialSlide("Slide 1", 1),
                TutorialSlide("Slide 2", 1),
                TutorialSlide("Slide 3", 1),
                TutorialSlide("Slide 4", 1),
                TutorialSlide("Slide 5", 1)
        )
        view.slider.setTutorialListener {
            fragmentManager?.popBackStack()
        }

        return view
    }

    override fun onBackPressed(): Boolean {
        fragmentManager?.popBackStack()
        return false
    }

    companion object {
        fun newInstane(): TutorialFragment {
            return TutorialFragment()
        }
    }
}