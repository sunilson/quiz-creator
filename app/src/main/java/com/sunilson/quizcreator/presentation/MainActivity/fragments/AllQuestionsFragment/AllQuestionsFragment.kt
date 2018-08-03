package com.sunilson.quizcreator.presentation.MainActivity.fragments.AllQuestionsFragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.R.id.searchbar_background
import com.sunilson.quizcreator.data.models.QuestionType
import com.sunilson.quizcreator.databinding.FragmentAllQuestionBinding
import com.sunilson.quizcreator.presentation.AddQuestionActivity.AddQuestionActivity
import com.sunilson.quizcreator.presentation.MainActivity.fragments.BaseFragment
import com.sunilson.quizcreator.presentation.shared.ADD_QUESTIONS_INTENT
import com.sunilson.quizcreator.presentation.shared.CategorySpinnerAdapter
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.convertToPx
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.showToast
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator
import kotlinx.android.synthetic.main.fragment_all_question.*
import kotlinx.android.synthetic.main.fragment_all_question.view.*
import javax.inject.Inject

class AllQuestionsFragment : BaseFragment() {

    @Inject
    lateinit var questionsRecyclerAdapterFactory: QuestionsRecyclerAdapterFactory

    @Inject
    lateinit var categorySpinnerAdapter: CategorySpinnerAdapter

    @Inject
    lateinit var allQuestionsViewModel: AllQuestionsViewModel

    lateinit var questionsRecyclerAdapter: QuestionsRecyclerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAllQuestionBinding>(inflater, R.layout.fragment_all_question, container, false)
        binding.viewModel = allQuestionsViewModel
        val view = binding.root

        questionsRecyclerAdapter = questionsRecyclerAdapterFactory.create({
            disposable.add(allQuestionsViewModel.deleteQuestion(it).subscribe({
                questionsRecyclerAdapter.remove(it)
            }, {
                context?.showToast(it.message)
            }))
        }, {
            disposable.add(allQuestionsViewModel.updateQuestion(it).subscribe({
                questionsRecyclerAdapter.update(it)
            }, {
                context?.showToast(it.message)
            }))
        }, recyclerView = view.question_recyclerview)

        view.category_spinner.adapter = categorySpinnerAdapter

        view.question_recyclerview.adapter = questionsRecyclerAdapter
        view.question_recyclerview.isNestedScrollingEnabled = true
        view.question_recyclerview.itemAnimator = OvershootInLeftAnimator(1f)
        view.question_recyclerview.itemAnimator!!.addDuration = 300
        view.question_recyclerview.itemAnimator!!.removeDuration = 300
        view.question_recyclerview.itemAnimator!!.moveDuration = 300
        view.question_recyclerview.itemAnimator!!.changeDuration = 300
        view.question_recyclerview.setHasFixedSize(true)
        view.question_recyclerview.layoutManager = LinearLayoutManager(context)

        view.fab_add_single.setOnClickListener {
            view.fab.collapse()
            val intent = Intent(context, AddQuestionActivity::class.java)
            intent.putExtra("type", QuestionType.SINGLE_CHOICE)
            startActivityForResult(intent, ADD_QUESTIONS_INTENT)
        }

        view.fab_add_multiple.setOnClickListener {
            view.fab.collapse()
            val intent = Intent(context, AddQuestionActivity::class.java)
            intent.putExtra("type", QuestionType.MULTIPLE_CHOICE)
            startActivityForResult(intent, ADD_QUESTIONS_INTENT)
        }

        view.searchbar_background.alpha = 0f

        view.close_searchbar.setOnClickListener {
            val params = view.searchbar.layoutParams as ConstraintLayout.LayoutParams
            view.close_searchbar.visibility = View.GONE
            view.searchbar_edittext.visibility = View.GONE
            view.category_spinner_container.visibility = View.GONE

            val backgroundAnimator = ObjectAnimator.ofFloat(searchbar_background, "alpha", 1f, 0f).setDuration(300)
            backgroundAnimator.interpolator = DecelerateInterpolator()

            val animator = ValueAnimator.ofInt(180, 60).setDuration(300)
            animator.interpolator = AccelerateInterpolator()
            animator.addUpdateListener {
                val value = it.animatedValue as Int
                params.height = value.convertToPx(context!!)
                view.searchbar.layoutParams = params
            }

            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    view.open_searchbar.visibility = View.VISIBLE
                    view.searchbar_tags.visibility = View.VISIBLE
                }
            })

            animator.start()
            backgroundAnimator.start()
        }

        view.open_searchbar.setOnClickListener {
            val params = view.searchbar.layoutParams as ConstraintLayout.LayoutParams
            view.open_searchbar.visibility = View.GONE
            view.searchbar_tags.visibility = View.GONE

            val backgroundAnimator = ObjectAnimator.ofFloat(searchbar_background, "alpha", 0f, 1f).setDuration(300)
            backgroundAnimator.interpolator = DecelerateInterpolator()

            val animator = ValueAnimator.ofInt(60, 180).setDuration(300)
            animator.interpolator = AccelerateInterpolator()
            animator.addUpdateListener {
                val value = it.animatedValue as Int
                params.height = value.convertToPx(context!!)
                view.searchbar.layoutParams = params
            }

            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    view.close_searchbar.visibility = View.VISIBLE
                    view.searchbar_edittext.visibility = View.VISIBLE
                    view.category_spinner_container.visibility = View.VISIBLE
                }
            })

            backgroundAnimator.start()
            animator.start()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            ADD_QUESTIONS_INTENT -> {
                if (resultCode == Activity.RESULT_OK) {
                    allQuestionsViewModel.loadQuestions()
                }
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        allQuestionsViewModel.loadQuestions()
        allQuestionsViewModel.loadCategories()
    }

    override fun onDestroy() {
        super.onDestroy()
        allQuestionsViewModel.onDestroy()
    }

    companion object {
        fun newInstance(): AllQuestionsFragment = AllQuestionsFragment()
    }

}