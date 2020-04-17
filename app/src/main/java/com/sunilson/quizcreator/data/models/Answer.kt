package com.sunilson.quizcreator.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "answer")
@ForeignKey(
    entity = Question::class,
    parentColumns = ["id"],
    childColumns = ["questionID"],
    onDelete = CASCADE
)
data class Answer(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var text: String = "",
    var correctAnswer: Boolean = false,
    var chosen: Boolean = false
)