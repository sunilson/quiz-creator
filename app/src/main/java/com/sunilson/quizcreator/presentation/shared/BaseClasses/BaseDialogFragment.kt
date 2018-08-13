package com.sunilson.quizcreator.presentation.shared.BaseClasses

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunilson.quizcreator.R
import io.reactivex.disposables.CompositeDisposable

open class BaseDialogFragment : DialogFragment() {

    protected lateinit var builder: AlertDialog.Builder
    protected val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = AlertDialog.Builder(activity)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.setBackgroundDrawableResource(R.drawable.dialog_background)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (dialog.window != null) dialog.window.attributes.windowAnimations = R.style.dialogAnimation
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}