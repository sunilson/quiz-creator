package com.sunilson.quizcreator.presentation.AddQuestionActivity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.QuestionType
import com.sunilson.quizcreator.presentation.AddQuestionActivity.fragments.AddQuestionFragment
import com.sunilson.quizcreator.presentation.AddQuestionActivity.fragments.AddQuestionTypeFragment
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class AddQuestionActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)
        supportFragmentManager.beginTransaction().replace(R.id.add_question_framelayout, AddQuestionTypeFragment.newInstance()).commit()
    }

    fun startCreation(type: QuestionType) {
        supportFragmentManager.beginTransaction().replace(R.id.add_question_framelayout, AddQuestionFragment.newInstance(type)).commit()
    }

    fun restart(current: Fragment) {
        supportFragmentManager.beginTransaction().remove(current).commit()
        supportFragmentManager.executePendingTransactions()
        supportFragmentManager.beginTransaction().replace(R.id.add_question_framelayout, AddQuestionTypeFragment.newInstance()).commit()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector
}