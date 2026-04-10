package com.example.hsilhackathon.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SessionManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPrefs = EncryptedSharedPreferences.create(
        context,
        "scira_session_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun login(email: String, namaLengkap: String) {
        sharedPrefs.edit()
            .putBoolean("is_logged_in", true)
            .putString("email", email)
            .putString("nama_lengkap", namaLengkap)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPrefs.getBoolean("is_logged_in", false)
    }

    fun logout() {
        sharedPrefs.edit().clear().apply()
    }

    fun getUserName(): String {
        return sharedPrefs.getString("nama_lengkap", "Tenaga Kesehatan") ?: "Tenaga Kesehatan"
    }

    fun getUserEmail(): String {
        return sharedPrefs.getString("email", "") ?: ""
    }
}
