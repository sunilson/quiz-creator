package com.sunilson.quizcreator.presentation.shared.BaseClasses

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
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