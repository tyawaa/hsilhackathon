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
        val etTanggalLahir = findViewById<TextInputEditText>(R.id.etTanggalLahir)
        val spinnerGender = findViewById<Spinner>(R.id.spinnerGender)
        val spinnerAgama = findViewById<Spinner>(R.id.spinnerAgama)
        val etNoTelp = findViewById<TextInputEditText>(R.id.etNoTelpPasien)
        val etAlamat = findViewById<TextInputEditText>(R.id.etAlamatPasien)
        val etPekerjaan = findViewById<TextInputEditText>(R.id.etPekerjaan)
        val spinnerGolonganDarah = findViewById<Spinner>(R.id.spinnerGolonganDarah)
        val etRiwayatPenyakit = findViewById<TextInputEditText>(R.id.etRiwayatPenyakit)
        val etKontakDarurat = findViewById<TextInputEditText>(R.id.etKontakDarurat)
        val etNomorBpjs = findViewById<TextInputEditText>(R.id.etNomorBpjs)

        btnBack.setOnClickListener {
            finish()
        }

        btnLanjut.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val nik = etNik.text.toString().trim()
            val tglLahir = etTanggalLahir.text.toString().trim()
            val noTelp = etNoTelp.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()
            val pekerjaan = etPekerjaan.text.toString().trim()
            val kontakDarurat = etKontakDarurat.text.toString().trim()
            // Riwayat penyakit dan BPJS opsional jadi tidak harus di validasi isNotEmpty()
            
            if (nama.isEmpty() || nik.isEmpty() || tglLahir.isEmpty() || noTelp.isEmpty() || alamat.isEmpty() || pekerjaan.isEmpty() || kontakDarurat.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua field yang wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // In a real app we'd save this to Room DB here or pass via Intent.
            val intent = Intent(this, ScanAIActivity::class.java)
            intent.putExtra("NAMA_PASIEN", nama)
            startActivity(intent)
        }
    }
}
