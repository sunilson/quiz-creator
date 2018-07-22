package com.sunilson.quizcreator.data

import android.arch.persistence.room.*
import com.sunilson.quizcreator.data.models.Category
import com.sunilson.quizcreator.data.models.Question
import com.sunilson.quizcreator.data.models.Quiz
import io.reactivex.Flowable
import io.reactivex.Single

@Dao()
interface QuizDatabaseDAO {

    @Query("SELECT * FROM question WHERE categoryId IN (:categoryIds)")
    fun getQuestionsForCategories(categoryIds: Array<String>): Flowable<List<Question>>

    @Query("SELECT * FROM question WHERE categoryId IN (:categoryIds)")
    fun getQuestionsForCategoriesOnce(categoryIds: Array<String>): Single<List<Question>>

    @Query("SELECT * FROM question")
    fun getAllQuestions(): Flowable<List<Question>>

    @Query("SELECT * FROM question")
    fun getAllQuestionsOnce(): Single<List<Question>>

    @Query("SELECT * FROM category")
    fun getAllCategories(): Flowable<List<Category>>

    @Query("SELECT * FROM category")
    fun getAllCategoriesOnce(): Single<List<Category>>

    @Query("SELECT * FROM quiz")
    fun getAllQuiz(): Flowable<List<Quiz>>

    @Query("SELECT * FROM quiz WHERE id == :id")
    fun getQuizOnce(id: String): Single<Quiz>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addQuiz(quiz: Quiz)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addQuestion(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(category: Category)

    @Update
    fun updateQuestion(question: Question)

    @Query("DELETE FROM question WHERE id == :id")
    fun removeQuestion(id: String): Int
}