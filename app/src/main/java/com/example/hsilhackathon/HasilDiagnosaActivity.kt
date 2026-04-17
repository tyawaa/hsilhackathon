package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hsilhackathon.data.DatabaseProvider
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class HasilDiagnosaActivity : AppCompatActivity() {

    private lateinit var rfModelExecutor: RFModelExecutor
    private var finalDiagnosisText: String = ""
    private var finalTreatmentText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hasil_diagnosa)

        val btnSelesai = findViewById<MaterialButton>(R.id.btnSelesai)
        val btnSecondOpinionResult = findViewById<MaterialButton>(R.id.btnSecondOpinionResult)
        val tvNamaPasienHasil = findViewById<TextView>(R.id.tvNamaPasienHasil)
        val tvDiagnosisBox = findViewById<TextView>(R.id.tvDiagnosisResult)
        val tvTreatment = findViewById<TextView>(R.id.tvTreatmentDesc)
        
        val namaPasien = intent.getStringExtra("NAMA_PASIEN") ?: "Nama Tidak Tersedia"
        val patientNik = intent.getStringExtra("PATIENT_NIK") ?: ""
        
        tvNamaPasienHasil.text = "Pasien: $namaPasien"

        rfModelExecutor = RFModelExecutor(this)

        // Process AI inference
        lifecycleScope.launch(Dispatchers.IO) {
            val db = DatabaseProvider.getDatabase(this@HasilDiagnosaActivity)
            val patient = db.patientDao().getAllPatients().firstOrNull { it.nik == patientNik }

            val features = mutableMapOf<String, Any>()
            
            // 1. Personal Data
            if (patient != null) {
                // Determine gender as "L" or "P"
                features["Gender"] = if (patient.jenisKelamin.contains("Perempuan", true)) "P" else "L"
                
                // Calculate age
                features["Umur"] = calculateAge(patient.tanggalLahir).toFloat()
            } else {
                features["Gender"] = "L"
                features["Umur"] = 30f
            }
            
            features["Status"] = "Baru"
            
            // 2. CNN Outputs
            val top1Group = intent.getStringExtra("CNN_TOP1_GROUP") ?: "Bercak Merah"
            val top2Group = intent.getStringExtra("CNN_TOP2_GROUP") ?: ""
            features["Group"] = top1Group
            features["cnn_top1_label"] = intent.getStringExtra("CNN_TOP1_LABEL") ?: ""
            features["cnn_top2_label"] = intent.getStringExtra("CNN_TOP2_LABEL") ?: ""
            features["cnn_top3_label"] = intent.getStringExtra("CNN_TOP3_LABEL") ?: ""
            features["cnn_top1_group"] = top1Group
            features["cnn_top2_group"] = top2Group
            features["cnn_top1_conf"] = intent.getFloatExtra("CNN_TOP1_CONF", 0f)
            features["cnn_top2_conf"] = intent.getFloatExtra("CNN_TOP2_CONF", 0f)
            
            // 3. Questionnaire Extracted features
            val qFeatures = listOf(
                "Menetap_Kekambuhan", "P2_Baal", "P3_Nyeri", "P4_Gatal", "P5_Pembesaran_Saraf",
                "P6_Otot_Menurun", "P9_Hewan_Bulu_Rontok", "Central_Healing", "Hifa_Sejati",
                "Pseudohifa", "Gatal_Malam", "Orang_Sekitar_Terkena", "Kontak_Tanah_Pasir",
                "Gatal", "Perih"
            )
            
            for (feat in qFeatures) {
                // Replace underscore with space logic if needed or match schema
                var schemaKey = feat
                if (feat == "Menetap_Kekambuhan") schemaKey = "Menetap / Kekambuhan"
                
                features[schemaKey] = intent.getFloatExtra(feat, 0f)
            }

            // Execute RF Model
            val diagnosisAkhir = rfModelExecutor.predict(features)

            withContext(Dispatchers.Main) {
                // Update UI based on diagnosis
                val diagnosisTitle = findViewById<TextView>(R.id.tvDiagnosisResult)
                val treatmentDesc = findViewById<TextView>(R.id.tvTreatmentDesc)
                
                diagnosisTitle?.text = diagnosisAkhir
                
                when (diagnosisAkhir.uppercase()) {
                    "KUSTA" -> treatmentDesc?.text = "Rekomendasi MDR (Multi Drug Therapy), silakan rujuk spesialis."
                    "TINEA" -> treatmentDesc?.text = "Berikan salep antifungal lokal."
                    "SCABIES" -> treatmentDesc?.text = "Salep permethrin dan edukasi kebersihan personal."
                    "CLM" -> treatmentDesc?.text = "Rekomendasi tindakan albendazole lokal/oral."
                    else -> treatmentDesc?.text = "Pertimbangkan observasi dan second opinion."
                }
                
                finalDiagnosisText = diagnosisAkhir
                finalTreatmentText = treatmentDesc?.text?.toString() ?: ""
                
                // Simpan/Hubungkan ke Backend Database Lokal (Room)
                if (patient != null) {
                    val updatedPatient = patient.copy(
                        lastDiagnosis = finalDiagnosisText,
                        lastDiagnosisDate = System.currentTimeMillis(),
                        lastRecommendation = finalTreatmentText
                    )
                    // Eksekusi update data ke database lokal
                    lifecycleScope.launch(Dispatchers.IO) {
                        db.patientDao().insertPatient(updatedPatient)
                    }
                }
            }
        }

        btnSelesai.setOnClickListener {
            Toast.makeText(this, "Sesi selesai", Toast.LENGTH_SHORT).show()
            val dashboardIntent = Intent(this, DashboardActivity::class.java)
            dashboardIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(dashboardIntent)
            finish()
        }

        btnSecondOpinionResult?.setOnClickListener {
            val dIntent = Intent(this, DermatologistListActivity::class.java)
            startActivity(dIntent)
        }
    }
    
    private fun calculateAge(tglLahir: String): Int {
        // Asumsi format dd-MM-yyyy atau dd/MM/yyyy
        try {
            val parts = tglLahir.split("-", "/")
            if (parts.size == 3) {
                val year = parts[2].toInt()
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                return currentYear - year
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 30
    }

    override fun onDestroy() {
        super.onDestroy()
        rfModelExecutor.close()
    }
}
