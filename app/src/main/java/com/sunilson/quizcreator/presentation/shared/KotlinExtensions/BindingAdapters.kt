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
import android.view.ViewTreeObserver
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.models.*
import com.sunilson.quizcreator.presentation.shared.BaseClasses.AdapterElement
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseRecyclerAdapter
import com.sunilson.quizcreator.presentation.shared.CategorySpinnerAdapter
import com.sunilson.quizcreator.presentation.shared.OPTIONAL_THRESHOLD
import com.sunilson.quizcreator.presentation.views.AnswerView.AnswerView
import com.sunilson.quizcreator.presentation.views.EditTextWithVoiceInput.EditTextWithVoiceInput
import com.sunilson.quizcreator.presentation.views.QuizView.QuizView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.voice_edittext.view.*
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import java.util.concurrent.TimeUnit

@BindingAdapter("selectedCategory", "categories", requireAll = false)
fun Spinner.setCategory(category: Category?, categories: List<Category>?) {
    if (categories != null) {
        val adapter = this.adapter as CategorySpinnerAdapter
        adapter.setCategories(categories)
    }

    if (category != null && (this.selectedItem as Category?)?.id != category.id) {
        this.setSelection((this.adapter as CategorySpinnerAdapter).data.indexOfFirst { it.id == category.id })
    }
}

@BindingAdapter("selectedCategoryAttrChanged")
fun Spinner.setCategoryListener(listener: InverseBindingListener) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
            listener.onChange()
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            listener.onChange()
        }
    }
}

@InverseBindingAdapter(attribute = "selectedCategory")
fun Spinner.getCategory(): Category {
    return this.selectedItem as Category
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
    if (value != null && value != editTextWithVoiceInput.voice_edittext.text.toString() && value.isNotEmpty()) {
        editTextWithVoiceInput.text = value
    }
}

@InverseBindingAdapter(attribute = "editTextValue")
fun getTextValue(editTextWithVoiceInput: EditTextWithVoiceInput): String? {
    return editTextWithVoiceInput.text
}

@BindingAdapter("showHideWithTransition")
fun View.showHideWithTransition(show: Boolean?) {
    show?.let {
        val start = if (it) 0f else 1f
        val end = if (it) 1f else 0f
        val animation = AlphaAnimation(start, end)
        animation.duration = 1000
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                if (it) this@showHideWithTransition.visibility = View.VISIBLE
                else this@showHideWithTransition.visibility = View.INVISIBLE
            }

            override fun onAnimationStart(p0: Animation?) {}
        })
        this.startAnimation(animation)
    }
}

@BindingAdapter("quiz")
fun QuizView.setQuiz(quiz: Quiz?) {
    quiz?.let {
        android.os.Handler().postDelayed({
            this.quiz = it
        }, 1000)
    }
}

@BindingAdapter("answers", "multiplePossible", "answerClickedCallback", requireAll = true)
fun LinearLayout.setAnswers(question: Question?, multiplePossible: Boolean, callback: ItemSelectedListener) {
    question?.answers?.forEachIndexed { index, answer ->
        val answerView = AnswerView(context, answer, index != question.answers.size - 1)
        answerView.setOnClickListener {
            if (!multiplePossible) {
                for (i in 0 until childCount) {
                    val child = getChildAt(i) as AnswerView
                    child.showAnswerResult()
                }
            } else {
                answerView.mark()
            }
            callback.itemSelected(answer)
        }
        this.addView(answerView)
    }
}

@BindingAdapter("correct", "marked", requireAll = false)
fun ImageView.answerIcon(correct: Boolean?, marked: Boolean = false) {
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
    } else {
        android.transition.TransitionManager.beginDelayedTransition(this.parent as ViewGroup, ChangeBounds())
        if (marked) {
            this.setImageDrawable(android.support.v4.content.ContextCompat.getDrawable(context, R.drawable.ic_check_circle_black_24dp))
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.unselected)))
        } else {
            this.setImageDrawable(null)
        }
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

