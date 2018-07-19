package com.sunilson.quizcreator.presentation.MainActivity.fragments

import android.os.Bundle
import android.view.*
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.AddQuestionActivity.AddQuestionViewModel
import kotlinx.android.synthetic.main.fragment_home.view.*
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    @Inject
    lateinit var viewMOdel: AddQuestionViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.add_questions_button.setOnClickListener {
            //mainActivity.replaceFragment(AddQuestionFragment.newInstance())
        }

        view.view_questions_button.setOnClickListener {
            //mainActivity.replaceFragment(AllQuestionsFragment.newInstance())
        }

        view.start_quiz.setOnClickListener {
            //mainActivity.replaceFragment(CreateQuizFragment.newInstance())
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}