package com.example.library.ui.auth

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthActivityViewModel
@Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
): ViewModel(), LifecycleObserver {


    val userPreferences = userPreferencesRepository.userPreferencesFlow.asLiveData()

    fun updateUserToken(token: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateUserToken(token)
        }
    }
}