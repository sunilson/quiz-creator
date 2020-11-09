package com.sunilson.quizcreator.presentation.mainActivity.fragments.createQuizFragment

import androidx.databinding.*
import com.sunilson.quizcreator.application.di.scopes.FragmentScope
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.presentation.shared.baseClasses.BaseViewModel
import com.sunilson.quizcreator.presentation.shared.LocalSettingsManager
import javax.inject.Inject

@FragmentScope
class CreateQuizViewModel @Inject constructor(repository: IQuizRepository, localSettingsManager: LocalSettingsManager) : BaseViewModel(repository) {
    val singleOrMultiple: ObservableBoolean = ObservableBoolean(false)
    val shuffleAnswers: ObservableBoolean = ObservableBoolean(false)
    val maxQuestionAmount: ObservableInt = ObservableInt(localSettingsManager.maxQuestionAmount)
    val quizzes: ObservableList<Quiz> = ObservableArrayList<Quiz>()

    init {
        loadQuizArchive()

        maxQuestionAmount.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                localSettingsManager.maxQuestionAmount = maxQuestionAmount.get()
            }
        })
    }

    override fun loadCategories() {
        disposable.add(repository.loadCategories().subscribe {
            val result = it.toMutableList()
            result.add(0, Category("all", "All"))
            categories.clear()
            categories.addAll(result)
        })
    }

    private fun loadQuizArchive() {
        disposable.add(repository.loadFinishedQuizzes().subscribe {
            quizzes.clear()
            quizzes.addAll(it)
        })
    }
}