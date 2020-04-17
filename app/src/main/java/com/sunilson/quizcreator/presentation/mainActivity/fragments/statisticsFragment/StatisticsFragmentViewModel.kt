package com.sunilson.quizcreator.presentation.mainActivity.fragments.statisticsFragment

import android.util.Log
import androidx.databinding.ObservableField

import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Statistics
import com.sunilson.quizcreator.presentation.shared.baseClasses.BaseViewModel



class StatisticsFragmentViewModel (repository: IQuizRepository) :
    BaseViewModel(repository) {

    val currentStatistics = ObservableField<Statistics>(Statistics())
    val averageDuration = ObservableField<String>("0 Seconds")

    init {
        loadCategories()
        loadStatistics()
    }

    fun loadStatistics() {
        disposable.add(repository.getStatistics().subscribe({
            currentStatistics.set(it)
            averageDuration.set((it.averageDuration / 1000).toString() + " Seconds")
        }, {
            Log.d("Linus", it.message)
            Log.d("Linus", "bla")
        }))
    }

    fun resetStatistics() {
        disposable.add(repository.resetStatistics().subscribe {
            loadStatistics()
        })
    }

}