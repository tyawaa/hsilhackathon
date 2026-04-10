package com.example.hsilhackathon.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom

object DatabaseProvider {

    private var database: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (database == null) {
            val key = getOrGenerateDatabaseKey(context)
            database = AppDatabase.getDatabase(context, key)
        }
        return database!!
    }

    private fun getOrGenerateDatabaseKey(context: Context): ByteArray {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secret_db_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val keyString = sharedPreferences.getString("db_key", null)
        if (keyString != null) {
            return android.util.Base64.decode(keyString, android.util.Base64.DEFAULT)
        }

        // Generate new secure key
        val secureRandom = SecureRandom()
        val newKey = ByteArray(32)
        secureRandom.nextBytes(newKey)

        val encodedKey = android.util.Base64.encodeToString(newKey, android.util.Base64.DEFAULT)
        sharedPreferences.edit().putString("db_key", encodedKey).apply()

        return newKey
    }
}
