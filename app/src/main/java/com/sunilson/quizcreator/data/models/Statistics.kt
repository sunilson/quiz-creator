package com.sunilson.quizcreator.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "statistics")
class Statistics(
        @PrimaryKey var id: String = "statistics",
        var finishedQuizAmount: Int = 0,
        var unfinishedQuizAmount: Int = 0,
        var finishedQuizAmountSevenDays: List<Int> = mutableListOf(),
        var finishedGoodQuizAmountSevenDays: List<Int> = mutableListOf(),
        var averageCorrectRate: Float = 0f,
        var averageCorrectRatePerCategory: Map<String, Float> = mapOf(),
        var lastAbsolvedDatePerCategory: Map<String, Long> = mapOf(),
        var averageDuration: Long = 0,
        var singleChoiceCorrectRate: Int = 0,
        var multipleChoiceCorrectRate: Int = 0
)