package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConsentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consent)

        val patientNik = intent.getStringExtra("PATIENT_NIK") ?: ""
        val patientName = intent.getStringExtra("NAMA_PASIEN") ?: "Pasien"
        val keluhan = intent.getStringExtra("KELUHAN_HARI_INI") ?: ""

        val btnBack = findViewById<ImageView>(R.id.btnBackConsent)
        val cb1 = findViewById<CheckBox>(R.id.cbConsent1)
        val cb2 = findViewById<CheckBox>(R.id.cbConsent2)
        val cb3 = findViewById<CheckBox>(R.id.cbConsent3)
        val btnAgree = findViewById<MaterialButton>(R.id.btnAgreeConsent)
        val tvPatientName = findViewById<TextView>(R.id.tvConsentPatientName)
        val tvPatientNik = findViewById<TextView>(R.id.tvConsentPatientNik)
        val tvTimestamp = findViewById<TextView>(R.id.tvConsentTimestamp)

        // Set patient info
        tvPatientName.text = "Nama: $patientName"
        tvPatientNik.text = if (patientNik.isNotEmpty()) "NIK: $patientNik" else "NIK: (Pasien Baru)"

        // Set current timestamp
        val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm 'WIB'", Locale("id", "ID"))
        tvTimestamp.text = "Waktu: ${sdf.format(Date())}"

        btnBack.setOnClickListener { finish() }

        // Check state listener for all checkboxes
        val checkListener = { _: android.widget.CompoundButton, _: Boolean ->
            val allChecked = cb1.isChecked && cb2.isChecked && cb3.isChecked
            btnAgree.isEnabled = allChecked
            if (allChecked) {
                btnAgree.backgroundTintList = android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#3182CE")
                )
                btnAgree.text = "Setuju & Lanjut ke Kamera AI"
            } else {
                btnAgree.backgroundTintList = android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#CCCCCC")
                )
                btnAgree.text = "Centang semua persetujuan di atas"
            }
        }

        cb1.setOnCheckedChangeListener(checkListener)
        cb2.setOnCheckedChangeListener(checkListener)
        cb3.setOnCheckedChangeListener(checkListener)

        btnAgree.setOnClickListener {
            val intent = Intent(this, ScanAIActivity::class.java)
            intent.putExtra("PATIENT_NIK", patientNik)
            intent.putExtra("NAMA_PASIEN", patientName)
            intent.putExtra("KELUHAN_HARI_INI", keluhan)
            intent.putExtra("CONSENT_GRANTED", true)
            startActivity(intent)
            finish()
        }
    }
}
