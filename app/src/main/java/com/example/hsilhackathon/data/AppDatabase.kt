package com.example.hsilhackathon.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hsilhackathon.data.dao.NakesDao
import com.example.hsilhackathon.data.entity.NakesEntity
import com.example.hsilhackathon.data.dao.JournalDao
import com.example.hsilhackathon.data.entity.JournalEntity
import com.example.hsilhackathon.data.dao.ConsultationDao
import com.example.hsilhackathon.data.entity.ConsultationEntity
import com.example.hsilhackathon.data.dao.PatientDao
import com.example.hsilhackathon.data.entity.PatientEntity
import net.sqlcipher.database.SupportFactory

@Database(entities = [NakesEntity::class, JournalEntity::class, ConsultationEntity::class, PatientEntity::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun nakesDao(): NakesDao
    abstract fun journalDao(): JournalDao
    abstract fun consultationDao(): ConsultationDao
    abstract fun patientDao(): PatientDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, encryptionKey: ByteArray): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // REQUIRED: Initialize SQLCipher native libraries
                System.loadLibrary("sqlcipher")
                
                // Initialize SupportFactory with the provided encryption key
                val factory = SupportFactory(encryptionKey)
                
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "scira_secure_database.db"
                )
                .openHelperFactory(factory)
                .fallbackToDestructiveMigration()
                .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
