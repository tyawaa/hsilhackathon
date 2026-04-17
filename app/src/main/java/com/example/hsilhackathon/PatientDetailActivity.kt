package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hsilhackathon.data.DatabaseProvider
import com.example.hsilhackathon.data.entity.PatientEntity
import com.example.hsilhackathon.security.BaseSecurityActivity
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PatientDetailActivity : BaseSecurityActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_detail)

        val patientNik = intent.getStringExtra("PATIENT_NIK") ?: run {
            finish()
            return
        }

        val btnBack = findViewById<ImageView>(R.id.btnBackDetail)
        btnBack.setOnClickListener { finish() }

        val btnNewPrediction = findViewById<MaterialButton>(R.id.btnNewPrediction)
        btnNewPrediction.setOnClickListener {
            val intent = Intent(this, ScanAIActivity::class.java)
            intent.putExtra("PATIENT_NIK", patientNik)
            startActivity(intent)
        }

        loadPatientData(patientNik)
    }

    private fun loadPatientData(nik: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = DatabaseProvider.getDatabase(this@PatientDetailActivity)
            val patient = db.patientDao().getPatientByNik(nik)

            withContext(Dispatchers.Main) {
                if (patient != null) {
                    bindPatientData(patient)
                } else {
                    finish()
                }
            }
        }
    }

    private fun bindPatientData(patient: PatientEntity) {
        // Header
        findViewById<TextView>(R.id.tvDetailInitial).text =
            patient.namaLengkap.firstOrNull()?.uppercase() ?: "?"
        findViewById<TextView>(R.id.tvDetailName).text = patient.namaLengkap
        findViewById<TextView>(R.id.tvDetailNik).text = "NIK: ${patient.nik}"

        // Info section
        findViewById<TextView>(R.id.tvDetailTglLahir).text = patient.tanggalLahir.ifEmpty { "-" }
        findViewById<TextView>(R.id.tvDetailGender).text = patient.jenisKelamin.ifEmpty { "-" }
        findViewById<TextView>(R.id.tvDetailAgama).text = patient.agama.ifEmpty { "-" }
        findViewById<TextView>(R.id.tvDetailPhone).text = patient.noTelepon.ifEmpty { "-" }
        findViewById<TextView>(R.id.tvDetailAlamat).text = patient.alamat.ifEmpty { "-" }
        findViewById<TextView>(R.id.tvDetailPekerjaan).text = patient.pekerjaan.ifEmpty { "-" }
        findViewById<TextView>(R.id.tvDetailGolDarah).text = patient.golonganDarah.ifEmpty { "-" }
        findViewById<TextView>(R.id.tvDetailBpjs).text =
            if (patient.nomorBpjs.isNotEmpty()) patient.nomorBpjs else "-"
        findViewById<TextView>(R.id.tvDetailKontakDarurat).text =
            if (patient.kontakDarurat.isNotEmpty()) patient.kontakDarurat else "-"

        // Riwayat
        val tvRiwayat = findViewById<TextView>(R.id.tvDetailRiwayat)
        tvRiwayat.text = if (patient.riwayatPenyakit.isNotEmpty()) {
            patient.riwayatPenyakit
        } else {
            "Tidak ada riwayat penyakit tercatat."
        }

        // Diagnosis
        val tvDiagnosis = findViewById<TextView>(R.id.tvDetailDiagnosis)
        val tvDiagnosisDate = findViewById<TextView>(R.id.tvDetailDiagnosisDate)
        if (patient.lastDiagnosis.isNotEmpty()) {
            tvDiagnosis.text = patient.lastDiagnosis
            if (patient.lastDiagnosisDate > 0) {
                val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
                tvDiagnosisDate.text = "Tanggal: ${sdf.format(Date(patient.lastDiagnosisDate))}"
            }
        } else {
            tvDiagnosis.text = "Belum ada diagnosis."
            tvDiagnosis.setTextColor(android.graphics.Color.parseColor("#8A8A8A"))
        }

        // Anjuran
        val tvRecommendation = findViewById<TextView>(R.id.tvDetailRecommendation)
        tvRecommendation.text = if (patient.lastRecommendation.isNotEmpty()) {
            patient.lastRecommendation
        } else {
            "Belum ada anjuran. Lakukan prediksi untuk mendapatkan rekomendasi."
        }
    }
}