@BindingAdapter("childChange")
fun View.onChildChange(listener: ItemSelectedListener) {
    (this as ViewGroup).setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
        override fun onChildViewRemoved(p0: View?, p1: View?) {
            listener.itemSelected(this@onChildChange.childCount)
        }

        override fun onChildViewAdded(p0: View?, p1: View?) {
            listener.itemSelected(this@onChildChange.childCount)
        }
    })
}

@BindingAdapter("initialEditTextAnswers", "questionType", requireAll = false)
fun LinearLayout.setInitialAnswers(answers: List<Answer>?, type: QuestionType?) {
    if (answers == null || answers.isEmpty()) return
    val parent = this.parent as View
    parent.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val vis = parent.visibility
            if (this@setInitialAnswers.tag == null || this@setInitialAnswers.tag as Int != vis) {
                this@setInitialAnswers.tag = vis
                if (vis == View.VISIBLE) {
                    if (this@setInitialAnswers.childCount == 0) {
                        TransitionManager.beginDelayedTransition(this@setInitialAnswers.rootView as ViewGroup, Fade())
                        answers.forEachIndexed { index, answer ->
                            val answerView = EditTextWithVoiceInput(context, index > OPTIONAL_THRESHOLD, (type != null && type == QuestionType.MULTIPLE_CHOICE), answer = answer)
                            this@setInitialAnswers.addView(answerView)
                        }
                    }
                } else {
                    if (this@setInitialAnswers.childCount != 0) {
                        this@setInitialAnswers.removeAllViews()
                        parent.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            }
        }
    })
}


/*
@BindingAdapter("correctAnswerAttrChanged")
fun setCorrectAnswerListener(view: ImageView, listener: InverseBindingListener) {
    view.setOnClickListener {
        when {
            view.tag == true -> view.tag = false
            view.tag == false -> view.tag = true
            else -> view.tag = false
        }
        listener.onChange()
    }
}
*/

@BindingAdapter("correctAnswer", "correctAnswerAttrChanged", "correctAnswerToggable", requireAll = false)
fun setCorrectAnswer(view: ImageView, answer: Answer?, listener: InverseBindingListener, toggable: Boolean) {
    if (answer != null) {
        view.tag = answer
        if (toggable) {
            if (answer.correctAnswer) {
                ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.correct)))
            } else {
                ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.unselected)))
            }

            view.setOnClickListener {
                answer.correctAnswer = !answer.correctAnswer
                view.tag = answer
                if (answer.correctAnswer) {
                    ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.correct)))
                } else {
                    ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.unselected)))
                }
                listener.onChange()
            }
        } else {
            if (answer.correctAnswer) {
                ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.correct)))
            } else {
                ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.disabled)))
            }
        }
    } else {
        view.visibility = View.GONE
        val edittext = (view.parent as ViewGroup).findViewById<EditText>(R.id.voice_edittext)
        edittext.setPadding(15.convertToPx(view.context), edittext.paddingTop, edittext.paddingRight, edittext.paddingBottom)
    }
}

@InverseBindingAdapter(attribute = "correctAnswer")
fun getCorrectAnswer(view: ImageView): Answer? {
    return view.tag as Answer
}

@BindingAdapter("questionIcon")
fun ImageView.setQuestionIcon(type: QuestionType) {
    when (type) {
        QuestionType.SINGLE_CHOICE -> {
            this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_black_24dp))
        }
        QuestionType.MULTIPLE_CHOICE -> {
            this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_done_all_black_24dp))
        }
    }
}

@BindingAdapter("statsTest")
fun PieChart.statsTest(statistics: Statistics) {
    val dataSet = PieDataSet(
            listOf(
                    PieEntry(statistics.finishedQuizAmount.toFloat(), "Finished Quizes"),
                    PieEntry(statistics.unfinishedQuizAmount.toFloat(), "Unfinished Quizes")),
            "Label")

    dataSet.setColors(ContextCompat.getColor(context, R.color.colorPrimary), ContextCompat.getColor(context, R.color.colorAccent))
    dataSet.valueTextSize = 24f
    dataSet.valueTextColor = ContextCompat.getColor(context, R.color.white)
    this.data = PieData(dataSet)
    this.legend.isEnabled = false
    this.description.isEnabled = false
    this.setDrawEntryLabels(false)
    this.invalidate()
}

