package com.sunilson.quizcreator.data

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.Quiz


class TypeConverters {

    val gson = Gson()


    @TypeConverter
    fun toString(answer: Answer): String {
        return gson.toJson(answer, Answer::class.java)
    }

    @TypeConverter
    fun toAnswer(string: String): Answer {
        return gson.fromJson<Answer>(string, Answer::class.java)
    }

    @TypeConverter
    fun toString(question: Question): String {
        return gson.toJson(question, Question::class.java)
    }

    @TypeConverter
    fun toQuestion(string: String): Question {
        return gson.fromJson<Question>(string, Question::class.java)
    }

    @TypeConverter
    fun toString(quiz: Quiz): String {
        return gson.toJson(quiz, Quiz::class.java)
    }

    @TypeConverter
    fun toQuiz(string: String): Quiz {
        return gson.fromJson<Quiz>(string, Quiz::class.java)
    }

    @TypeConverter
    fun toAnswerList(string: String): List<Answer> {
        val listType = object : TypeToken<List<Answer>>() {}.type
        return gson.fromJson(string, listType)
    }

    @TypeConverter
    fun answersToString(answers: List<Answer>): String {
        return gson.toJson(answers)
    }

    @TypeConverter
    fun toQuestionList(string: String): List<Question> {
        val listType = object : TypeToken<List<Question>>() {}.type
        return gson.fromJson(string, listType)
    }

    @TypeConverter
    fun questionsToString(questions: List<Question>): String {
        return gson.toJson(questions)
    }
}