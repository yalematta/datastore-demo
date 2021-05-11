package com.yalematta.datastore_demo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.yalematta.datastore_demo.databinding.ActivityLoginBinding
import com.yalematta.datastore_demo.repository.DataStoreRepository
import com.yalematta.datastore_demo.viewmodel.LoginViewModel
import com.yalematta.datastore_demo.viewmodel.LoginViewModelFactory


private const val USER_PREFERENCES_NAME = "user_preferences"

val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    private var rememberMe = false
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this,
            LoginViewModelFactory(DataStoreRepository(dataStore)))
                .get(LoginViewModel::class.java)

        viewModel.userPreferences.observe(this, { userPreferences ->
            rememberMe = userPreferences.remember
            username = userPreferences.username
            if (rememberMe) {
                startActivity(Intent(this, WelcomeActivity::class.java))
            }
        })

        binding.login.setOnClickListener {
            if (binding.remember.isChecked) {
                val name = binding.username.text.toString()
                viewModel.saveUserPreferences(name, true)
            }
            startActivity(Intent(this, WelcomeActivity::class.java))
        }

        binding.remember.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (!compoundButton.isChecked) {
                viewModel.clearUserPreferences()
            }
        }

    }
}