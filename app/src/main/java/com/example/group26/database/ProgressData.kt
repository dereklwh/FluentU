package com.example.group26.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress_table")
data class ProgressData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val userId: Long? = null, // Assuming we implement user entity
    val quizId: Long,
    val score: Int,
    val attempts: Int,
    val quizType: String
)
