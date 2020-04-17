package com.sunilson.quizcreator.presentation.quizActivity.fragments.quizFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.databinding.FragmentQuizBinding
import com.sunilson.quizcreator.presentation.mainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.quizActivity.QuizActivity
import com.sunilson.quizcreator.presentation.quizActivity.QuizViewModel
import com.sunilson.quizcreator.presentation.shared.AudioService
import com.sunilson.quizcreator.presentation.shared.kotlinExtensions.showToast
import kotlinx.android.synthetic.main.fragment_quiz.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.lifecycleScope

class QuizFragment : BaseFragment() {

    private val audioService: AudioService by inject()
    private val viewModel: QuizViewModel by lazy { requireActivity().lifecycleScope.get<QuizViewModel>() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentQuizBinding>(
            inflater,
            R.layout.fragment_quiz,
            container,
            false
        )
        binding.viewModel = viewModel

        disposable.add(
            viewModel.generateQuiz(
                requireArguments().getString("selectedCategory"),
                requireArguments().getBoolean("shuffleAnswers"),
                requireArguments().getBoolean("onlySingle"),
                requireArguments().getInt("maxQuestionAmount")
            ).subscribe({}, {
                context?.showToast(it.message)
                requireActivity().finish()
            })
        )

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
        fun newInstance(
            selectedCategory: String?,
            shuffleAnswers: Boolean,
            onlySingle: Boolean,
            maxQuestionAmount: Int
        ): QuizFragment {
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