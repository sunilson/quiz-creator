package com.sunilson.quizcreator.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.sunilson.quizcreator.BR
import com.sunilson.quizcreator.presentation.shared.ANSWERS_PER_QUIZ_QUESTION
import com.sunilson.quizcreator.presentation.shared.baseClasses.AdapterElement
import java.util.*

@Entity(tableName = "question")
@ForeignKey(
    entity = Category::class,
    parentColumns = ["id"],
    childColumns = ["categoryId"],
    onDelete = ForeignKey.NO_ACTION
)
class Question(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "buttonText") var _text: String = "",
    var categoryId: String = "",
    var answers: MutableList<Answer> = mutableListOf(),
    @ColumnInfo(name = "maxAnswers") var _maxAnswers: Int = ANSWERS_PER_QUIZ_QUESTION,
    @ColumnInfo(name = "onlySameCategory") var _onlySameCategory: Boolean = true,
    var type: QuestionType = QuestionType.SINGLE_CHOICE,
    var correctlyAnswered: Boolean = false,
    @Ignore var categoryName: String = ""
) : BaseObservable(), AdapterElement {

    @Ignore
    override val compareByString: String = id

    /*
    TODO Observable list so dass nicht übr alle answer views iteriert werden muss am schluss
    @Ignore
    val observableAnswers: ObservableField<List<Answer>> = object : ObservableArrayList<Answer>() {
        override fun get(index: Int): Answer {
            return answers[index]
        }

        override fun set(index: Int, element: Answer?): Answer {

        }
    }
    */

    var text: String
        @Ignore
        set(value) {
            _text = value
            notifyPropertyChanged(BR.text)
        }
        @Ignore
        @Bindable
        get() = _text

    var category: Category
        @Ignore
        set(value) {
            categoryId = value.id
            categoryName = value.name
            notifyPropertyChanged(BR.category)
        }
        @Ignore
        @Bindable
        get() = Category(categoryId, categoryName)

    var maxAnswers: Int
        @Ignore
        set(value) {
            _maxAnswers = value
            notifyPropertyChanged(BR.maxAnswers)
        }
        @Ignore
        @Bindable
        get() = _maxAnswers

    var onlySameCategory: Boolean
        @Ignore
        set(value) {
            _onlySameCategory = value
            notifyPropertyChanged(BR.onlySameCategory)
        }
        @Ignore
        @Bindable
        get() = _onlySameCategory
}

enum class QuestionType {
    SINGLE_CHOICE, MULTIPLE_CHOICE
}