package com.example.hsilhackathon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hsilhackathon.data.entity.PatientEntity

@Dao
interface PatientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: PatientEntity)

    @Query("SELECT * FROM patients WHERE namaLengkap LIKE '%' || :query || '%' OR nik LIKE '%' || :query || '%' ORDER BY namaLengkap ASC")
    suspend fun searchByNameOrNik(query: String): List<PatientEntity>

    @Query("SELECT * FROM patients WHERE nik = :nik LIMIT 1")
    suspend fun getPatientByNik(nik: String): PatientEntity?

    @Query("SELECT * FROM patients ORDER BY namaLengkap ASC")
    suspend fun getAllPatients(): List<PatientEntity>

    @Query("SELECT COUNT(*) FROM patients")
    suspend fun getPatientCount(): Int
}
