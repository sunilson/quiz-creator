package com.sunilson.quizcreator.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "quiz")
class Quiz(
        @PrimaryKey
        var id: String = UUID.randomUUID().toString(),
        var questions: MutableList<Question> = mutableListOf(),
        var correctAnswers: Int = 0,
        var duration: Long = 0,
        var timestamp: Long = Date().time,
        var finished: Boolean = false
)