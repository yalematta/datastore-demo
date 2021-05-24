package com.yalematta.datastore_demo.repository

import android.util.Log
import androidx.datastore.core.DataStore
import com.yalematta.datastore_demo.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException


class UserPreferencesRepository(private val userPreferencesStore: DataStore<UserPreferences>) {

    private val TAG: String = "UserPreferencesRepo"

    val userPreferencesFlow: Flow<UserPreferences> = userPreferencesStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading sort order preferences.", exception)
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun updateUsername(username: String) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setUsername(username).build()
        }
    }

    suspend fun updateRemember(remember: Boolean) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setRemember(remember).build()
        }
    }

    suspend fun clearDataStore() {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().clear().build()
        }
    }

    suspend fun clearUsername() {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().clearUsername().build()
        }
    }
}
