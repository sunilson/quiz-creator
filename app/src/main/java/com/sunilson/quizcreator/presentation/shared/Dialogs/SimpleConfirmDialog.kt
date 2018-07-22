package com.sunilson.quizcreator.presentation.shared.Dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseDialogFragment
import kotlinx.android.synthetic.main.confirm_dialog_body.view.*

class SimpleConfirmDialog : BaseDialogFragment(), DialogWithResult<Boolean> {

    override var listener: DialogListener<Boolean>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        builder.setTitle(arguments?.getString("title"))
        val content = LayoutInflater.from(context).inflate(R.layout.confirm_dialog_body, null)
        content.confirm_dialog_text.text = arguments?.getString("question")
        builder.setView(content)

        builder.setPositiveButton(R.string.ok) { p0, p1 ->
            listener?.onResult(true)
        }

        builder.setNegativeButton(R.string.cancel) { p0, p1 ->
            listener?.onResult(false)
        }

        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        listener?.onResult(false)
    }

    companion object {
        fun newInstance(title: String, question: String) : SimpleConfirmDialog{
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("question", question)
            val dialog = SimpleConfirmDialog()
            dialog.arguments = bundle
            return dialog
        }
    }
}