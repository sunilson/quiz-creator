package com.sunilson.quizcreator.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "answer")
@ForeignKey(entity = Question::class, parentColumns = ["id"], childColumns = ["questionID"], onDelete = CASCADE)
data class Answer(
        @PrimaryKey
        var id: String = UUID.randomUUID().toString(),
        var text: String = "",
        var correctAnswer: Boolean = false,
        var chosen: Boolean = false
)