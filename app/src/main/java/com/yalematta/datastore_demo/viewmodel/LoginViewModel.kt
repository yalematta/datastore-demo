package com.yalematta.datastore_demo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yalematta.datastore_demo.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow.asLiveData()

    fun saveUserPreferences(username: String, remember: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateUsername(username)
            userPreferencesRepository.updateRemember(remember)
        }
    }

    fun clearUserPreferences() {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.clearDataStore()
        }
    }
}

class LoginViewModelFactory(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}