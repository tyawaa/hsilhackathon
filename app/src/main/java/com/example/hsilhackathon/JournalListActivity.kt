package com.example.hsilhackathon

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.hsilhackathon.data.AppDatabase
import com.example.hsilhackathon.data.dao.JournalDao
import com.example.hsilhackathon.data.entity.JournalEntity
import com.example.hsilhackathon.utils.PasswordUtils
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JournalListActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var journalDao: JournalDao
    private lateinit var adapter: JournalAdapter
    private lateinit var tvEmptyState: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var currentCategoryFilter: String = "Semua"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_list)

        // Initialize Database
        val encryptionKey = PasswordUtils.getEncryptionKey(this)
        db = AppDatabase.getDatabase(this, encryptionKey)
        journalDao = db.journalDao()

        // Init Views
        tvEmptyState = findViewById(R.id.tvEmptyState)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        val rvJournals = findViewById<RecyclerView>(R.id.rvJournals)
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroupCategory)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        // Setup RecyclerView
        adapter = JournalAdapter(emptyList())
        rvJournals.layoutManager = LinearLayoutManager(this)
        rvJournals.adapter = adapter

        // Setup Listeners
        btnBack.setOnClickListener { finish() }

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                currentCategoryFilter = "Semua" // default if nothing selected
            } else {
                when (checkedIds.first()) {
                    R.id.chipAll -> currentCategoryFilter = "Semua"
                    R.id.chipSkabies -> currentCategoryFilter = "Skabies"
                    R.id.chipKusta -> currentCategoryFilter = "Kusta"
                    R.id.chipFrambusia -> currentCategoryFilter = "Frambusia"
                    R.id.chipUmum -> currentCategoryFilter = "Panduan Umum"
                }
            }
            loadJournals()
        }

        swipeRefreshLayout.setOnRefreshListener {
            simulateSyncFromWifi()
        }

        // Initialize Data (Mocks some offline data if DB is empty)
        lifecycleScope.launch {
            checkAndPopulateInitialData()
            loadJournals()
        }
    }

    private fun loadJournals() {
        lifecycleScope.launch {
            val journals = withContext(Dispatchers.IO) {
                if (currentCategoryFilter == "Semua") {
                    journalDao.getAllJournals()
                } else {
                    journalDao.getJournalsByCategory(currentCategoryFilter)
                }
            }
            
            adapter.updateData(journals)
            
            if (journals.isEmpty()) {
                tvEmptyState.visibility = View.VISIBLE
            } else {
                tvEmptyState.visibility = View.GONE
            }
        }
    }

    private suspend fun checkAndPopulateInitialData() {
        withContext(Dispatchers.IO) {
            val existing = journalDao.getAllJournals()
            if (existing.isEmpty()) {
                val initialData = listOf(
                    JournalEntity("j1", "Panduan Deteksi Dini Skabies Berbasis AI", "Kemenkes RI", "12 Jan 2026", "Skabies", "https://kemkes.go.id"),
                    JournalEntity("j2", "Waspada Kusta di Daerah Tropis", "Jurnal Kedokteran Tropis", "05 Jan 2026", "Kusta", "https://kemkes.go.id"),
                    JournalEntity("j3", "Tata Laksana Pengobatan Frambusia", "WHO Indonesia", "10 Feb 2026", "Frambusia", "https://who.int/indonesia")
                )
                journalDao.insertJournals(initialData)
            }
        }
    }

    private fun simulateSyncFromWifi() {
        // In a real app, this checks network state:
        // val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Then updates DB from an API.
        
        Toast.makeText(this, "Menyinkronkan dari server...", Toast.LENGTH_SHORT).show()
        
        // Simulating internet delay
        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val syncData = listOf(
                        JournalEntity("s1", "Update Tatalaksana Skabies Komprehensif", "Ikatan Dokter Indonesia", "09 Apr 2026", "Skabies", "https://kemkes.go.id", isFromSync = true),
                        JournalEntity("s2", "Edukasi Masyarakat Tentang Bahaya Kusta", "Kemenkes RI", "10 Apr 2026", "Kusta", "https://kemkes.go.id", isFromSync = true)
                    )
                    journalDao.insertJournals(syncData)
                }
                loadJournals()
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@JournalListActivity, "Sinkronisasi Berhasil (2 data baru)", Toast.LENGTH_SHORT).show()
            }
        }, 1500)
    }
}
