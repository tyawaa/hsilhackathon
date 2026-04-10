package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hsilhackathon.utils.SessionManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val sessionManager = SessionManager(this)
        
        // As per the offline-first Health Worker architecture,
        // we bypass the patient login and go straight to the Nakes Auth flow.
        // NEW: Check if device is activated first
        if (!sessionManager.isDeviceActivated()) {
            startActivity(Intent(this, ActivationActivity::class.java))
        } else if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, DashboardActivity::class.java))
        } else {
            startActivity(Intent(this, LoginTenagaKesehatanActivity::class.java))
        }
        
        finish() // Close this dispatch activity
    }
}