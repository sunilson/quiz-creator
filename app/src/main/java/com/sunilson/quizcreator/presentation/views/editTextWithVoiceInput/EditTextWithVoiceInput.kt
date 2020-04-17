package com.sunilson.quizcreator.presentation.views.editTextWithVoiceInput

import android.animation.Animator
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.databinding.VoiceEdittextBinding
import com.sunilson.quizcreator.presentation.shared.baseClasses.SpeechRecognizingActivity
import com.sunilson.quizcreator.presentation.shared.kotlinExtensions.showToast
import kotlinx.android.synthetic.main.voice_edittext.view.*


class EditTextWithVoiceInput : ConstraintLayout {

    private lateinit var viewModel: EditTextWithVoiceInputViewModel
    private lateinit var binding: VoiceEdittextBinding

    var text: String
        get() = viewModel.text
        set(value) {
            viewModel.text = value
        }

    val answer: Answer?
        get() = viewModel.answer

    val optional: Boolean
        get() = viewModel.optional

    val correctToggable: Boolean
        get() = viewModel.correctToggable

    val voiceEnabled: Boolean
        get() = viewModel.voiceEnabled

    val hint: String
        get() = viewModel.hint

    private var recording: Boolean = false
    private var ending: Boolean = false
    private var doHandler: Handler = Handler()
    private var runnable: Runnable = Runnable {
        context?.showToast("bla")
    }

    constructor(
        context: Context,
        optional: Boolean,
        correctToggable: Boolean = false,
        answer: Answer? = null,
        voiceEnabled: Boolean = true,
        hint: String = ""
    ) : super(context) {
        initBinding(optional, correctToggable, answer, voiceEnabled, hint)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.EditTextWithVoiceInput,
            0,
            0
        )
        initBinding(
            a.getBoolean(R.styleable.EditTextWithVoiceInput_optional, false),
            a.getBoolean(R.styleable.EditTextWithVoiceInput_correctToggable, false),
            voiceEnabled = a.getBoolean(R.styleable.EditTextWithVoiceInput_voiceEnabled, true),
            hint = a.getString(R.styleable.EditTextWithVoiceInput_hint) ?: ""
        )
        a.recycle()
    }


    @Suppress("LongMethod")
    private fun initBinding(
        optional: Boolean,
        correctToggable: Boolean = false,
        answer: Answer? = null,
        voiceEnabled: Boolean = true,
        hint: String = ""
    ) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = VoiceEdittextBinding.inflate(inflater, this, true)

        if (optional) delete_button.visibility = View.VISIBLE
        background = ContextCompat.getDrawable(context, R.drawable.single_card_view_background)

        val layoutParams = LinearLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.height = resources.getDimensionPixelSize(R.dimen.voice_edittext_height)
        layoutParams.setMargins(
            0,
            resources.getDimensionPixelSize(R.dimen.voice_edittext_margin),
            0,
            resources.getDimensionPixelSize(R.dimen.voice_edittext_margin)
        )
        this.layoutParams = layoutParams

        microphone_button.setOnLongClickListener {
            if (!recording) {
                val anim = ViewAnimationUtils.createCircularReveal(
                    voice_active_overlay,
                    voice_active_overlay.right,
                    voice_active_overlay.bottom,
                    0f,
                    Math.max(voice_active_overlay.width, voice_active_overlay.height).toFloat()
                )
                voice_active_overlay.visibility = View.VISIBLE
                anim.start()
                recording = true
                microphone_button.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_close_white_24dp
                    )
                )
                (context as SpeechRecognizingActivity).startListening({
                    voice_edittext.setText(it)
                }, {
                    endVoiceInput()
                }, {
                    endVoiceInput()
                })
            }
            true
        }

        microphone_button.setOnClickListener {
            if (recording) (context as SpeechRecognizingActivity).stopListening()
            else context.showToast("Press button longer to record audio!", Toast.LENGTH_SHORT)
        }

        microphone_button.setOnTouchListener { p0, p1 ->
            return@setOnTouchListener when (p1.action) {
                MotionEvent.ACTION_POINTER_UP,
                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_OUTSIDE,
                MotionEvent.ACTION_UP -> {
                    if (recording) {
                        (context as SpeechRecognizingActivity).stopListening()
                        true
                    } else {
                        false
                    }
                }
                else -> false
            }
        }

        viewModel =
            EditTextWithVoiceInputViewModel(answer, optional, correctToggable, voiceEnabled, hint)
        binding.viewModel = viewModel
    }

    private fun endVoiceInput() {
        recording = false
        if (!ending) {
            ending = true
            val anim = ViewAnimationUtils.createCircularReveal(
                voice_active_overlay,
                voice_active_overlay.right,
                voice_active_overlay.bottom,
                Math.max(voice_active_overlay.width, voice_active_overlay.height).toFloat(),
                0f
            )
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationEnd(p0: Animator?) {
                    voice_active_overlay.visibility = View.INVISIBLE
                    microphone_button.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_mic_black_24dp
                        )
                    )
                    ending = false
                }

                override fun onAnimationCancel(p0: Animator?) {
                    voice_active_overlay.visibility = View.INVISIBLE
                    microphone_button.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_mic_black_24dp
                        )
                    )
                    ending = false
                }

                override fun onAnimationStart(p0: Animator?) {}
            })
            anim.start()
        }
    }
}