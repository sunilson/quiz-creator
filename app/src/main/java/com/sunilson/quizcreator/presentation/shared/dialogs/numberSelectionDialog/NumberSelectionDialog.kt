package com.sunilson.quizcreator.presentation.shared.dialogs.numberSelectionDialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.databinding.NumberSelectionDialogBinding
import com.sunilson.quizcreator.presentation.shared.baseClasses.BaseDialogFragment
import com.sunilson.quizcreator.presentation.shared.dialogs.DialogListener
import com.sunilson.quizcreator.presentation.shared.dialogs.DialogWithResult
import org.koin.android.ext.android.inject

class NumberSelectionDialog : BaseDialogFragment(), DialogWithResult<Int> {

    val viewModel: NumberSelectionDialogViewmodel by inject()

    override var listener: DialogListener<Int>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        arguments?.run {
            viewModel.max = getInt("max")
            viewModel.currentValue = getInt("startValue")
            viewModel.title = getString("title", "")
        }

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