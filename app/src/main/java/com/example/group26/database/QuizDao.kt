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

    @Query("SELECT * FROM quiz_table")
    fun getAllQuizzes(): LiveData<List<QuizData>>

    //TODO: add other operations
}
