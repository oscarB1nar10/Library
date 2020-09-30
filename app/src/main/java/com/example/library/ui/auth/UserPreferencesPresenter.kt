package com.example.library.ui.auth

import javax.inject.Inject

class UserPreferencesPresenter
@Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
){

    suspend fun removeUserToken(){
        userPreferencesRepository.removeUserToken()
    }
}