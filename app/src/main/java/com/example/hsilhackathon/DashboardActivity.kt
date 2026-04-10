package com.example.hsilhackathon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val btnGoScan = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnGoScan)
        btnGoScan.setOnClickListener {
            val intent = android.content.Intent(this, PatientSearchActivity::class.java)
            startActivity(intent)
        }

        // Bottom Nav: Database Pasien (center button)
        val btnNavDatabase = findViewById<android.widget.FrameLayout>(R.id.btnNavDatabase)
        btnNavDatabase.setOnClickListener {
            val intent = android.content.Intent(this, PatientListActivity::class.java)
            startActivity(intent)
        }

        val btnNavJurnal = findViewById<android.widget.LinearLayout>(R.id.btnNavJurnal)
        btnNavJurnal.setOnClickListener {
            val intent = android.content.Intent(this, JournalListActivity::class.java)
            startActivity(intent)
        }

        val btnNavHistory = findViewById<android.widget.LinearLayout>(R.id.btnNavHistory)
        btnNavHistory?.setOnClickListener {
            val intent = android.content.Intent(this, ConsultationHistoryActivity::class.java)
            startActivity(intent)
        }

        val btnSecondOpinionSyifa = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSecondOpinionSyifa)
        val btnSecondOpinionRina = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSecondOpinionRina)

        val navToDermatologist = android.view.View.OnClickListener {
            val intent = android.content.Intent(this, DermatologistListActivity::class.java)
            startActivity(intent)
        }

        btnSecondOpinionSyifa?.setOnClickListener(navToDermatologist)
        btnSecondOpinionRina?.setOnClickListener(navToDermatologist)

        handleMockConsultationIntent(intent)
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        handleMockConsultationIntent(intent)
    }

    private fun handleMockConsultationIntent(intent: android.content.Intent?) {
        if (intent?.getBooleanExtra("EXTRA_START_MOCK_CONSULTATION", false) == true) {
            // Remove the extra so it doesn't trigger again on rotation
            intent.removeExtra("EXTRA_START_MOCK_CONSULTATION")
            
            // Start the Mock 20-second timer
            android.widget.Toast.makeText(this, "Permintaan Konsultasi dikirim. Menunggu respons...", android.widget.Toast.LENGTH_LONG).show()

            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                // Show notification / Banner
                showConsultationResponseBanner()
            }, 10000) // For quick demo, 10 seconds is usually better than 20.
        }
    }

    private fun showConsultationResponseBanner() {
        val view = findViewById<android.view.View>(android.R.id.content)
        val snackbar = com.google.android.material.snackbar.Snackbar.make(
            view,
            "Respons konsultasi tersedia! Case #CS-2026-0047",
            com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction("Lihat Sekarang") {
            val resIntent = android.content.Intent(this, ConsultationResponseActivity::class.java)
            startActivity(resIntent)
        }
        // Make it look critical/urgent
        snackbar.setActionTextColor(android.graphics.Color.YELLOW)
        snackbar.view.setBackgroundColor(android.graphics.Color.parseColor("#D32F2F"))
        snackbar.show()
    }
}