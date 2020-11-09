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
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class NumberSelectionDialog : BaseDialogFragment(), DialogWithResult<Int> {

    @Inject
    lateinit var viewModel: NumberSelectionDialogViewmodel

    override var listener: DialogListener<Int>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        viewModel.max = arguments!!.getInt("max")
        viewModel.currentValue = arguments?.getInt("startValue", 0) ?: 0
        viewModel.title = arguments?.getString("title").orEmpty()

        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = NumberSelectionDialogBinding.inflate(inflater, null, false)
        builder.setView(binding.root)
        binding.viewModel = viewModel

        builder.setPositiveButton(R.string.ok) { _, _ ->
            listener?.onResult(viewModel.currentValue)
        }

        builder.setNegativeButton(R.string.cancel) { _, _ ->
            listener?.onResult(null)
        }

        return builder.create()
    }

    override fun onAttach(conext: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context!!)
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