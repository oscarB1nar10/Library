package com.example.library.ui.auth

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.example.library.business.domain.states.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepositoryImpl
@Inject
constructor(
    val context: Context
): UserPreferencesRepository {

    companion object{
        const val DATA_STORE_NAME = "user_preferences"
        const val PREFERENCE_USER_TOKE = "user_token"
        const val USER_TOKEN_EMPTY = ""
    }

    private val dataStore: DataStore<Preferences> =
        context.createDataStore(name = DATA_STORE_NAME)

    override val userPreferencesFlow: Flow<State<UserPreferences>> = dataStore.data
        .catch {exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map {preferences ->
            // Get user token
            val userToken = preferences[PreferencesKeys.USER_TOKEN]?:""
            State.Success(UserPreferences(userToken = userToken))
        }

    override suspend fun updateUserToken(userToken: String){
        dataStore.edit {preferences ->
            preferences[PreferencesKeys.USER_TOKEN] = userToken
        }
    }

    override suspend fun removeUserToken() {
        dataStore.edit {preferences ->
            preferences[PreferencesKeys.USER_TOKEN] = USER_TOKEN_EMPTY
        }
    }

    data class UserPreferences(
        val userToken: String)

    private object PreferencesKeys{
        val USER_TOKEN = preferencesKey<String>(PREFERENCE_USER_TOKE)
    }
}