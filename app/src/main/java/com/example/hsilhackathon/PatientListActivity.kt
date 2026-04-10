package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsilhackathon.data.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PatientListActivity : AppCompatActivity() {

    private lateinit var adapter: PatientAdapter
    private lateinit var rvPatients: RecyclerView
    private lateinit var tvPatientCount: TextView
    private lateinit var emptyState: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_list)

        rvPatients = findViewById(R.id.rvPatients)
        tvPatientCount = findViewById(R.id.tvPatientCount)
        emptyState = findViewById(R.id.emptyState)
        val etSearch = findViewById<EditText>(R.id.etSearchPatient)

        adapter = PatientAdapter(emptyList()) { patient ->
            val intent = Intent(this, PatientDetailActivity::class.java)
            intent.putExtra("PATIENT_NIK", patient.nik)
            startActivity(intent)
        }

        rvPatients.layoutManager = LinearLayoutManager(this)
        rvPatients.adapter = adapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchPatients(s.toString().trim())
            }
        })

        // Nav Home button
        val btnNavHome = findViewById<View>(R.id.btnNavHome)
        btnNavHome.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadAllPatients()
    }

    private fun loadAllPatients() {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = DatabaseProvider.getDatabase(this@PatientListActivity)
            val patients = db.patientDao().getAllPatients()
            withContext(Dispatchers.Main) {
                adapter.updateData(patients)
                tvPatientCount.text = "${patients.size} Pasien Terdaftar"
                emptyState.visibility = if (patients.isEmpty()) View.VISIBLE else View.GONE
                rvPatients.visibility = if (patients.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    private fun searchPatients(query: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = DatabaseProvider.getDatabase(this@PatientListActivity)
            val patients = if (query.isEmpty()) {
                db.patientDao().getAllPatients()
            } else {
                db.patientDao().searchByNameOrNik(query)
            }
            withContext(Dispatchers.Main) {
                adapter.updateData(patients)
                tvPatientCount.text = "${patients.size} Pasien Ditemukan"
                emptyState.visibility = if (patients.isEmpty()) View.VISIBLE else View.GONE
                rvPatients.visibility = if (patients.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }
}
