package com.example.hsilhackathon

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.example.hsilhackathon.data.DatabaseProvider
import com.example.hsilhackathon.data.dao.NakesDao
import com.example.hsilhackathon.utils.PasswordUtils
import com.example.hsilhackathon.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginTenagaKesehatanActivity : AppCompatActivity() {

    private lateinit var nakesDao: NakesDao
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        sessionManager = SessionManager(this)
        
        // If already logged in, go directly to Dashboard
        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            return
        }
        
        setContentView(R.layout.activity_login_tenaga_kesehatan)

        nakesDao = DatabaseProvider.getDatabase(this).nakesDao()

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

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val nakes = nakesDao.getNakesByEmail(email)

                    withContext(Dispatchers.Main) {
                        btnLogin.isEnabled = true
                        
                        if (nakes == null) {
                            Toast.makeText(this@LoginTenagaKesehatanActivity, "Akun tidak ditemukan di database lokal", Toast.LENGTH_LONG).show()
                            return@withContext
                        }

                        val isValidPassword = PasswordUtils.verifyPassword(password, nakes.hashedPassword)
                        if (!isValidPassword) {
                            Toast.makeText(this@LoginTenagaKesehatanActivity, "Password salah", Toast.LENGTH_LONG).show()
                            return@withContext
                        }

                        // Success! Save session
                        sessionManager.login(nakes.email, nakes.namaLengkap)
                        Toast.makeText(this@LoginTenagaKesehatanActivity, "Login berhasil, selamat datang ${nakes.namaLengkap}", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this@LoginTenagaKesehatanActivity, DashboardActivity::class.java))
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        btnLogin.isEnabled = true
                        Toast.makeText(this@LoginTenagaKesehatanActivity, "Error DB: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterTenagaKesehatanActivity::class.java))
        }
    }
}