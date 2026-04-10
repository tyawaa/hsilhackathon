package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanAIActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_ai)

        val btnCapture = findViewById<ImageButton>(R.id.btnCapture)
        val btnBack = findViewById<ImageView>(R.id.btnBackCam)
        val pbAnalisis = findViewById<ProgressBar>(R.id.pbAnalisis)
        
        val namaPasien = intent.getStringExtra("NAMA_PASIEN") ?: "Pasien"

        btnBack.setOnClickListener {
            finish()
        }

        btnCapture.setOnClickListener {
            btnCapture.visibility = View.INVISIBLE
            pbAnalisis.visibility = View.VISIBLE
            
            Toast.makeText(this, "AI Sedang Menganalisis Gambar...", Toast.LENGTH_SHORT).show()

            // Simulate Local AI processing time
            lifecycleScope.launch(Dispatchers.IO) {
                delay(3000)
                
                withContext(Dispatchers.Main) {
                    val intent = Intent(this@ScanAIActivity, QuestionnaireLanjutanActivity::class.java)
                    intent.putExtra("NAMA_PASIEN", namaPasien)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
