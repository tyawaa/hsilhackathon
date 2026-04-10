package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class QuestionnaireLanjutanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire_lanjutan)

        val btnLihatHasil = findViewById<MaterialButton>(R.id.btnLihatHasil)
        val rgGatal = findViewById<RadioGroup>(R.id.rgGatal)
        val rgKontak = findViewById<RadioGroup>(R.id.rgKontak)
        
        val namaPasien = intent.getStringExtra("NAMA_PASIEN") ?: "Pasien"

        btnLihatHasil.setOnClickListener {
            if (rgGatal.checkedRadioButtonId == -1 || rgKontak.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Mohon lengkapi jawaban radio button", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, HasilDiagnosaActivity::class.java)
            intent.putExtra("NAMA_PASIEN", namaPasien)
            startActivity(intent)
            finish()
        }
    }
}
