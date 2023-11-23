package com.example.group26.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FlashcardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(flashcardEntry: FlashcardEntry)

    @Query("SELECT * FROM flashcard_table")
    fun getAllFlashcards(): LiveData<List<FlashcardEntry>>

    //TODO: add other operations
}
