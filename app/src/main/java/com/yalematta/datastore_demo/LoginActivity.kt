package com.yalematta.datastore_demo

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import com.yalematta.datastore_demo.databinding.ActivityLoginBinding

const val USER_PREFERENCES_NAME = "user_preferences"
const val REMEMBER = "remember"
const val USERNAME = "username"

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPreferences: SharedPreferences = getSharedPreferences(USER_PREFERENCES_NAME, MODE_PRIVATE)
        val rememberMe = sharedPreferences.getBoolean(REMEMBER, false)

        if (rememberMe) {
            startActivity(Intent(this, WelcomeActivity::class.java))
        }

        binding.login.setOnClickListener {

            if (binding.remember.isChecked) {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(USERNAME, binding.username.text.toString())
                editor.putBoolean(REMEMBER, true)
                editor.apply()
            } else {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.clear().apply()
            }

            startActivity(Intent(this, WelcomeActivity::class.java))
        }

    }
}