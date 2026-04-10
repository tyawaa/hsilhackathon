package com.example.hsilhackathon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hsilhackathon.data.entity.NakesEntity

@Dao
interface NakesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNakes(nakes: NakesEntity)

    @Query("SELECT * FROM nakes WHERE email = :email")
    suspend fun getNakesByEmail(email: String): NakesEntity?

    @Query("DELETE FROM nakes")
    suspend fun clearAllNakes()
}
