package com.sunilson.quizcreator.presentation.shared.Dialogs.ImportExportDialog

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "application/json"
            startActivityForResult(intent, PICK_IMPORT_FILE)
        }

        content.export_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "application/json"
            intent.putExtra(Intent.EXTRA_TITLE, "quiz-creater-export.json")
            startActivityForResult(intent, EXPORT_REQUEST_CODE, null)
        }

        builder.setView(content)

        return builder.create()
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private fun exportData(uri: Uri) {
        disposable.add(repository.exportQuestionsAndCategories(uri).subscribe({
            context?.showToast(getString(R.string.export_successful))
            dismiss()
        }, {
            context?.showToast(getString(R.string.export_error))
        }))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            EXPORT_REQUEST_CODE -> data?.data?.let { exportData(it) }
            PICK_IMPORT_FILE -> {
                if (data?.data?.path != null) {
                    disposable.add(repository.importQuestionsAndCategories(data.data).subscribe({
                        context?.showToast(getString(R.string.import_success))
                        dismiss()
                        listener?.onResult(true)
                    }, {
                        context?.showToast(getString(R.string.import_failed))
                    }))
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
        const val EXPORT_REQUEST_CODE = 1

        fun newInstance(): ImportExportDialog {
            return ImportExportDialog()
        }
    }
}