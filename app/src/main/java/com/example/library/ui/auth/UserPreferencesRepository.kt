package com.example.library.ui.auth

import com.example.library.states.State
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    suspend fun updateUserToken(userToken: String)

    suspend fun removeUserToken()

    val userPreferencesFlow: Flow<State<UserPreferencesRepositoryImpl.UserPreferences>>
}