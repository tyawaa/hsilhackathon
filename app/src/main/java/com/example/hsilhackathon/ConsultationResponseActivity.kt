package com.example.hsilhackathon

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ConsultationResponseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultation_response)

        val btnBack = findViewById<ImageView>(R.id.btnBackRes)
        btnBack.setOnClickListener { finish() }

        val btnSelesaiRujukan = findViewById<MaterialButton>(R.id.btnSelesaiRujukan)
        btnSelesaiRujukan.setOnClickListener {
            showFeedbackLoopDialog()
        }
    }

    private fun showFeedbackLoopDialog() {
        AlertDialog.Builder(this)
            .setTitle("Feedback Loop SCIRA")
            .setMessage("Berdasarkan respons Spesialis, apakah diagnosis akhir SUSUAI dengan prediksi awal AI (Skabies)?\n\nData ini akan digunakan untuk melatih ulang AI SCIRA.")
            .setPositiveButton("Ya, Sesuai") { _, _ ->
                finishConsultation("Match")
            }
            .setNegativeButton("Tidak Sesuai") { _, _ ->
                finishConsultation("Mismatch")
            }
            .setCancelable(false)
            .show()
    }

    private fun finishConsultation(status: String) {
        Toast.makeText(this, "Kasus ditutup. Feedback ($status) disimpan untuk retraining model offline.", Toast.LENGTH_LONG).show()
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}
