package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.example.hsilhackathon.data.DatabaseProvider
import com.example.hsilhackathon.data.entity.NakesEntity
import com.example.hsilhackathon.utils.PasswordUtils
import com.example.hsilhackathon.utils.SessionManager
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activation)

        val etKodeFasilitas = findViewById<TextInputEditText>(R.id.etKodeFasilitasActivation)
        val btnAktivasi = findViewById<AppCompatButton>(R.id.btnAktivasi)
        val pbAktivasi = findViewById<ProgressBar>(R.id.pbAktivasi)
        val sessionManager = SessionManager(this)

        btnAktivasi.setOnClickListener {
            val kodeFasilitas = etKodeFasilitas.text.toString().trim().uppercase()

            if (kodeFasilitas.isEmpty()) {
                etKodeFasilitas.error = "Kode fasilitas wajib diisi"
                return@setOnClickListener
            }

            btnAktivasi.isEnabled = false
            btnAktivasi.text = "Menyinkronkan Data..."
            pbAktivasi.visibility = View.VISIBLE

            // Mock Data Sync (Simulating network delay)
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    delay(2000) // Simulate network call to Server/Firebase
                    
                    val nakesDao = DatabaseProvider.getDatabase(this@ActivationActivity).nakesDao()
                    
                    // Delete existing local data to ensure a fresh sync
                    nakesDao.clearAllNakes()
                    
                    // Creating Mock Nakes records as if downloaded from Firebase
                    val dummyNakes = listOf(
                        NakesEntity(
                            email = "andi@klinik.com",
                            namaLengkap = "dr. Andi Wijaya",
                            hashedPassword = PasswordUtils.hashPassword("password123"), // Server should ideally send hashes
                            kodeFasilitas = kodeFasilitas,
                            idPetugas = "NKS-001",
                            jabatan = "Dokter Umum"
                        ),
                        NakesEntity(
                            email = "budi@klinik.com",
                            namaLengkap = "Budi Santoso, S.Kep",
                            hashedPassword = PasswordUtils.hashPassword("password123"),
                            kodeFasilitas = kodeFasilitas,
                            idPetugas = "NKS-002",
                            jabatan = "Perawat"
                        )
                    )

                    // Insert to encrypted local DB
                    dummyNakes.forEach { 
                        nakesDao.insertNakes(it)
                    }

                    withContext(Dispatchers.Main) {
                        // Mark device as activated
                        sessionManager.setDeviceActivated(kodeFasilitas)
                        
                        Toast.makeText(this@ActivationActivity, "Aktivasi & Sync Berhasil!", Toast.LENGTH_LONG).show()
                        
                        // Proceed to Login
                        startActivity(Intent(this@ActivationActivity, LoginTenagaKesehatanActivity::class.java))
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        btnAktivasi.isEnabled = true
                        btnAktivasi.text = "Verifikasi & Sync Data"
                        pbAktivasi.visibility = View.GONE
                        Toast.makeText(this@ActivationActivity, "Aktivasi Gagal: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
