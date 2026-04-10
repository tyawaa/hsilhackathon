package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hsilhackathon.data.DatabaseProvider
import com.example.hsilhackathon.utils.SessionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NakesProfileActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nakes_profile)

        sessionManager = SessionManager(this)

        val btnBack = findViewById<ImageView>(R.id.btnBackProfile)
        val btnLogout = findViewById<MaterialButton>(R.id.btnLogout)

        btnBack.setOnClickListener { finish() }

        btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar dari akun ini?")
                .setPositiveButton("Logout") { _, _ ->
                    sessionManager.logout()
                    val intent = Intent(this, LoginTenagaKesehatanActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        loadProfileData()
    }

    private fun loadProfileData() {
        val email = sessionManager.getUserEmail()
        val name = sessionManager.getUserName()

        // Set basic data from session
        findViewById<TextView>(R.id.tvProfileName).text = name
        findViewById<TextView>(R.id.tvProfileEmail).text = email
        findViewById<TextView>(R.id.tvProfileInitial).text =
            name.firstOrNull()?.uppercase() ?: "?"

        // Load full nakes data from DB
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val db = DatabaseProvider.getDatabase(this@NakesProfileActivity)
                val nakes = db.nakesDao().getNakesByEmail(email)
                val patientCount = db.patientDao().getPatientCount()
                val consultationCount = db.consultationDao().getAllConsultations().size

                withContext(Dispatchers.Main) {
                    if (nakes != null) {
                        findViewById<TextView>(R.id.tvProfileJabatan).text = nakes.jabatan
                        findViewById<TextView>(R.id.tvProfileIdPetugas).text = nakes.idPetugas
                        findViewById<TextView>(R.id.tvProfileKodeFasilitas).text = nakes.kodeFasilitas
                    }
                    findViewById<TextView>(R.id.tvStatPasien).text = patientCount.toString()
                    findViewById<TextView>(R.id.tvStatKonsultasi).text = consultationCount.toString()
                }
            } catch (e: Exception) {
                // Silently handle - session data is already displayed
            }
        }
    }
}
