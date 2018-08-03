package com.sunilson.quizcreator.presentation.MainActivity.fragments.CategoriesFragment

import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import io.reactivex.Completable
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(repository: IQuizRepository) : BaseViewModel(repository) {

    init {
        loadCategories()
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