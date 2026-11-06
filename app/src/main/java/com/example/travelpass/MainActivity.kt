package com.example.travelpass

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var cbRememberMe: CheckBox
    private lateinit var btnLogin: Button

    companion object {
        const val PREFS_NAME = "TravelPassPrefs"
        const val KEY_REMEMBER_ME = "remember_me"
        const val KEY_SAVED_USERNAME = "saved_username"
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        cbRememberMe = findViewById(R.id.cbRememberMe)
        btnLogin = findViewById(R.id.btnLogin)

        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val rememberMe = prefs.getBoolean(KEY_REMEMBER_ME, false)
        if (rememberMe) {
            val savedUsername = prefs.getString(KEY_SAVED_USERNAME, "")
            etUsername.setText(savedUsername)
            cbRememberMe.isChecked = true
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save or clear Remember Me preferences
            prefs.edit {
                if (cbRememberMe.isChecked) {
                    putBoolean(KEY_REMEMBER_ME, true)
                    putString(KEY_SAVED_USERNAME, username)
                } else {
                    putBoolean(KEY_REMEMBER_ME, false)
                    putString(KEY_SAVED_USERNAME, "")
                }
            }

            // Navigate to WelcomeActivity passing username via Intent
            val intent = Intent(this, WelcomeActivity::class.java).apply {
                putExtra(EXTRA_USERNAME, username)
            }
            startActivity(intent)
            finish()
        }
    }
}