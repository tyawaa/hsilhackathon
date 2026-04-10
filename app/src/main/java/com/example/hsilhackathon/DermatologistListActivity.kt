package com.example.hsilhackathon

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class DermatologistListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dermatologist_list)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        val btnSendSyifa = findViewById<MaterialButton>(R.id.btnSendSyifa)
        val btnSendRina = findViewById<MaterialButton>(R.id.btnSendRina)

        btnSendSyifa.setOnClickListener {
            showConfirmationDialog("Dr. Syifa Astuti, Sp.DVE")
        }

        btnSendRina.setOnClickListener {
            showConfirmationDialog("Dr. Rina Arisma, Sp.DVE")
        }
    }

    private fun showConfirmationDialog(doctorName: String) {
        val message = "Yang akan dikirim:\n" +
                "✓ Data klinis anonymized\n" +
                "✓ 3 foto kulit terenkripsi\n" +
                "✓ Hasil AI SCIRA\n" +
                "✓ Jawaban kuesioner\n" +
                "✓ Catatan tambahan GP\n\n" +
                "⚠ Foto akan otomatis dihapus dari server dalam 24 jam setelah konsultasi selesai."

        AlertDialog.Builder(this)
            .setTitle("Kirim ke $doctorName?")
            .setMessage(message)
            .setPositiveButton("Kirim Sekarang") { _, _ ->
                simulateSendAndReturn()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun simulateSendAndReturn() {
        Toast.makeText(this, "Mengenkripsi dan mengirim data...", Toast.LENGTH_SHORT).show()

        // After returning to dashboard, we tell it to start the mock timer
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra("EXTRA_START_MOCK_CONSULTATION", true)
        startActivity(intent)
        finish()
    }
}
