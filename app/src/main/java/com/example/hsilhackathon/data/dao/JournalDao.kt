package com.example.hsilhackathon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hsilhackathon.data.entity.JournalEntity

@Dao
interface JournalDao {

    @Query("SELECT * FROM journals ORDER BY date DESC")
    suspend fun getAllJournals(): List<JournalEntity>

    @Query("SELECT * FROM journals WHERE category = :category ORDER BY date DESC")
    suspend fun getJournalsByCategory(category: String): List<JournalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournals(journals: List<JournalEntity>)

    @Query("DELETE FROM journals")
    suspend fun deleteAllJournals()
}
