package com.example.group26.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "quiz_table")
data class QuizData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val question: String,
    val multipleChoice: List<String>
)