package com.sunilson.quizcreator.presentation.MainActivity.fragments.CategoriesFragment

import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import com.sunilson.quizcreator.presentation.shared.EventBus
import com.sunilson.quizcreator.presentation.shared.EventChannel
import io.reactivex.Completable
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(repository: IQuizRepository, eventBus: EventBus) : BaseViewModel(repository) {

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