package com.sunilson.quizcreator.presentation.shared.Dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseDialogFragment
import kotlinx.android.synthetic.main.input_dialog_body.view.*

class SimpleInputDialog : BaseDialogFragment(), DialogWithResult<String> {

    override var listener: DialogListener<String>? = null

    private val imm : InputMethodManager by lazy {
        context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        builder.setTitle(arguments?.getString("title"))
        val content = LayoutInflater.from(context).inflate(R.layout.input_dialog_body, null)
        content.dialog_input.setText(arguments?.getString("value"))
        builder.setView(content)

        builder.setPositiveButton(R.string.ok) { p0, p1 ->
            listener?.onResult(content.dialog_input.text.toString())
        }

        builder.setNegativeButton(R.string.cancel) { p0, p1 ->
        }

        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        super.onDismiss(dialog)
    }

    companion object {
        fun newInstance(title: String, value: String = ""): SimpleInputDialog {
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("value", value)
            val dialog = SimpleInputDialog()
            dialog.arguments = bundle
            return dialog
        }
    }
}