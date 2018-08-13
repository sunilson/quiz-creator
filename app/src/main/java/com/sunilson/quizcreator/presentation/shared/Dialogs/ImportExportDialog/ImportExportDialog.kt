package com.sunilson.quizcreator.presentation.shared.Dialogs.ImportExportDialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseDialogFragment
import com.sunilson.quizcreator.presentation.shared.Dialogs.DialogListener
import com.sunilson.quizcreator.presentation.shared.Dialogs.DialogWithResult
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.hasPermission
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.showToast
import com.sunilson.quizcreator.presentation.shared.PICK_IMPORT_FILE
import com.sunilson.quizcreator.presentation.shared.REQUEST_EXTERNAL_STORAGE_INTENT
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.import_export_dialog.view.*
import javax.inject.Inject


class ImportExportDialog : BaseDialogFragment(), DialogWithResult<Boolean> {

    @Inject
    lateinit var repository: IQuizRepository

    override var listener: DialogListener<Boolean>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val content = inflater.inflate(R.layout.import_export_dialog, null, false)

        content.import_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            //intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, PICK_IMPORT_FILE)
        }

        content.export_button.setOnClickListener {
            disposable.add(repository.exportQuestionsAndCategories().subscribe({
                context?.showToast(getString(R.string.export_successful) + it)
                dismiss()
            }, {
                context?.showToast(getString(R.string.export_error))
            }))
        }

        builder.setView(content)

        return builder.create()
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        if (!context!!.hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE_INTENT)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PICK_IMPORT_FILE -> {
                if (data?.data?.path != null && data.data.path.endsWith(".json")) {
                    disposable.add(repository.importQuestionsAndCategories(data.data).subscribe({
                        context?.showToast(getString(R.string.import_success))
                        dismiss()
                        listener?.onResult(true)
                    }, {
                        context?.showToast(getString(R.string.import_failed))
                    }))
                } else {
                    context?.showToast(getString(R.string.invalid_file))
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE_INTENT -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // exportQuestions()
                } else {
                    dismiss()
                }
            }
        }
    }

    companion object {
        fun newInstance(): ImportExportDialog {
            return ImportExportDialog()
        }
    }
}