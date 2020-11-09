package com.sunilson.quizcreator.presentation.quizActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.quizActivity.fragments.quizFragment.QuizFragment
import com.sunilson.quizcreator.presentation.quizActivity.fragments.resultFragment.ResultFragment
import com.sunilson.quizcreator.presentation.quizActivity.fragments.singleQuizFragment.SingleQuizFragment
import com.sunilson.quizcreator.presentation.shared.baseClasses.BaseActivity
import com.sunilson.quizcreator.presentation.shared.dialogs.DialogListener
import com.sunilson.quizcreator.presentation.shared.dialogs.SimpleConfirmDialog
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class QuizActivity : BaseActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var quizViewModel: QuizViewModel

    private var showingArchive: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        if (intent.getStringExtra("id") != null) {
            showingArchive = true
            quizViewModel.loadQuiz(intent.getStringExtra("id").orEmpty())
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, SingleQuizFragment.newInstance()).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, QuizFragment.newInstance(
                    intent.getStringExtra("selectedCategory"),
                    intent.getBooleanExtra("shuffleAnswers", false),
                    intent.getBooleanExtra("onlySingle", false),
                    intent.getIntExtra("maxQuestionAmount", 8)
            )).commit()
        }
    }

    fun showResult() {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, ResultFragment.newInstance()).commit()
    }

    fun showArchive() {
        showingArchive = true
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                .addToBackStack("bla")
                .add(R.id.frame_layout, SingleQuizFragment.newInstance())
                .commit()
    }

    override fun onBackPressed() {
        if (showingArchive) {
            showingArchive = false
            super.onBackPressed()
        } else {
            val dialog = SimpleConfirmDialog.newInstance(getString(R.string.exit_quiz), getString(R.string.exit_quiz_question))
            val exit: () -> Unit = {
                super.onBackPressed()
            }
            dialog.listener = object : DialogListener<Boolean> {
                override fun onResult(result: Boolean?) {
                    result?.let {
                        if (result) exit()
                    }
                }
            }
            dialog.show(supportFragmentManager, "dialog")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun androidInjector() = dispatchingAndroidInjector

}