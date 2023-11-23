package com.example.group26.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(progressData: ProgressData)

    @Query("SELECT * FROM progress_table")
    fun getAllProgress(): LiveData<List<ProgressData>>

    //TODO: add other operations
}
