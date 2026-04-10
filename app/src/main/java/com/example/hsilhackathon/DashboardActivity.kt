package com.example.hsilhackathon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val btnGoScan = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnGoScan)
        btnGoScan.setOnClickListener {
            val intent = android.content.Intent(this, QuestionnaireAwalActivity::class.java)
            startActivity(intent)
        }

        val btnNavJurnal = findViewById<android.widget.LinearLayout>(R.id.btnNavJurnal)
        btnNavJurnal.setOnClickListener {
            val intent = android.content.Intent(this, JournalListActivity::class.java)
            startActivity(intent)
        }
    }
}