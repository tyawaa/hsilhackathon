package com.example.hsilhackathon

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsilhackathon.data.AppDatabase
import kotlinx.coroutines.launch

class ConsultationHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultation_history)

        val btnBack = findViewById<ImageView>(R.id.btnBackHistory)
        btnBack.setOnClickListener { finish() }

        val rvConsultationHistory = findViewById<RecyclerView>(R.id.rvConsultationHistory)
        val tvEmptyState = findViewById<TextView>(R.id.tvEmptyState)

        rvConsultationHistory.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            val appDb = AppDatabase.getDatabase(
                this@ConsultationHistoryActivity,
                "dummy_key_123".toByteArray()
            )
            
            val historyList = appDb.consultationDao().getAllConsultations()
            
            if (historyList.isEmpty()) {
                tvEmptyState.visibility = View.VISIBLE
                rvConsultationHistory.visibility = View.GONE
            } else {
                tvEmptyState.visibility = View.GONE
                rvConsultationHistory.visibility = View.VISIBLE
                rvConsultationHistory.adapter = ConsultationHistoryAdapter(historyList)
            }
        }
    }
}
