package com.sunilson.quizcreator.presentation.QuizActivity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.databinding.FragmentQuizBinding
import com.sunilson.quizcreator.presentation.MainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.showToast

class QuizFragment : BaseFragment() {

    val viewModel: QuizViewModel by lazy { (activity!! as QuizActivity).quizViewModel }
    var quizId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentQuizBinding>(inflater, R.layout.fragment_quiz, container, false)
        binding.viewModel = viewModel
        quizId = arguments?.getString("quizId") ?: ""
        if (quizId.isEmpty()) activity!!.finish()
        else {
            disposable.add(viewModel.loadQuiz(quizId).subscribe({}, {
                context?.showToast(getString(R.string.quiz_not_valid))
                activity!!.finish()
            }))
        }
        val view = binding.root
        return view
    }

    companion object {
        fun newInstance(quizId: String): QuizFragment {
            val bundle = Bundle()
            bundle.putString("quizId", quizId)
            val fragment = QuizFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}