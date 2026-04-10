package com.example.hsilhackathon

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            .setNeutralButton("Kembali Baca") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }

    private fun finishConsultation(status: String) {
        val consultation = com.example.hsilhackathon.data.entity.ConsultationEntity(
            caseId = "CS-2026-0047",
            doctorName = "Dr. Syifa Astuti, Sp.DVE",
            aiDiagnosis = "Skabies",
            specialistDiagnosis = "Skabies",
            treatmentRecommendation = "1. Tetap lanjutkan Permethrin 5%.\n2. Pastikan seluruh anggota keluarga / kontak erat juga diobati secara bersamaan.\n3. Cuci sprei dengan air panas.",
            feedbackStatus = status,
            dateTimestamp = System.currentTimeMillis()
        )

        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            val appDb = com.example.hsilhackathon.data.DatabaseProvider.getDatabase(this@ConsultationResponseActivity)
            appDb.consultationDao().insertConsultation(consultation)
            
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                Toast.makeText(this@ConsultationResponseActivity, "Kasus ditutup. Feedback ($status) disimpan secara offline.", Toast.LENGTH_LONG).show()
                val intent = Intent(this@ConsultationResponseActivity, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
        }
    }
}
