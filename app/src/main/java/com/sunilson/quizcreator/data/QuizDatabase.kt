package com.sunilson.quizcreator.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.Quiz


@Database(entities = [(Answer::class), (Question::class), (Quiz::class), (Category::class)], version = 6)
@TypeConverters(com.sunilson.quizcreator.data.TypeConverters::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDAO(): QuizDatabaseDAO
}