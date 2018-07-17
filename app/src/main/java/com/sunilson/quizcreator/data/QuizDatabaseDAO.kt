package com.sunilson.quizcreator.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.sunilson.quizcreator.data.models.Answer
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.Quiz
import io.reactivex.Flowable
import io.reactivex.Single

@Dao()
interface QuizDatabaseDAO {

    @Query("SELECT * FROM answer WHERE categoryId == :categoryId")
    fun getAnswersForCategory(categoryId: String): Single<List<Answer>>

    @Query("SELECT * FROM question WHERE categoryId IN (:categoryIds)")
    fun getQuestionsForCategories(categoryIds: Array<String>): Flowable<List<Question>>

    @Query("SELECT * FROM question")
    fun getAllQuestions() : Flowable<List<Question>>

    @Query("SELECT * FROM category")
    fun getAllCategories(): Flowable<List<Category>>

    @Query("SELECT * FROM category")
    fun getAllCategoriesOnce(): Single<List<Category>>

    @Query("SELECT * FROM quiz")
    fun getAllQuiz(): Flowable<List<Quiz>>

    @Insert
    fun addQuiz(question: Question)

    @Insert
    fun addQuestion(question: Question)

    @Insert
    fun addCategory(category: Category)

    @Update
    fun updateQuestion(question: Question)

    @Query("DELETE FROM question WHERE id == :id")
    fun removeQuestion(id: String)

    @Query("DELETE FROM answer WHERE id == :id")
    fun removeAnswer(id: String)

}