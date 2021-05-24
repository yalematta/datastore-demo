package com.yalematta.datastore_demo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.yalematta.datastore_demo.databinding.ActivityWelcomeBinding
import com.yalematta.datastore_demo.repository.UserPreferencesRepository
import com.yalematta.datastore_demo.viewmodel.LoginViewModel
import com.yalematta.datastore_demo.viewmodel.LoginViewModelFactory

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this,
            LoginViewModelFactory(UserPreferencesRepository(userPreferencesStore))
        )
            .get(LoginViewModel::class.java)

        viewModel.userPreferencesFlow.observe(this, { userPreferences ->
            val username = userPreferences.username
            if (username.isNotEmpty()) {
                binding.welcome.text = String.format(getString(R.string.welcome_user), username)
            }
        })

        binding.logout.setOnClickListener {
            viewModel.clearUserPreferences()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}