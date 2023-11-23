package com.example.group26.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard_table")
data class FlashcardEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val englishPhrase: String,
    val frenchPhrase: String? = null,
    val spanishPhrase: String? = null,
    val chinesePhrase: String? = null
    )