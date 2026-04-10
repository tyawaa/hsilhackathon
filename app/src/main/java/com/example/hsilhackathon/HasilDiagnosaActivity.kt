package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class HasilDiagnosaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hasil_diagnosa)

        val btnSelesai = findViewById<MaterialButton>(R.id.btnSelesai)
        val tvNamaPasienHasil = findViewById<TextView>(R.id.tvNamaPasienHasil)
        
        val namaPasien = intent.getStringExtra("NAMA_PASIEN") ?: "Nama Tidak Tersedia"
        tvNamaPasienHasil.text = "Pasien: $namaPasien"

        btnSelesai.setOnClickListener {
            // Save to room logic here (Mocked)
            Toast.makeText(this, "Data pasien berhasil disimpan secara offline (terenkripsi)", Toast.LENGTH_LONG).show()
            
            // Return to Dashbaord
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
