package com.sunilson.quizcreator.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.presentation.SingleActivity.SpeechRecognizingActivity
import kotlinx.android.synthetic.main.voice_edittext.view.*


class EditTextWithVoiceInput : LinearLayout{

    val text: String
        get() = voice_edittext.text.toString()

    var optional: Boolean = false
        set(value) {
            field = value
            if(value) delete_button.visibility = View.VISIBLE
            else delete_button.visibility = View.GONE
        }


    constructor(context: Context, optional: Boolean) : super(context) {
        this.optional = optional
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        val a = context.theme.obtainStyledAttributes(attributeSet, R.styleable.EditTextWithVoiceInput, 0, 0)
        optional = a.getBoolean(R.styleable.EditTextWithVoiceInput_optional, false)
        a.recycle()
    }

    init {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.voice_edittext, this, true)

        if (optional) delete_button.visibility = View.VISIBLE

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
    }
}