package com.example.hsilhackathon.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consultations")
data class ConsultationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val caseId: String,
    val doctorName: String,
    val aiDiagnosis: String,
    val specialistDiagnosis: String,
    val treatmentRecommendation: String,
    val feedbackStatus: String, // "Match" or "Mismatch"
    val dateTimestamp: Long
)
