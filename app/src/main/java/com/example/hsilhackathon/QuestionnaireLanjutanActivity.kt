package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class QuestionnaireLanjutanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire_lanjutan)

        val namaPasien = intent.getStringExtra("NAMA_PASIEN") ?: "Pasien"
        val top1Label = intent.getStringExtra("CNN_TOP1_LABEL") ?: ""
        val top2Label = intent.getStringExtra("CNN_TOP2_LABEL") ?: ""
        val top3Label = intent.getStringExtra("CNN_TOP3_LABEL") ?: ""
        val top1Group = intent.getStringExtra("CNN_TOP1_GROUP") ?: "Bercak Merah"
        val top2Group = intent.getStringExtra("CNN_TOP2_GROUP") ?: ""

        val llBercak = findViewById<LinearLayout>(R.id.llBercakMerah)
        val llBintil = findViewById<LinearLayout>(R.id.llBintilMerah)
        val btnLihatHasil = findViewById<MaterialButton>(R.id.btnLihatHasilAILanjut)

        if (top1Group == "Bercak Merah") {
            llBercak.visibility = View.VISIBLE
        } else if (top1Group == "Bintil Merah") {
            llBintil.visibility = View.VISIBLE
        }

        btnLihatHasil.setOnClickListener {
            val intentHasil = Intent(this, HasilDiagnosaActivity::class.java)
            intentHasil.putExtras(intent) // Pass CNN results & Identitas

            if (top1Group == "Bercak Merah") {
                val rg1 = findViewById<RadioGroup>(R.id.rgBercakMenetap)
                val rg2 = findViewById<RadioGroup>(R.id.rgBercakBaal)
                val rg3 = findViewById<RadioGroup>(R.id.rgBercakNyeri)
                val rg4 = findViewById<RadioGroup>(R.id.rgBercakGatal)
                val rg5 = findViewById<RadioGroup>(R.id.rgBercakSaraf)
                val rg6 = findViewById<RadioGroup>(R.id.rgBercakOtot)
                val rg7 = findViewById<RadioGroup>(R.id.rgBercakHewan)
                val rg8 = findViewById<RadioGroup>(R.id.rgBercakCentral)
                val rg9 = findViewById<RadioGroup>(R.id.rgBercakHifa)
                val rg10 = findViewById<RadioGroup>(R.id.rgBercakPseudohifa)

                if (rg1.checkedRadioButtonId == -1 || rg2.checkedRadioButtonId == -1 || 
                    rg3.checkedRadioButtonId == -1 || rg4.checkedRadioButtonId == -1 ||
                    rg5.checkedRadioButtonId == -1 || rg6.checkedRadioButtonId == -1 ||
                    rg7.checkedRadioButtonId == -1 || rg8.checkedRadioButtonId == -1 ||
                    rg9.checkedRadioButtonId == -1 || rg10.checkedRadioButtonId == -1) {
                    Toast.makeText(this, "Mohon jawab semua pertanyaan Bercak Merah", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Encoding sesuai spek:
                // Menetap / Kekambuhan: 1 = Menetap, 2 = Kekambuhan
                // P2_Baal: 1 = Ya, 2 = Tidak (dll, kecuali Gatal: 1=Ya, 0=Tidak) - oh wait, yang gatal yang mana?
                // Di spec: P4_Gatal = 1/2. Lalu di Bintil ada Gatal = 1/0.
                
                intentHasil.putExtra("Menetap_Kekambuhan", if (findViewById<RadioButton>(rg1.checkedRadioButtonId).text == "Menetap") 1f else 2f)
                intentHasil.putExtra("P2_Baal", if (findViewById<RadioButton>(rg2.checkedRadioButtonId).text == "Ya") 1f else 2f)
                intentHasil.putExtra("P3_Nyeri", if (findViewById<RadioButton>(rg3.checkedRadioButtonId).text == "Ya") 1f else 2f)
                intentHasil.putExtra("P4_Gatal", if (findViewById<RadioButton>(rg4.checkedRadioButtonId).text == "Ya") 1f else 2f)
                intentHasil.putExtra("P5_Pembesaran_Saraf", if (findViewById<RadioButton>(rg5.checkedRadioButtonId).text == "Ya") 1f else 2f)
                intentHasil.putExtra("P6_Otot_Menurun", if (findViewById<RadioButton>(rg6.checkedRadioButtonId).text == "Ya") 1f else 2f)
                intentHasil.putExtra("P9_Hewan_Bulu_Rontok", if (findViewById<RadioButton>(rg7.checkedRadioButtonId).text == "Ya") 1f else 2f)
                intentHasil.putExtra("Central_Healing", if (findViewById<RadioButton>(rg8.checkedRadioButtonId).text == "Ya") 1f else 2f)
                intentHasil.putExtra("Hifa_Sejati", if (findViewById<RadioButton>(rg9.checkedRadioButtonId).text == "Ya") 1f else 2f)
                intentHasil.putExtra("Pseudohifa", if (findViewById<RadioButton>(rg10.checkedRadioButtonId).text == "Ya") 1f else 2f)

            } else if (top1Group == "Bintil Merah") {
                val rg1 = findViewById<RadioGroup>(R.id.rgBintilGatalMalam)
                val rg2 = findViewById<RadioGroup>(R.id.rgBintilSekitar)
                val rg3 = findViewById<RadioGroup>(R.id.rgBintilTanah)
                val rg4 = findViewById<RadioGroup>(R.id.rgBintilGatal)
                val rg5 = findViewById<RadioGroup>(R.id.rgBintilPerih)

                if (rg1.checkedRadioButtonId == -1 || rg2.checkedRadioButtonId == -1 || 
                    rg3.checkedRadioButtonId == -1 || rg4.checkedRadioButtonId == -1 ||
                    rg5.checkedRadioButtonId == -1) {
                    Toast.makeText(this, "Mohon jawab semua pertanyaan Bintil Merah", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                intentHasil.putExtra("Gatal_Malam", if (findViewById<RadioButton>(rg1.checkedRadioButtonId).text == "Ya") 1f else 2f)
                intentHasil.putExtra("Orang_Sekitar_Terkena", if (findViewById<RadioButton>(rg2.checkedRadioButtonId).text == "Ya") 1f else 2f)
                intentHasil.putExtra("Kontak_Tanah_Pasir", if (findViewById<RadioButton>(rg3.checkedRadioButtonId).text == "Ya") 1f else 2f)
                // Spec: Gatal = 1/0, Perih = 1/0 untuk form ini
                intentHasil.putExtra("Gatal", if (findViewById<RadioButton>(rg4.checkedRadioButtonId).text == "Ya") 1f else 0f)
                intentHasil.putExtra("Perih", if (findViewById<RadioButton>(rg5.checkedRadioButtonId).text == "Ya") 1f else 0f)
            }

            startActivity(intentHasil)
            finish()
        }
    }
}
