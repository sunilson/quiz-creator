package com.sunilson.quizcreator.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sunilson.quizcreator.presentation.shared.baseClasses.AdapterElement
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
) : AdapterElement {
        override val compareByString: String
                get() = id
}