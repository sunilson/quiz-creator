package com.sunilson.quizcreator.presentation.mainActivity.fragments.categoriesFragment

import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.presentation.shared.EventBus
import com.sunilson.quizcreator.presentation.shared.EventChannel
import com.sunilson.quizcreator.presentation.shared.baseClasses.BaseViewModel
import io.reactivex.Completable


class CategoriesViewModel (repository: IQuizRepository, eventBus: EventBus) :
    BaseViewModel(repository) {

    init {
        loadCategories()
        disposable.add(eventBus.subscribeToChannel(EventChannel.RELOAD_CATEGORIES).subscribe { loadCategories() })
    }

    override fun loadCategories() {
        loading.set(true)
        disposable.add(repository.loadCategories().subscribe {
            loading.set(false)
            categories.clear()
            categories.addAll(it)
        })
    }

    fun deleteCategory(category: Category): Completable {
        return repository.deleteCategory(category.id)
    }

    fun addCategory(name: String): Completable {
        return repository.addCategory(Category(name = name))
    }

    fun updateCategory(category: Category): Completable {
        return repository.updateCategory(category)
    }
}