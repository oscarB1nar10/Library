package com.example.library.ui.auth

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthActivityViewModel
@ViewModelInject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {


    val userPreferences = userPreferencesRepository.userPreferencesFlow.asLiveData()

    fun updateUserToken(token: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateUserToken(token)
        }
    }
}