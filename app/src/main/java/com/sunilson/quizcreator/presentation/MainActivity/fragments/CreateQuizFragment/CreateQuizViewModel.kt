package com.sunilson.quizcreator.presentation.MainActivity.fragments.CreateQuizFragment

import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import com.sunilson.quizcreator.Application.di.scopes.FragmentScope
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import javax.inject.Inject

@FragmentScope
class CreateQuizViewModel @Inject constructor(repository: IQuizRepository) : BaseViewModel(repository) {
    val singleOrMultiple: ObservableBoolean = ObservableBoolean(false)
    val shuffleAnswers: ObservableBoolean = ObservableBoolean(false)
    val maxQuestionAmount: ObservableInt = ObservableInt(10)

    override fun loadCategories() {
        disposable.add(repository.loadCategories().subscribe {
            val result = it.toMutableList()
            result.add(0, Category("all", "All"))
            categories.clear()
            categories.addAll(result)
        })
    }
}