package com.sunilson.quizcreator.presentation.shared.Dialogs.NumberSelectionDialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.databinding.NumberSelectionDialogBinding
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseDialogFragment
import com.sunilson.quizcreator.presentation.shared.Dialogs.DialogListener
import com.sunilson.quizcreator.presentation.shared.Dialogs.DialogWithResult
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class NumberSelectionDialog : BaseDialogFragment(), DialogWithResult<Int> {

    @Inject
    lateinit var viewModel: NumberSelectionDialogViewmodel

    override var listener: DialogListener<Int>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        viewModel.max = arguments!!.getInt("max")
        viewModel.currentValue = arguments!!.getInt("startValue")
        viewModel.title = arguments!!.getString("title")

        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = NumberSelectionDialogBinding.inflate(inflater, null, false)
        builder.setView(binding.root)
        binding.viewModel = viewModel

        builder.setPositiveButton(R.string.ok) { p0, p1 ->
            listener?.onResult(viewModel.currentValue)
        }

        builder.setNegativeButton(R.string.cancel) { p0, p1 ->
            listener?.onResult(null)
        }

        return builder.create()
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    companion object {
        fun newInstance(title: String, startValue: Int = 0, max: Int = 20): NumberSelectionDialog {
            val dialog = NumberSelectionDialog()
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putInt("startValue", startValue)
            bundle.putInt("max", max)
            dialog.arguments = bundle
            return dialog
        }
    }
}