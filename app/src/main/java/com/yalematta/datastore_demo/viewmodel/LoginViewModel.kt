package com.yalematta.datastore_demo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yalematta.datastore_demo.repository.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val dataStoreRepository: DataStoreRepository)
    : ViewModel() {

    val userPreferences = dataStoreRepository.readFromDataStore.asLiveData()

    fun saveUserPreferences(username: String, remember: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveToDataStore(username, remember)
        }
    }

    fun clearUserPreferences() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.clearDataStore()
        }
    }
}

class LoginViewModelFactory(
    private val dataStoreRepository: DataStoreRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(dataStoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}