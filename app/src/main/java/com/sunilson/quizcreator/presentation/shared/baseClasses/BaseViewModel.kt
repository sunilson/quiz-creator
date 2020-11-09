package com.sunilson.quizcreator.presentation.shared.baseClasses

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Category
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(val repository: IQuizRepository) {

    val categories: ObservableList<Category> = ObservableArrayList<Category>()
    var selectedCategory: ObservableField<Category?> = ObservableField()

    val errorMessage: ObservableField<String> = ObservableField("")
    val loading: ObservableBoolean = ObservableBoolean(true)
    val working: ObservableBoolean = ObservableBoolean(false)
    val disposable: CompositeDisposable = CompositeDisposable()

    open fun loadCategories() {
        disposable.add(repository.loadCategories().subscribe {
            categories.clear()
            categories.addAll(it)
        })
    }

    fun unselectCategory(value: Boolean) {
        if (!value) selectedCategory.set(null)
    }

    fun onDestroy() {
        disposable.dispose()
    }
}