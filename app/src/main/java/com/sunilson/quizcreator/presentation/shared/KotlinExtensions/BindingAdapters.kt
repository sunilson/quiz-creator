package com.sunilson.quizcreator.presentation.shared.KotlinExtensions

import android.content.res.ColorStateList
import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ImageViewCompat
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.presentation.shared.BaseClasses.AdapterElement
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseRecyclerAdapter
import com.sunilson.quizcreator.presentation.shared.CategorySpinnerAdapter
import com.sunilson.quizcreator.presentation.views.AnswerView.AnswerView
import com.sunilson.quizcreator.presentation.views.EditTextWithVoiceInput.EditTextWithVoiceInput
import com.sunilson.quizcreator.presentation.views.QuizView.QuizView
import kotlinx.android.synthetic.main.voice_edittext.view.*

@BindingAdapter("categories")
fun Spinner.setEntries(categories: List<Category>?) {
    if (categories != null) {
        val adapter = this.adapter as CategorySpinnerAdapter
        adapter.setCategories(categories)
    }
}

@BindingAdapter("itemSelectedCallback")
fun Spinner.itemSelectedCallback(callback: ItemSelectedListener) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {}
        override fun onItemSelected(p0: AdapterView<*>, p1: View?, p2: Int, p3: Long) {
            callback.itemSelected(p0.selectedItem)
        }
    }
}

@BindingAdapter("entries")
fun RecyclerView.setEntries(entries: List<AdapterElement>?) {
    if (entries != null) {
        val adapter = this.adapter as BaseRecyclerAdapter<AdapterElement>
        adapter.addAll(entries)
    }
}


@BindingAdapter("editTextValueAttrChanged")
fun setTextValueListener(editTextWithVoiceInput: EditTextWithVoiceInput, listener: InverseBindingListener) {
    editTextWithVoiceInput.voice_edittext.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            listener.onChange()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })
}

@BindingAdapter("editTextValue")
fun setTextValue(editTextWithVoiceInput: EditTextWithVoiceInput, value: String?) {
    if (value != editTextWithVoiceInput.voice_edittext.text.toString()) editTextWithVoiceInput.voice_edittext.setText(value)
}

@InverseBindingAdapter(attribute = "editTextValue")
fun getTextValue(editTextWithVoiceInput: EditTextWithVoiceInput): String? {
    return editTextWithVoiceInput.voice_edittext.text.toString()
}

@BindingAdapter("showHideWithTransition")
fun View.showHideWithTransition(show: Boolean) {
    android.transition.TransitionManager.beginDelayedTransition(this.parent as ViewGroup, Fade())
    if (show) this.visibility = View.VISIBLE
    else this.visibility = View.INVISIBLE
}

@BindingAdapter("quiz")
fun QuizView.setQuiz(quiz: Quiz?) {
    quiz?.let {
        this.quiz = it
    }
}

/*

@BindingAdapter("answers", "answerClickedCallback", requireAll = true)
fun LinearLayout.setAnswers(answers: List<Answer>, callback: ItemSelectedListener) {
    answers.forEach { answer ->
        val textView = TextView(context)
        textView.text = answer.text
        textView.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        textView.setOnClickListener { callback.itemSelected(answer) }
        this.addView(textView)
    }
}
*/

@BindingAdapter("answers", "answerClickedCallback", requireAll = false)
fun LinearLayout.setAnswers(question: Question?, callback: ItemSelectedListener) {
    question?.answers?.forEachIndexed { index, answer ->
        val answerView = AnswerView(context, answer, index != question.answers.size - 1)
        answerView.setOnClickListener {
            for (i in 0 until childCount) {
                val child = getChildAt(i) as AnswerView
                child.showAnswerResult(child.answer!!.correctAnswer)
            }
            callback.itemSelected(answer)
        }
        this.addView(answerView)
    }
}

@BindingAdapter("correct")
fun ImageView.answerIcon(correct: Boolean?) {
    if (correct != null) {
        android.transition.TransitionManager.beginDelayedTransition(this.parent as ViewGroup, ChangeBounds())
        if (correct) {
            this.setImageDrawable(android.support.v4.content.ContextCompat.getDrawable(context, R.drawable.ic_check_circle_black_24dp))
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.correct)))
        } else {
            this.setImageDrawable(android.support.v4.content.ContextCompat.getDrawable(context, R.drawable.ic_cancel_black_24dp))
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.wrong)))
        }
        this.visibility = View.VISIBLE
    }
}

@BindingAdapter("deleteParent")
fun View.deleteParent(value: Boolean?) {
    value?.let {
        if (it) {
            this.setOnClickListener {
                android.transition.TransitionManager.beginDelayedTransition(this.rootView as ViewGroup, ChangeBounds())
                val superparent = this.parent.parent
                val parent = this.parent
                (superparent as ViewGroup).removeView(parent as ViewGroup)
            }
        }
    }

}

@BindingAdapter("initialEditTextAnswers")
fun LinearLayout.setInitialAnswers(answers: List<Answer>) {
    val parent = this.parent as View
    parent.viewTreeObserver.addOnGlobalLayoutListener {
        val vis = parent.visibility
        if (this.tag == null || this.tag as Int != vis) {
            this.tag = vis
            if (vis == View.VISIBLE) {
                if (this.childCount == 0) {
                    TransitionManager.beginDelayedTransition(this.rootView as ViewGroup, Fade())
                    answers.forEachIndexed { index, answer ->
                        val answerView = EditTextWithVoiceInput(context, index > 3, answer)
                        this.addView(answerView)
                    }
                }
            } else {
                this.removeAllViews()
            }
        }
    }
}


interface ItemSelectedListener {
    fun itemSelected(item: Any)
}