package com.example.hsilhackathon.security

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.hsilhackathon.LoginTenagaKesehatanActivity

/**
 * Aktivitas Dasar untuk Kepatuhan ISO 27001 dan UU PDP
 * 
 * Fitur Security:
 * 1. Mencegah Screenshot/Screen Recording (Menjaga Kerahasiaan Data Medis Pasien)
 * 2. Auto Logout (Session Management) jika tidak ada interaksi selama 15 menit
 */
abstract class BaseSecurityActivity : AppCompatActivity() {

    private val timeoutHandler = Handler(Looper.getMainLooper())
    private val TIMEOUT_IN_MS: Long = 15 * 60 * 1000 // 15 menit (sesuaikan kebijakan faskes)

    private val timeoutRunnable = Runnable {
        performAutoLogout()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // [PRIVACY DATA] Mencegah Screenshot dan Screen Recording
        // Ini memenuhi pelindungan data pasien agar Nakes tidak secara sengaja/tidak sengaja
        // mengambil gambar layar berisi rekam medis menggunakan fitur screenshot OS.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    override fun onResume() {
        super.onResume()
        startSessionTimeout()
    }

    override fun onPause() {
        super.onPause()
        stopSessionTimeout()
    }

    // Mendeteksi sentuhan pada layar
    override fun onUserInteraction() {
        super.onUserInteraction()
        resetSessionTimeout()
    }

    private fun startSessionTimeout() {
        resetSessionTimeout()
    }

    private fun stopSessionTimeout() {
        timeoutHandler.removeCallbacks(timeoutRunnable)
    }

    private fun resetSessionTimeout() {
        timeoutHandler.removeCallbacks(timeoutRunnable)
        timeoutHandler.postDelayed(timeoutRunnable, TIMEOUT_IN_MS)
    }

    private fun performAutoLogout() {
        // Arahkan kembali ke halaman Login Nakes dengan menghapus backstack
        val intent = Intent(this, LoginTenagaKesehatanActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
