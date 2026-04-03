package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPasien = findViewById<AppCompatButton>(R.id.btnPasien)

        btnPasien.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}