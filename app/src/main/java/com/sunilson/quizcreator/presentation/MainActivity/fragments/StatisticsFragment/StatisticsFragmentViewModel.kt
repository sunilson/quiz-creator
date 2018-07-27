package com.sunilson.quizcreator.presentation.MainActivity.fragments.StatisticsFragment

import android.databinding.ObservableField
import android.util.Log
import com.sunilson.quizcreator.Application.di.FragmentScope
import com.sunilson.quizcreator.data.IQuizRepository
import com.sunilson.quizcreator.data.models.Statistics
import com.sunilson.quizcreator.presentation.shared.BaseClasses.BaseViewModel
import javax.inject.Inject

@FragmentScope
class StatisticsFragmentViewModel @Inject constructor(repository: IQuizRepository) : BaseViewModel(repository) {

    val currentStatistics = ObservableField<Statistics>(Statistics())

    init {
        loadStatistics()
    }

    fun loadStatistics() {
        disposable.add(repository.getStatistics().subscribe({
            currentStatistics.set(it)
        }, {
            Log.d("Linus", "bla")
        }))
    }

    fun resetStatistics() {
        disposable.add(repository.resetStatistics().subscribe {
            loadStatistics()
        })
    }

}