package com.sunilson.quizcreator.presentation.mainActivity.fragments.allQuestionsFragment

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.presentation.shared.EventBus
import com.sunilson.quizcreator.presentation.shared.EventChannel
import com.sunilson.quizcreator.presentation.shared.baseClasses.BaseViewModel
import io.reactivex.Completable

class AllQuestionsViewModel constructor(
    val application: Application,
    repository: IQuizRepository,
    eventBus: EventBus
) : BaseViewModel(repository) {

    init {
        loadCategories()
        loadSearchCategories()
        disposable.add(eventBus.subscribeToChannel(EventChannel.RELOAD_QUESTIONS).subscribe { loadQuestions() })
    }

    val questions: ObservableList<Question> = ObservableArrayList<Question>()
    val searchCategories: ObservableList<Category> = ObservableArrayList<Category>()
    var selectedSearchCategory: ObservableField<Category?> = ObservableField()
    var searchQuery: ObservableField<String?> = ObservableField()

    private fun loadSearchCategories() {
        disposable.add(repository.loadCategories().map {
            val result = it.toMutableList()
            result.add(0, Category("all", "All"))
            result.toList()
        }.subscribe {
            searchCategories.clear()
            searchCategories.addAll(it)
        })
    }

    fun selectSearchCategory(category: Category?) {
        if (category == null) {
            this.selectedSearchCategory.set(null)
            loadQuestions()
        } else {
            val newCategory = if (category?.id != "all") category
            else null
            if (newCategory?.id != selectedSearchCategory.get()?.id) {
                this.selectedSearchCategory.set(newCategory)
                loadQuestions()
            }
        }
    }

    fun search(query: String) {
        val newQuery = if (query.isNotEmpty()) query
        else null

        if (newQuery != searchQuery.get()) {
            this.searchQuery.set(newQuery)
            loadQuestions()
        }
    }

    fun loadQuestions() {
        loading.set(true)
        disposable.add(
            repository.loadQuestionsOnce(
                selectedSearchCategory.get()?.id,
                searchQuery.get()
            ).subscribe({
                if (categories.size > 0) {
                    it.forEach { question ->
                        question.categoryName =
                            categories.find { question.categoryId == it.id }?.name ?: ""
                    }
                }
                questions.clear()
                questions.addAll(it)
                loading.set(false)
            }, { loading.set(false) })
        )
    }

    fun deleteQuestion(question: Question): Completable {
        return repository.deleteQuestion(question.id)
    }

    fun updateQuestion(question: Question): Completable {
        return repository.updateQuestion(question)
    }
}