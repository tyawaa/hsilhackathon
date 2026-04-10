package com.example.hsilhackathon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hsilhackathon.data.entity.ConsultationEntity

@Dao
interface ConsultationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsultation(consultation: ConsultationEntity)

    @Query("SELECT * FROM consultations ORDER BY dateTimestamp DESC")
    suspend fun getAllConsultations(): List<ConsultationEntity>
}
