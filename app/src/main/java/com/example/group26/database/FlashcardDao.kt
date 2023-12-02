package com.example.group26.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete

@Dao
interface FlashcardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(flashcardEntry: FlashcardEntry)

    @Query("SELECT * FROM flashcard_table")
    fun getAllFlashcards(): LiveData<List<FlashcardEntry>>

    @Delete
    fun deleteFlashCard(flashcardEntry:FlashcardEntry)

    @Query("DELETE FROM flashcard_table WHERE deckName = :deckName")
    fun deleteDeck(deckName:String)

    //TODO: add other operations
}
