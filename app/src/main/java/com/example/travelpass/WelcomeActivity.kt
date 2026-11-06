package com.example.travelpass

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WelcomeActivity : AppCompatActivity() {

    private lateinit var tvGreeting: TextView
    private lateinit var tvUsernameDisplay: TextView
    private lateinit var tvNameDisplay: TextView
    private lateinit var tvDistanceUnitDisplay: TextView
    private lateinit var tvTempUnitDisplay: TextView
    private lateinit var tvAlertsDisplay: TextView
    private lateinit var btnEditProfile: Button

    private var currentUsername: String = ""

    companion object {
        const val KEY_NAME = "profile_name"
        const val KEY_DISTANCE_UNIT = "distance_unit"
        const val KEY_TEMP_UNIT = "temp_unit"
        const val KEY_RECEIVE_ALERTS = "receive_alerts"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.welcome_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        tvGreeting = findViewById(R.id.tvGreeting)
        tvUsernameDisplay = findViewById(R.id.tvUsernameDisplay)
        tvNameDisplay = findViewById(R.id.tvNameDisplay)
        tvDistanceUnitDisplay = findViewById(R.id.tvDistanceUnitDisplay)
        tvTempUnitDisplay = findViewById(R.id.tvTempUnitDisplay)
        tvAlertsDisplay = findViewById(R.id.tvAlertsDisplay)
        btnEditProfile = findViewById(R.id.btnEditProfile)

        currentUsername = intent.getStringExtra(MainActivity.EXTRA_USERNAME) ?: ""

        if (currentUsername.isEmpty()) {
            val prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE)
            currentUsername = prefs.getString(MainActivity.KEY_SAVED_USERNAME, "") ?: "Traveller"
        }

        btnEditProfile.setOnClickListener {
            openProfileScreen()
        }

        loadProfileAndDisplay()
    }

    override fun onResume() {
        super.onResume()
        loadProfileAndDisplay()
    }

    private fun loadProfileAndDisplay() {
        val prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE)
        val profileName = prefs.getString(KEY_NAME, "") ?: ""
        val distanceUnit = prefs.getString(KEY_DISTANCE_UNIT, "km") ?: "km"
        val tempUnit = prefs.getString(KEY_TEMP_UNIT, "°C") ?: "°C"
        val receiveAlerts = prefs.getBoolean(KEY_RECEIVE_ALERTS, true)

        val displayName = profileName.ifEmpty { currentUsername }
        tvGreeting.text = getString(R.string.greeting_user, displayName)

        tvUsernameDisplay.text = currentUsername.ifEmpty { getString(R.string.not_set) }
        tvNameDisplay.text = profileName.ifEmpty { getString(R.string.not_set) }
        tvDistanceUnitDisplay.text = distanceUnit
        tvTempUnitDisplay.text = tempUnit
        tvAlertsDisplay.text = if (receiveAlerts) getString(R.string.yes) else getString(R.string.no)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit_profile, R.id.menu_settings -> {
                openProfileScreen()
                true
            }
            R.id.menu_logout -> {
                logoutUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openProfileScreen() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun logoutUser() {
        // Clear saved login data
        val prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE)
        prefs.edit {
            putBoolean(MainActivity.KEY_REMEMBER_ME, false)
            putString(MainActivity.KEY_SAVED_USERNAME, "")
        }

        Toast.makeText(this, getString(R.string.logged_out_msg), Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}