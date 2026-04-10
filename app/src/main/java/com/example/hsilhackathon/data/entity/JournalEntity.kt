package com.example.hsilhackathon.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journals")
data class JournalEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val source: String,
    val date: String,
    val category: String, // e.g., "Skabies", "Kusta", "NTD Umum"
    val contentUrl: String,
    val isFromSync: Boolean = false // to mark if this was fetched from wifi later
)
