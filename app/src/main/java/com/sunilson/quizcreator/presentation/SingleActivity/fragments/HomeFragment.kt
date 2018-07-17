package com.sunilson.quizcreator.presentation.SingleActivity.fragments

import android.os.Bundle
import android.view.*
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.SingleActivity.fragments.AddQuestionFragment.AddQuestionFragment
import com.sunilson.quizcreator.presentation.SingleActivity.fragments.AllQuestionsFragment.AllQuestionsFragment
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.add_questions_button.setOnClickListener {
            mainActivity.replaceFragment(AddQuestionFragment.newInstance(),
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left,
                    R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right
            )
        }

        view.view_questions_button.setOnClickListener {
            mainActivity.replaceFragment(AllQuestionsFragment.newInstance(),
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left,
                    R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right
            )
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