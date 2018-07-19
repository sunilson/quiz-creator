package com.sunilson.quizcreator.presentation.AddQuestionActivity

import android.app.Application
import com.sunilson.quizcreator.Application.di.ActivityScope
import com.sunilson.quizcreator.R
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import io.reactivex.Completable
import javax.inject.Inject

@ActivityScope
class AddQuestionViewModel @Inject constructor(val application: Application, repository: IQuizRepository) : BaseViewModel(repository){

    var creationQuestion: Question = Question()

    fun createQuestion(): Completable {
        errorMessage.set("")
        return repository.addQuestion(creationQuestion).doOnError { errorMessage.set(it.message) }
    }

    fun addCategory(name: String): Completable {
        if (name.isEmpty()) return Completable.error(IllegalArgumentException(application.getString(R.string.category_name_empty)))
        return repository.addCategory(Category(name = name))
    }

    fun startQuestionCreation() {
        creationQuestion = Question()
    }
}