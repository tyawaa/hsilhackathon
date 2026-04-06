package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class RegisterTenagaKesehatanActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_tenaga_kesehatan)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val etKodeFasilitas = findViewById<EditText>(R.id.etKodeFasilitas)
        val etNamaLengkap = findViewById<EditText>(R.id.etNamaLengkap)
        val etIdPetugas = findViewById<EditText>(R.id.etIdPetugas)
        val actvJabatan = findViewById<AutoCompleteTextView>(R.id.actvJabatan)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etKonfirmasiPassword = findViewById<EditText>(R.id.etKonfirmasiPassword)
        val btnRegister = findViewById<AppCompatButton>(R.id.btnRegisterTenagaKesehatan)
        val tvLogin = findViewById<TextView>(R.id.tvLoginTenagaKesehatan)

        val daftarJabatan = listOf(
            "Dokter Umum",
            "Dokter Spesialis",
            "Perawat",
            "Bidan",
            "Apoteker",
            "Petugas Puskesmas",
            "Tenaga Kesehatan Lainnya"
        )

        val adapterJabatan = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            daftarJabatan
        )
        actvJabatan.setAdapter(adapterJabatan)

        btnRegister.setOnClickListener {
            val kodeFasilitas = etKodeFasilitas.text.toString().trim().uppercase()
            val namaLengkap = etNamaLengkap.text.toString().trim()
            val idPetugas = etIdPetugas.text.toString().trim().uppercase()
            val jabatan = actvJabatan.text.toString().trim()
            val email = etEmail.text.toString().trim().lowercase()
            val password = etPassword.text.toString().trim()
            val konfirmasiPassword = etKonfirmasiPassword.text.toString().trim()

            if (kodeFasilitas.isEmpty()) {
                etKodeFasilitas.error = "Kode fasilitas wajib diisi"
                return@setOnClickListener
            }

            if (namaLengkap.isEmpty()) {
                etNamaLengkap.error = "Nama lengkap wajib diisi"
                return@setOnClickListener
            }

            if (idPetugas.isEmpty()) {
                etIdPetugas.error = "ID petugas wajib diisi"
                return@setOnClickListener
            }

            if (jabatan.isEmpty()) {
                actvJabatan.error = "Jabatan wajib dipilih"
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

            if (password.isEmpty()) {
                etPassword.error = "Password wajib diisi"
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
                    if (!authTask.isSuccessful) {
                        btnRegister.isEnabled = true
                        Toast.makeText(
                            this,
                            authTask.exception?.message ?: "Register gagal",
                            Toast.LENGTH_LONG
                        ).show()
                        return@addOnCompleteListener
                    }

                    val uid = auth.currentUser?.uid
                    if (uid == null) {
                        btnRegister.isEnabled = true
                        Toast.makeText(this, "UID user tidak ditemukan", Toast.LENGTH_LONG).show()
                        return@addOnCompleteListener
                    }

                    val userMap = hashMapOf(
                        "uid" to uid,
                        "role" to "health_worker",
                        "namaLengkap" to namaLengkap,
                        "email" to email,
                        "facilityCode" to kodeFasilitas,
                        "staffId" to idPetugas,
                        "jabatan" to jabatan,
                        "verified" to false,
                        "isActive" to true,
                        "createdAt" to FieldValue.serverTimestamp()
                    )

                    db.collection("users")
                        .document(uid)
                        .set(userMap)
                        .addOnSuccessListener {
                            auth.signOut()

                            btnRegister.isEnabled = true
                            Toast.makeText(
                                this,
                                "Register berhasil. Akun menunggu verifikasi.",
                                Toast.LENGTH_LONG
                            ).show()

                            startActivity(
                                Intent(this, LoginTenagaKesehatanActivity::class.java)
                            )
                            finish()
                        }
                        .addOnFailureListener { e ->
                            auth.currentUser?.delete()

                            btnRegister.isEnabled = true
                            Toast.makeText(
                                this,
                                e.message ?: "Gagal menyimpan data tenaga kesehatan",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginTenagaKesehatanActivity::class.java))
            finish()
        }
    }
}