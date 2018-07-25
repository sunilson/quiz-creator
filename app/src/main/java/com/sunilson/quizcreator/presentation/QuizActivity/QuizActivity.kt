package com.sunilson.quizcreator.presentation.QuizActivity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.QuizActivity.fragments.QuizFragment.QuizFragment
import com.sunilson.quizcreator.presentation.QuizActivity.fragments.ResultFragment.ResultFragment
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseActivity
import com.sunilson.quizcreator.presentation.shared.Dialogs.DialogListener
import com.sunilson.quizcreator.presentation.shared.Dialogs.SimpleConfirmDialog
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.showToast
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class QuizActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var quizViewModel: QuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_quiz)

        val quizId = intent.getStringExtra("quizId")
        if (quizId.isNotEmpty()) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, QuizFragment.newInstance(quizId)).commit()
        } else {
            this.showToast(getString(com.sunilson.quizcreator.R.string.quiz_not_valid))
            finish()
        }
    }

    fun showResult() {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, ResultFragment.newInstance()).commit()
    }

    override fun onBackPressed() {
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


    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

}