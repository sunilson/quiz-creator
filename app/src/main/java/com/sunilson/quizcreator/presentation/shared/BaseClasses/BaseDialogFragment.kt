package com.sunilson.quizcreator.presentation.shared.BaseClasses

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

open class BaseDialogFragment : DialogFragment() {

    protected lateinit var builder: AlertDialog.Builder

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = AlertDialog.Builder(activity)
        return super.onCreateDialog(savedInstanceState)
    }

}