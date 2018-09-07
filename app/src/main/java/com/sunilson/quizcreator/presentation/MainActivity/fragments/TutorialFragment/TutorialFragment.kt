package com.sunilson.quizcreator.presentation.MainActivity.fragments.TutorialFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.MainActivity.OnBackPressedListener
import com.sunilson.quizcreator.presentation.MainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.shared.LocalSettingsManager
import com.sunilson.quizcreator.presentation.views.TutorialSlider.TutorialSlide
import kotlinx.android.synthetic.main.fragment_tutorial.view.*
import javax.inject.Inject

class TutorialFragment : BaseFragment(), OnBackPressedListener {

    @Inject
    lateinit var localSettingsManager: LocalSettingsManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tutorial, container, false)
        view.slider.slides = listOf(
                TutorialSlide(getString(R.string.welcome), R.drawable.web_hi_res_512, Pair(300, 300)),
                TutorialSlide(getString(R.string.tutorial_category_tab), R.drawable.tutorial_categories),
                TutorialSlide(getString(R.string.tutorial_question_tab), R.drawable.tutorial_create_question),
                TutorialSlide(getString(R.string.tutorial_microphone), R.drawable.tutorial_microphone),
                TutorialSlide(getString(R.string.tutorial_answer_pool), R.drawable.tutorial_add_answers),
                TutorialSlide(getString(R.string.tutorial_answer_options), R.drawable.tutorial_answer_options),
                TutorialSlide(getString(R.string.tutorial_quiz), R.drawable.tutorial_quiz),
                TutorialSlide(getString(R.string.tutorial_statistics), R.drawable.tutorial_statistics),
                TutorialSlide(getString(R.string.tutorial_end), R.drawable.web_hi_res_512, Pair(300, 300))
        )
        view.slider.setTutorialListener {
            localSettingsManager.tutorial = true
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