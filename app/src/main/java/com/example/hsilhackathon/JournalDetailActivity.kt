package com.example.hsilhackathon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip

class JournalDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_detail)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val tvDetailTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val tvDetailSourceDate = findViewById<TextView>(R.id.tvDetailSourceDate)
        val tvDetailContent = findViewById<TextView>(R.id.tvDetailContent)
        val chipCategory = findViewById<Chip>(R.id.chipCategory)
        val btnOpenWeb = findViewById<MaterialButton>(R.id.btnOpenWeb)

        btnBack.setOnClickListener { finish() }

        // Get data from intent
        val title = intent.getStringExtra("EXTRA_TITLE") ?: ""
        val source = intent.getStringExtra("EXTRA_SOURCE") ?: ""
        val date = intent.getStringExtra("EXTRA_DATE") ?: ""
        val category = intent.getStringExtra("EXTRA_CATEGORY") ?: ""
        val content = intent.getStringExtra("EXTRA_CONTENT") ?: "Konten tidak tersedia."
        val url = intent.getStringExtra("EXTRA_URL") ?: ""

        tvDetailTitle.text = title
        tvDetailSourceDate.text = "$source • $date"
        chipCategory.text = category
        tvDetailContent.text = content

        if (url.isNotEmpty()) {
            btnOpenWeb.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }
        } else {
            btnOpenWeb.isEnabled = false
        }
    }
}
