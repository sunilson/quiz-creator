package com.sunilson.quizcreator.presentation.shared.KotlinExtensions

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.transition.Fade
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.presentation.shared.BaseClasses.AdapterElement
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseRecyclerAdapter
import com.sunilson.quizcreator.presentation.shared.CategorySpinnerAdapter
import com.sunilson.quizcreator.presentation.views.EditTextWithVoiceInput
import com.sunilson.quizcreator.presentation.views.QuizView.AnswerView
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
    question?.answers?.forEach { answer ->
        val answerView = AnswerView(context, answer)
        answerView.setOnClickListener {
                for (i in 0 until childCount) {
                    val child = getChildAt(i) as AnswerView
                    child.showAnswerResult(child.answer?.id == question.correctAnswerId)
                }
            callback.itemSelected(answer)
        }
        this.addView(answerView)
    }
}

interface ItemSelectedListener {
    fun itemSelected(item: Any)
}