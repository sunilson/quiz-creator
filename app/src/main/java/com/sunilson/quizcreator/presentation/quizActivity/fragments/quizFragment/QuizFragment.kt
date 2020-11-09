package com.sunilson.quizcreator.presentation.quizActivity.fragments.quizFragment

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.databinding.FragmentQuizBinding
import com.sunilson.quizcreator.presentation.mainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.quizActivity.QuizActivity
import com.sunilson.quizcreator.presentation.quizActivity.QuizViewModel
import com.sunilson.quizcreator.presentation.shared.AudioService
import com.sunilson.quizcreator.presentation.shared.extensions.showToast
import kotlinx.android.synthetic.main.fragment_quiz.view.*
import javax.inject.Inject

class QuizFragment : BaseFragment() {

    @Inject
    lateinit var audioService: AudioService

    val viewModel: QuizViewModel by lazy { (activity!! as QuizActivity).quizViewModel }
    var quizId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentQuizBinding>(inflater, R.layout.fragment_quiz, container, false)
        binding.viewModel = viewModel

        disposable.add(viewModel.generateQuiz(
                arguments!!.getString("selectedCategory"),
                arguments!!.getBoolean("shuffleAnswers"),
                arguments!!.getBoolean("onlySingle"),
                arguments!!.getInt("maxQuestionAmount")).subscribe({}, {
            context?.showToast(it.message)
            activity!!.finish()
        }))

        val view = binding.root
        view.quiz_view.quizFinishedCallback = {
            disposable.add(viewModel.storeQuiz().subscribe({
                (activity as QuizActivity).showResult()
            }, {
                (activity as QuizActivity).showResult()
                context?.showToast(getString(R.string.store_quiz_error))
            }))
        }
        view.quiz_view.audioService = audioService

        return view
    }

    companion object {
        fun newInstance(selectedCategory: String?, shuffleAnswers: Boolean, onlySingle: Boolean, maxQuestionAmount: Int): QuizFragment {
            val bundle = Bundle()
            if (selectedCategory != null) bundle.putString("selectedCategory", selectedCategory)
            bundle.putBoolean("onlySingle", onlySingle)
            bundle.putBoolean("shuffleAnswers", shuffleAnswers)
            bundle.putInt("maxQuestionAmount", maxQuestionAmount)
            val fragment = QuizFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}