package com.yalematta.datastore_demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import androidx.lifecycle.ViewModelProvider
import com.yalematta.datastore_demo.data.UserPreferencesSerializer
import com.yalematta.datastore_demo.databinding.ActivityLoginBinding
import com.yalematta.datastore_demo.repository.UserPreferencesRepository
import com.yalematta.datastore_demo.viewmodel.LoginViewModel
import com.yalematta.datastore_demo.viewmodel.LoginViewModelFactory

const val USER_PREFERENCES_NAME = "user_preferences"
private const val DATA_STORE_FILE_NAME = "user_prefs.pb"

val Context.userPreferencesStore: DataStore<UserPreferences> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = UserPreferencesSerializer,
    produceMigrations = { context ->
        listOf(sharedPrefsMigration(context))
    }
)

fun sharedPrefsMigration(context: Context) = SharedPreferencesMigration(
    context, USER_PREFERENCES_NAME) { sharedPrefs: SharedPreferencesView, currentData: UserPreferences ->
    // Define the mapping from SharedPreferences to UserPreferences
    currentData
}

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    private var rememberMe = false
    private var luckyNumber = 0
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(UserPreferencesRepository(userPreferencesStore))
        ).get(LoginViewModel::class.java)

        viewModel.userPreferencesFlow.observe(this, { userPreferences ->
            rememberMe = userPreferences.remember
            username = userPreferences.username
            luckyNumber = userPreferences.luckyNumber
            if (rememberMe) {
                startActivity(Intent(this, WelcomeActivity::class.java))
            }
        })

        binding.login.setOnClickListener {
            if (binding.remember.isChecked) {
                val name = binding.username.text.toString()
                var number = luckyNumber
                if (binding.luckyNumber.text.toString().isNotEmpty()) {
                    number = binding.luckyNumber.text.toString().toInt()
                }
                viewModel.saveUserPreferences(true, name, number)
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