@BindingAdapter("sevenDayQuizAmount", "sevenDayGoodQuizAmount")
fun BarChart.sevenDayQuizAmount(days: List<Int>, goodDays: List<Int>) {
    if (days.isEmpty()) return
    val entries = days.reversed().mapIndexed { index, value -> BarEntry(index.toFloat(), value.toFloat()) }
    val goodEntries = goodDays.reversed().mapIndexed { index, value -> BarEntry(index.toFloat(), value.toFloat()) }


    val dataSet = BarDataSet(entries, context.getString(R.string.finished))
    dataSet.color = ContextCompat.getColor(context, R.color.unselected)
    val dataSetGood = BarDataSet(goodEntries, context.getString(R.string.finished_good))
    dataSetGood.color = ContextCompat.getColor(context, R.color.correct)

    val barData = BarData(listOf(dataSet, dataSetGood))
    barData.barWidth = 0.42f
    barData.setDrawValues(false)
    this.data = barData
    this.data.isHighlightEnabled = false
    this.data.groupBars(0f, 0.06f, 0.02f)

    val dataLabels = days.reversed().mapIndexed { index, value ->
        val date = DateTime.now().minusDays(index)
        date.dayOfWeek
    }.reversed()

    this.description.isEnabled = false
    this.setPinchZoom(false)
    this.setScaleEnabled(false)
    this.setDrawBorders(false)
    this.setFitBars(true)
    this.axisLeft.granularity = 1f
    this.axisRight.isEnabled = false
    this.xAxis.setDrawGridLines(false)
    this.xAxis.position = XAxis.XAxisPosition.BOTTOM
    this.setExtraOffsets(0f, 0f, 0f, 0f)
    this.xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
        when (dataLabels[value.toInt()]) {
            1 -> context.getString(R.string.monday)
            2 -> context.getString(R.string.tuesday)
            3 -> context.getString(R.string.wednesday)
            4 -> context.getString(R.string.thursday)
            5 -> context.getString(R.string.friday)
            6 -> context.getString(R.string.saturday)
            else -> context.getString(R.string.sunday)
        }.take(3)
    }

    this.invalidate()
}

@BindingAdapter("categorySuccessRates", "categoryDates", "categories", requireAll = true)
fun LinearLayout.categorySuccessRates(rates: Map<String, Float>, dates: Map<String, Long>, categories: List<Category>) {
    if (rates.size <= categories.size) {
        this.removeAllViews()
        rates.forEach { s, fl ->
            val view = com.sunilson.quizcreator.presentation.views.CustomProgressBar.CustomProgressBar(
                    context,
                    categories.find { it.id == s }!!.name,
                    fl,
                    "Last answered on ${DateTimeFormat.shortDate().print(LocalDate(dates[s]))}")
            view.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
                this.topMargin = 15.convertToPx(context)
            }
            this.addView(view)
        }
    }
}

@BindingAdapter("changeListener")
fun EditTextWithVoiceInput.changeListener(callback: ItemSelectedListener) {
    RxTextView
            .textChanges(this.findViewById(R.id.voice_edittext))
            .filter { it.isNotEmpty() }
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { callback.itemSelected(it.toString()) }
}

/*
@BindingAdapter("correctAnswerToggable", "answerForCorrectIcon", requireAll = false)
fun ImageView.correctAnswerIcon(correctAnswerIconToggable: Boolean?, answer: Answer?) {
    if (answer != null && correctAnswerIconToggable != null) {
        if (correctAnswerIconToggable) {

        } else {

        }
    }
}
*/

interface ItemSelectedListener {
    fun itemSelected(item: Any)
}