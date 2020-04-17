package com.sunilson.quizcreator.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.Quiz
import com.sunilson.quizcreator.data.models.Statistics


@Database(
    entities = [(Answer::class), (Question::class), (Quiz::class), (Category::class), (Statistics::class)],
    version = 18
)
@TypeConverters(com.sunilson.quizcreator.data.TypeConverters::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDAO(): QuizDatabaseDAO
}