package com.example.library.ui.book_gender

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.library.models.Gender
import com.example.library.states.State
import com.example.library.ui.auth.UserPreferencesRepository
import com.example.library.util.checkIsNullOrEmpty
import kotlinx.coroutines.flow.collect

class BookGenderViewModel
@ViewModelInject
constructor(
    private val bookGenderRepository: BookGenderRepository,
    userPreferencesRepository: UserPreferencesRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var gender: Gender? = null
    lateinit var userToken: String

    private val saveBookGender: MutableLiveData<Gender> = MutableLiveData()
    private val updateBookGender: MutableLiveData<Gender> = MutableLiveData()
    private val synchronizeRemoteAndLocalGenders: MutableLiveData<List<Gender>> = MutableLiveData()
    private val removeGender: MutableLiveData<Gender> = MutableLiveData()


    private val userPreferences = userPreferencesRepository.userPreferencesFlow.asLiveData()

    val saveGenderResponse: LiveData<State<Boolean>> =
        Transformations.switchMap(saveBookGender) { gender ->
            liveData {
                bookGenderRepository.saveGender(gender, userToken ?: "").collect {
                    emit(it)
                }
            }
        }

    val updateGenderResponse: LiveData<State<Boolean>> =
        Transformations.switchMap(updateBookGender) { gender ->
            liveData {
                bookGenderRepository.updateGender(gender, userToken ?: "").collect {
                    emit(it)
                }
            }
        }

    val getRemoteGendersResponse: LiveData<State<List<Gender>>> =
        Transformations.switchMap(userPreferences) { preferences ->
            userToken = when (preferences) {
                is State.Success -> {
                    preferences.data.userToken
                }
                else -> ""
            }
            liveData {
                return@liveData bookGenderRepository.getRemoteGenders(userToken).collect {
                    emit(it)
                }
            }
        }

    val synchronizeRemoteAndLocalGendersResponse: LiveData<State<List<Gender>>> =
        Transformations.switchMap(synchronizeRemoteAndLocalGenders) { genders ->
            liveData {
                bookGenderRepository.synchronizeRemoteAndLocalGenders(genders)
                    .collect {
                        emit(it)
                    }
            }
        }

    val removeGenderResponse: LiveData<State<Boolean>> =
        Transformations.switchMap(removeGender) { gender ->
            liveData(viewModelScope.coroutineContext) {
                bookGenderRepository.removeGender(gender, userToken ?: "")
                    .collect {
                        emit(it)
                    }
            }
        }


    fun saveGender(gender: Gender) {
        if (isValidUserToken())
            saveBookGender.value = gender
    }

    fun updateGender() {
        if (isValidUserToken())
            updateBookGender.value = gender
    }

    fun saveBookGendersInLocalDB(genders: List<Gender>) {
        synchronizeRemoteAndLocalGenders.value = genders
    }

    fun removeBookGender(gender: Gender) {
        if (userToken.checkIsNullOrEmpty())
            removeGender.value = gender
    }

    private fun isValidUserToken(): Boolean = userToken.checkIsNullOrEmpty()

}