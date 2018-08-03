package com.sunilson.quizcreator.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.sunilson.quizcreator.data.models.*


@Database(entities = [(Answer::class), (Question::class), (Quiz::class), (Category::class), (Statistics::class)], version = 12)
@TypeConverters(com.sunilson.quizcreator.data.TypeConverters::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDAO(): QuizDatabaseDAO
}