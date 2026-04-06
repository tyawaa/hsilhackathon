package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class LoginTenagaKesehatanActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_tenaga_kesehatan)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmailNakes)
        val etPassword = findViewById<EditText>(R.id.etPasswordNakes)
        val btnLogin = findViewById<AppCompatButton>(R.id.btnLoginTenagaKesehatan)
        val tvSignUp = findViewById<TextView>(R.id.tvSignUpTenagaKesehatan)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty()) {
                etEmail.error = "Email wajib diisi"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPassword.error = "Password wajib diisi"
                return@setOnClickListener
            }

            btnLogin.isEnabled = false

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        btnLogin.isEnabled = true
                        Toast.makeText(
                            this,
                            task.exception?.message ?: "Login gagal",
                            Toast.LENGTH_LONG
                        ).show()
                        return@addOnCompleteListener
                    }

                    val uid = auth.currentUser?.uid

                    if (uid == null) {
                        btnLogin.isEnabled = true
                        Toast.makeText(this, "UID user tidak ditemukan", Toast.LENGTH_LONG).show()
                        return@addOnCompleteListener
                    }

                    db.collection("users")
                        .document(uid)
                        .get(Source.SERVER)
                        .addOnSuccessListener { doc ->
                            btnLogin.isEnabled = true

                            if (!doc.exists()) {
                                Toast.makeText(
                                    this,
                                    "Profil user tidak ditemukan",
                                    Toast.LENGTH_LONG
                                ).show()
                                auth.signOut()
                                return@addOnSuccessListener
                            }

                            val role = doc.getString("role") ?: ""

                            val namaLengkap = doc.getString("namaLengkap")
                                ?: doc.getString("nama_lengkap")
                                ?: ""

                            val verified = when (val value = doc.get("verified")) {
                                is Boolean -> value
                                is String -> value.equals("true", ignoreCase = true)
                                else -> false
                            }

                            val isActive = when (val value = doc.get("isActive")) {
                                is Boolean -> value
                                is String -> value.equals("true", ignoreCase = true)
                                else -> false
                            }

                            Toast.makeText(
                                this,
                                "role=$role, verified=$verified, isActive=$isActive",
                                Toast.LENGTH_LONG
                            ).show()

                            if (role != "health_worker") {
                                Toast.makeText(
                                    this,
                                    "Akun ini bukan akun tenaga kesehatan",
                                    Toast.LENGTH_LONG
                                ).show()
                                auth.signOut()
                                return@addOnSuccessListener
                            }

                            if (!isActive) {
                                Toast.makeText(
                                    this,
                                    "Akun tidak aktif",
                                    Toast.LENGTH_LONG
                                ).show()
                                auth.signOut()
                                return@addOnSuccessListener
                            }

                            if (!verified) {
                                Toast.makeText(
                                    this,
                                    "Akun belum diverifikasi",
                                    Toast.LENGTH_LONG
                                ).show()
                                auth.signOut()
                                return@addOnSuccessListener
                            }

                            Toast.makeText(
                                this,
                                "Login berhasil, selamat datang $namaLengkap",
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            btnLogin.isEnabled = true
                            Toast.makeText(
                                this,
                                e.message ?: "Gagal mengambil data user dari server",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
        }

        tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterTenagaKesehatanActivity::class.java))
        }
    }
}