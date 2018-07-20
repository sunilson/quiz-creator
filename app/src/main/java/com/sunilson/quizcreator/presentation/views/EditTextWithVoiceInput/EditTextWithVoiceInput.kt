package com.sunilson.quizcreator.presentation.views.EditTextWithVoiceInput

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.databinding.VoiceEdittextBinding
import com.sunilson.quizcreator.presentation.MainActivity.SpeechRecognizingActivity
import com.sunilson.quizcreator.presentation.shared.KotlinExtensions.convertToPx
import kotlinx.android.synthetic.main.voice_edittext.view.*


class EditTextWithVoiceInput : ConstraintLayout {

    val viewModel: EditTextWithVoiceInputViewModel
    val binding: VoiceEdittextBinding

    val text: String
        get() = voice_edittext.text.toString()

    var answer: Answer? = null
        set(value) {
            field = value
            viewModel.answer = field
        }

    var optional: Boolean = false
        set(value) {
            field = value
            viewModel.optional = field
        }

    constructor(context: Context, optional: Boolean, answer: Answer? = null) : super(context) {
        this.optional = optional
        this.answer = answer
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        val a = context.theme.obtainStyledAttributes(attributeSet, R.styleable.EditTextWithVoiceInput, 0, 0)
        optional = a.getBoolean(R.styleable.EditTextWithVoiceInput_optional, false)
        a.recycle()
    }

    init {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = VoiceEdittextBinding.inflate(inflater, this, true)

        if (optional) delete_button.visibility = View.VISIBLE

        background = ContextCompat.getDrawable(context, R.drawable.stacked_card_view_background)
        setPadding(20.convertToPx(context), 15.convertToPx(context), 20.convertToPx(context), 15.convertToPx(context))

        val layoutParams = LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.height = resources.getDimensionPixelSize(R.dimen.voice_edittext_height)
        layoutParams.setMargins(0, resources.getDimensionPixelSize(R.dimen.voice_edittext_margin), 0, resources.getDimensionPixelSize(R.dimen.voice_edittext_margin))
        this.layoutParams = layoutParams

        microphone_button.setOnTouchListener { p0, p1 ->
            when (p1.action) {
                MotionEvent.ACTION_DOWN -> {
                    (context as SpeechRecognizingActivity).startListening {
                        voice_edittext.setText(it)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    (context as SpeechRecognizingActivity).stopListening()
                }
            }
            false
        }

        viewModel = EditTextWithVoiceInputViewModel(answer, optional)
        binding.viewModel = viewModel
    }
}