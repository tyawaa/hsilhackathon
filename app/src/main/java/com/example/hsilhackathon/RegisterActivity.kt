package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

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

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Format email tidak valid"
                return@setOnClickListener
            }

            if (password.length < 6) {
                etPassword.error = "Password minimal 6 karakter"
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

            btnRegister.isEnabled = false

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { authTask ->
                    if (authTask.isSuccessful) {
                        val uid = auth.currentUser?.uid

                        if (uid == null) {
                            btnRegister.isEnabled = true
                            Toast.makeText(this, "UID user tidak ditemukan", Toast.LENGTH_LONG).show()
                            return@addOnCompleteListener
                        }

                        val userMap = hashMapOf(
                            "uid" to uid,
                            "namaLengkap" to namaLengkap,
                            "email" to email,
                            "role" to "pasien"
                        )

                        db.collection("users")
                            .document(uid)
                            .set(userMap)
                            .addOnSuccessListener {
                                btnRegister.isEnabled = true
                                Toast.makeText(this, "Register berhasil", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                btnRegister.isEnabled = true
                                Toast.makeText(
                                    this,
                                    e.message ?: "Gagal menyimpan data user",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    } else {
                        btnRegister.isEnabled = true
                        Toast.makeText(
                            this,
                            authTask.exception?.message ?: "Register gagal",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}