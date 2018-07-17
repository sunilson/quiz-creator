package com.sunilson.quizcreator.presentation.SingleActivity.fragments

import android.content.Context
import android.support.v4.app.Fragment
import com.sunilson.quizcreator.presentation.SingleActivity.MainActivity
import com.sunilson.quizcreator.presentation.SingleActivity.QuizViewModel
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment : Fragment() {

    protected val disposable: CompositeDisposable = CompositeDisposable()
    protected lateinit var mainActivity: MainActivity
    protected lateinit var viewModel: QuizViewModel

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        mainActivity = activity as MainActivity
        viewModel = mainActivity.quizViewModel
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}