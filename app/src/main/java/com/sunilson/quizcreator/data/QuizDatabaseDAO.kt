package com.sunilson.quizcreator.data

import android.arch.persistence.room.*
import com.sunilson.quizcreator.data.models.*
import io.reactivex.Flowable
import io.reactivex.Single

@Dao()
interface QuizDatabaseDAO {

    @Query("SELECT * FROM question")
    fun getAllQuestions(): Flowable<List<Question>>

    @Query("SELECT * FROM question WHERE categoryId IN (:categoryIds)")
    fun getAllQuestions(categoryIds: Array<String>): Flowable<List<Question>>

    @Query("SELECT * FROM question")
    fun getAllQuestionsOnce(): Single<List<Question>>

    @Query("SELECT * FROM question WHERE type == :type")
    fun getAllQuestionsOnce(type: QuestionType): Single<List<Question>>

    @Query("SELECT * FROM question WHERE categoryId IN (:categoryIds)")
    fun getAllQuestionsOnce(categoryIds: Array<String>): Single<List<Question>>

    @Query("SELECT * FROM question WHERE categoryId IN (:categoryIds) AND type == :type")
    fun getAllQuestionsOnce(type: QuestionType, categoryIds: Array<String>): Single<List<Question>>

    @Query("SELECT * FROM category")
    fun getAllCategories(): Flowable<List<Category>>

    @Query("SELECT * FROM category")
    fun getAllCategoriesOnce(): Single<List<Category>>

    @Query("SELECT * FROM quiz")
    fun getAllQuiz(): Flowable<List<Quiz>>

    @Query("SELECT * FROM quiz")
    fun getAllQuizOnce(): Single<List<Quiz>>

    @Query("SELECT * FROM quiz WHERE id == :id")
    fun getQuizOnce(id: String): Single<Quiz>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addQuiz(quiz: Quiz)

    @Query("DELETE FROM quiz")
    fun deleteAllQuiz()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addQuestions(vararg questions: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addQuestion(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategories(vararg category: Category)

    @Update
    fun updateCategory(category: Category)

    @Query("DELETE FROM question WHERE categoryId == :id")
    fun removeQuestionsWithCategory(id: String)

    @Query("DELETE FROM category WHERE id == :id")
    fun removeCategory(id: String)

    @Update
    fun updateQuestion(question: Question)

    @Query("DELETE FROM question WHERE id == :id")
    fun removeQuestion(id: String): Int

    @Query("SELECT * FROM statistics WHERE id == 'statistics'")
    fun getStatistics(): Flowable<Statistics>

    @Query("SELECT * FROM statistics WHERE id == 'statistics'")
    fun getStatisticsOnce(): Single<Statistics>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setStatistics(statistics: Statistics)

    @Query("SELECT COUNT(*) FROM question WHERE categoryId == :categoryId")
    fun countCategoryQuestions(categoryId: String): Int
}