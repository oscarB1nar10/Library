package com.example.library.ui.book_gender

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.library.business.domain.model.GenderModel
import com.example.library.models.GenderCacheEntity
import com.example.library.business.domain.states.State
import com.example.library.ui.auth.UserPreferencesRepository
import com.example.library.util.checkIsNullOrEmpty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect

class BookGenderViewModel
@ViewModelInject
constructor(
    private val bookGenderRepository: BookGenderRepository,
    userPreferencesRepository: UserPreferencesRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var gender: GenderModel? = null
    lateinit var userToken: String

    private val saveBookGender: MutableLiveData<GenderModel> = MutableLiveData()
    private val updateBookGender: MutableLiveData<GenderModel> = MutableLiveData()
    private val synchronizeRemoteAndLocalGenders: MutableLiveData<List<GenderModel>> = MutableLiveData()
    private val removeGender: MutableLiveData<GenderModel> = MutableLiveData()


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
                bookGenderRepository.getRemoteGenderToUpdate(
                    genderId = gender.pk,
                    userToken = userToken
                )
                    .collect { state ->
                        when (state) {
                            is State.Loading -> {
                                //TODO (Maybe show progress bar)
                            }

                            is State.Success -> {
                                bookGenderRepository.updateGender(
                                    state.data.filter { it.pk == gender.pk }[0],
                                    gender,
                                    userToken ?: ""
                                ).collect {
                                    emit(it)
                                }
                            }

                            is State.Failed -> {
                                // TODO(Maybe handle error)
                            }
                        }
                    }
            }
        }

    val getRemoteGendersResponse: LiveData<State<List<GenderModel>>> =
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

    val synchronizeRemoteAndLocalGendersResponse: LiveData<State<List<GenderModel>>> =
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


    fun saveGender(gender: GenderModel) {
        if (isValidUserToken())
            saveBookGender.value = gender
    }

    fun updateGender() {
        if (isValidUserToken())
            updateBookGender.value = gender
    }

    fun saveBookGendersInLocalDB(genders: List<GenderModel>) {
        synchronizeRemoteAndLocalGenders.value = genders
    }

    fun removeBookGender(gender: GenderModel) {
        if (userToken.checkIsNullOrEmpty())
            removeGender.value = gender
    }

    private fun isValidUserToken(): Boolean = userToken.checkIsNullOrEmpty()

}