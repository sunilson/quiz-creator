package com.sunilson.quizcreator.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sunilson.quizcreator.data.models.*


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
    fun toString(statistics: Statistics): String {
        return gson.toJson(statistics, Statistics::class.java)
    }

    @TypeConverter
    fun toStatistics(string: String): Statistics {
        return gson.fromJson<Statistics>(string, Statistics::class.java)
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
    fun toString(questionType: QuestionType): String {
        return gson.toJson(questionType, QuestionType::class.java)
    }

    @TypeConverter
    fun toQuestionType(string: String): QuestionType {
        return gson.fromJson<QuestionType>(string, QuestionType::class.java)
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
    fun toCategoryRateMap(string: String): Map<String, Float> {
        val listType = object : TypeToken<Map<String, Float>>() {}.type
        return gson.fromJson(string, listType)
    }

    @TypeConverter
    fun rateMapToString(rates: Map<String, Float>): String {
        return gson.toJson(rates)
    }

    @TypeConverter
    fun toDateCategoryMap(string: String): Map<String, Long> {
        val listType = object : TypeToken<Map<String, Long>>() {}.type
        return gson.fromJson(string, listType)
    }

    @TypeConverter
    fun dateCategoryMapToString(rates: Map<String, Long>): String {
        return gson.toJson(rates)
    }

    @TypeConverter
    fun toIntList(string: String): List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(string, listType)
    }

    @TypeConverter
    fun intListToString(list: List<Int>): String {
        return gson.toJson(list)
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