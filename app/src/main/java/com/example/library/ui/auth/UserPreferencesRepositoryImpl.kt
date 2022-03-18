package com.example.library.ui.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.library.business.domain.states.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepositoryImpl
@Inject
constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    companion object {
        const val DATA_STORE_NAME = "user_preferences"
        const val PREFERENCE_USER_TOKE = "user_token"
        const val USER_TOKEN_EMPTY = ""
    }

    override val userPreferencesFlow: Flow<State<UserPreferences>> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            // Get user token
            val userToken = preferences[PreferencesKeys.USER_TOKEN] ?: ""
            State.Success(UserPreferences(userToken = userToken))
        }

    override suspend fun updateUserToken(userToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_TOKEN] = userToken
        }
    }

    override suspend fun removeUserToken() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_TOKEN] = USER_TOKEN_EMPTY
        }
    }

    data class UserPreferences(
        val userToken: String
    )

    private object PreferencesKeys {
        val USER_TOKEN = stringPreferencesKey(PREFERENCE_USER_TOKE)
    }
}