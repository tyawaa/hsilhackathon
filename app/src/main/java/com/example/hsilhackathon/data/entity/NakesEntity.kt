package com.example.hsilhackathon.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nakes")
data class NakesEntity(
    @PrimaryKey
    val email: String,
    val namaLengkap: String,
    val hashedPassword: String,
    val kodeFasilitas: String,
    val idPetugas: String,
    val jabatan: String
)
