package com.example.hsilhackathon.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey
    val nik: String,
    val namaLengkap: String,
    val tanggalLahir: String,
    val jenisKelamin: String,
    val agama: String,
    val noTelepon: String,
    val alamat: String,
    val pekerjaan: String,
    val golonganDarah: String,
    val riwayatPenyakit: String = "",
    val kontakDarurat: String = "",
    val nomorBpjs: String = "",
    val lastDiagnosis: String = "",
    val lastDiagnosisDate: Long = 0L,
    val lastRecommendation: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
