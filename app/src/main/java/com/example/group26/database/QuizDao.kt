package com.example.group26.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(quizData: QuizData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(quizzes: List<QuizData>)

    @Query("SELECT * FROM quiz_table")
    fun getAllQuizzes(): LiveData<List<QuizData>>

    @Query("SELECT * FROM quiz_table ORDER BY RANDOM() LIMIT 5")
    fun getRandomQuizzes(): List<QuizData>

    //TODO: add other operations
}
