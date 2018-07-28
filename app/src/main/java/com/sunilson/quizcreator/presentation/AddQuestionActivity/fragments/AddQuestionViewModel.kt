package com.sunilson.quizcreator.presentation.AddQuestionActivity.fragments

import android.app.Application
import com.sunilson.quizcreator.Application.di.FragmentScope
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.QuestionType
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import io.reactivex.Completable
import javax.inject.Inject

@FragmentScope
class AddQuestionViewModel @Inject constructor(val application: Application, repository: IQuizRepository) : BaseViewModel(repository) {

    var creationQuestion: Question = Question()

    fun createQuestion(): Completable {
        errorMessage.set("")
        return repository.addQuestion(creationQuestion).doOnError { errorMessage.set(it.message) }
    }

    fun addCategory(name: String): Completable {
        if (name.isEmpty()) return Completable.error(IllegalArgumentException(application.getString(R.string.category_name_empty)))
        return repository.addCategory(Category(name = name))
    }

    override fun selectCategory(category: Category) {
        this.selectedCategory = category
        creationQuestion.categoryId = category.id
    }

    fun startQuestionCreation(questionType: QuestionType) {
        creationQuestion = Question(
                text = "Ist dies eine Frage?",
                type = questionType,
                answers = mutableListOf(
                        Answer(text = application.getString(R.string.answer_default_text), correctAnswer = true),
                        Answer(text = application.getString(R.string.answer_default_text)),
                        Answer(text = application.getString(R.string.answer_default_text)),
                        Answer(text = application.getString(R.string.answer_default_text))
                )
        )
    }
}