package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hsilhackathon.data.DatabaseProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PatientSearchActivity : AppCompatActivity() {

    private lateinit var adapter: PatientAdapter
    private lateinit var tvResultCount: TextView
    private lateinit var noResultState: LinearLayout
    private lateinit var rvSearchResults: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_search)

        val btnBack = findViewById<ImageView>(R.id.btnBackSearch)
        val etSearch = findViewById<EditText>(R.id.etSearchPatientPre)
        val btnAddNew = findViewById<MaterialButton>(R.id.btnAddNewPatient)
        tvResultCount = findViewById(R.id.tvSearchResultCount)
        noResultState = findViewById(R.id.noResultState)
        rvSearchResults = findViewById(R.id.rvSearchResults)

        btnBack.setOnClickListener { finish() }

        adapter = PatientAdapter(emptyList()) { patient ->
            // Show dialog asking for today's complaint before proceeding
            showKeluhanDialog(patient.nik, patient.namaLengkap)
        }

        rvSearchResults.layoutManager = LinearLayoutManager(this)
        rvSearchResults.adapter = adapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                if (query.isEmpty()) {
                    adapter.updateData(emptyList())
                    tvResultCount.text = "Ketik untuk mencari..."
                    noResultState.visibility = View.GONE
                    rvSearchResults.visibility = View.GONE
                } else {
                    searchPatients(query)
                }
            }
        })

        // New patient button → go to full form
        btnAddNew.setOnClickListener {
            val intent = Intent(this, QuestionnaireAwalActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun searchPatients(query: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = DatabaseProvider.getDatabase(this@PatientSearchActivity)
            val patients = db.patientDao().searchByNameOrNik(query)
            withContext(Dispatchers.Main) {
                adapter.updateData(patients)
                if (patients.isEmpty()) {
                    tvResultCount.text = "Tidak ditemukan"
                    noResultState.visibility = View.VISIBLE
                    rvSearchResults.visibility = View.GONE
                } else {
                    tvResultCount.text = "${patients.size} pasien ditemukan"
                    noResultState.visibility = View.GONE
                    rvSearchResults.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showKeluhanDialog(patientNik: String, patientName: String) {
        val editText = EditText(this).apply {
            hint = "Contoh: Gatal-gatal di tangan"
            setPadding(48, 32, 48, 32)
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE
            minLines = 2
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Keluhan Hari Ini")
            .setMessage("Pasien: $patientName\n\nApa keluhan pasien pada kunjungan ini?")
            .setView(editText)
            .setPositiveButton("Lanjut ke Kamera") { _, _ ->
                val keluhan = editText.text.toString().trim()
                val intent = Intent(this, ScanAIActivity::class.java)
                intent.putExtra("PATIENT_NIK", patientNik)
                intent.putExtra("NAMA_PASIEN", patientName)
                intent.putExtra("KELUHAN_HARI_INI", keluhan)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
