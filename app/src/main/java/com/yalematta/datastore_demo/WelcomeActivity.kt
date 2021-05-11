package com.yalematta.datastore_demo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yalematta.datastore_demo.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPreferences: SharedPreferences =
            getSharedPreferences(USER_PREFERENCES_NAME, MODE_PRIVATE)
        val username = sharedPreferences.getString(USERNAME, "")

        binding.welcome.text =
            if (username != null && username.isNotEmpty()) {
                String.format(getString(R.string.welcome_user), username)
            } else {
                getString(R.string.welcome)
            }

        binding.logout.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear().apply()

            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}