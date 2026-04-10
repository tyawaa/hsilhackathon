package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class QuestionnaireAwalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire_awal)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnLanjut = findViewById<MaterialButton>(R.id.btnLanjutScan)
        val etNama = findViewById<TextInputEditText>(R.id.etNamaPasien)
        val etNik = findViewById<TextInputEditText>(R.id.etNikPasien)
        val etUsia = findViewById<TextInputEditText>(R.id.etUsiaPasien)
        val spinnerGender = findViewById<Spinner>(R.id.spinnerGender)

        btnBack.setOnClickListener {
            finish()
        }

        btnLanjut.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val nik = etNik.text.toString().trim()
            val usia = etUsia.text.toString().trim()
            
            if (nama.isEmpty() || nik.isEmpty() || usia.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // In a real app we'd save this to Room DB here or pass via Intent.
            val intent = Intent(this, ScanAIActivity::class.java)
            intent.putExtra("NAMA_PASIEN", nama)
            startActivity(intent)
        }
    }
}
