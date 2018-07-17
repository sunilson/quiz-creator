package com.sunilson.quizcreator.presentation.shared.Dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseDialogFragment
import kotlinx.android.synthetic.main.input_dialog_body.view.*

class SimpleInputDialog : BaseDialogFragment(), DialogWithResult<String> {

    override var listener: DialogListener<String>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        builder.setTitle(arguments?.getString("title"))
        val content = LayoutInflater.from(context).inflate(R.layout.input_dialog_body, null)
        builder.setView(content)

        builder.setPositiveButton(R.string.ok) { p0, p1 ->
            listener?.onResult(content.dialog_input.text.toString())
        }

        builder.setNegativeButton(R.string.cancel) { p0, p1 ->
        }

        return builder.create()
    }

    companion object {
        fun newInstance(title: String) : SimpleInputDialog{
            val bundle = Bundle()
            bundle.putString("title", title)
            val dialog = SimpleInputDialog()
            dialog.arguments = bundle
            return dialog
        }
    }
}