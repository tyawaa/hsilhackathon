package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etNamaLengkap = findViewById<EditText>(R.id.etNamaLengkap)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etKonfirmasiPassword = findViewById<EditText>(R.id.etKonfirmasiPassword)
        val btnRegister = findViewById<AppCompatButton>(R.id.btnRegister)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)

        btnRegister.setOnClickListener {
            val namaLengkap = etNamaLengkap.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val konfirmasiPassword = etKonfirmasiPassword.text.toString().trim()

            if (namaLengkap.isEmpty()) {
                etNamaLengkap.error = "Nama lengkap wajib diisi"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                etEmail.error = "Email wajib diisi"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPassword.error = "Password wajib diisi"
                return@setOnClickListener
            }

            if (konfirmasiPassword.isEmpty()) {
                etKonfirmasiPassword.error = "Konfirmasi password wajib diisi"
                return@setOnClickListener
            }

            if (password != konfirmasiPassword) {
                etKonfirmasiPassword.error = "Password tidak sama"
                return@setOnClickListener
            }

            Toast.makeText(this, "Register berhasil untuk $namaLengkap", Toast.LENGTH_SHORT).show()
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}