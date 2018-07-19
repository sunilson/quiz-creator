package com.sunilson.quizcreator.presentation.MainActivity.fragments

import android.content.Context
import android.support.v4.app.Fragment
import com.sunilson.quizcreator.presentation.MainActivity.MainActivity
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment() : Fragment() {

    protected val disposable: CompositeDisposable = CompositeDisposable()
    protected lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        mainActivity = activity as MainActivity
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}