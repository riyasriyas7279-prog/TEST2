package com.example.travelpass

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileActivity : AppCompatActivity() {

    private lateinit var etProfileName: EditText
    private lateinit var rgDistanceUnit: RadioGroup
    private lateinit var rbKm: RadioButton
    private lateinit var rbMiles: RadioButton
    private lateinit var rgTempUnit: RadioGroup
    private lateinit var rbCelsius: RadioButton
    private lateinit var rbFahrenheit: RadioButton
    private lateinit var cbReceiveAlerts: CheckBox
    private lateinit var btnSaveProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        etProfileName = findViewById(R.id.etProfileName)
        rgDistanceUnit = findViewById(R.id.rgDistanceUnit)
        rbKm = findViewById(R.id.rbKm)
        rbMiles = findViewById(R.id.rbMiles)
        rgTempUnit = findViewById(R.id.rgTempUnit)
        rbCelsius = findViewById(R.id.rbCelsius)
        rbFahrenheit = findViewById(R.id.rbFahrenheit)
        cbReceiveAlerts = findViewById(R.id.cbReceiveAlerts)
        btnSaveProfile = findViewById(R.id.btnSaveProfile)

        loadExistingProfile()

        btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadExistingProfile() {
        val prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE)
        val name = prefs.getString(WelcomeActivity.KEY_NAME, "")
        val distanceUnit = prefs.getString(WelcomeActivity.KEY_DISTANCE_UNIT, "km")
        val tempUnit = prefs.getString(WelcomeActivity.KEY_TEMP_UNIT, "°C")
        val receiveAlerts = prefs.getBoolean(WelcomeActivity.KEY_RECEIVE_ALERTS, true)

        etProfileName.setText(name)

        if (distanceUnit == "miles") {
            rbMiles.isChecked = true
        } else {
            rbKm.isChecked = true
        }

        if (tempUnit == "°F") {
            rbFahrenheit.isChecked = true
        } else {
            rbCelsius.isChecked = true
        }

        cbReceiveAlerts.isChecked = receiveAlerts
    }

    private fun saveProfile() {
        val name = etProfileName.text.toString().trim()
        val distanceUnit = if (rbMiles.isChecked) "miles" else "km"
        val tempUnit = if (rbFahrenheit.isChecked) "°F" else "°C"
        val receiveAlerts = cbReceiveAlerts.isChecked

        val prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE)
        prefs.edit {
            putString(WelcomeActivity.KEY_NAME, name)
            putString(WelcomeActivity.KEY_DISTANCE_UNIT, distanceUnit)
            putString(WelcomeActivity.KEY_TEMP_UNIT, tempUnit)
            putBoolean(WelcomeActivity.KEY_RECEIVE_ALERTS, receiveAlerts)
        }

        Toast.makeText(this, getString(R.string.profile_saved), Toast.LENGTH_SHORT).show()
        finish()
    }
}