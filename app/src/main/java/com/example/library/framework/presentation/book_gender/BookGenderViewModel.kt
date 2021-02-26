package com.example.library.framework.presentation.book_gender

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import com.example.library.business.interactors.bookgender.BookGenderInteractors
import com.example.library.ui.auth.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class BookGenderViewModel
@ViewModelInject
constructor(
        private val bookGenderInteractors: BookGenderInteractors,
        userPreferencesRepository: UserPreferencesRepository,
        @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var gender: GenderModel? = null

    private val saveBookGender: MutableLiveData<GenderModel> = MutableLiveData()
    private val updateBookGender: MutableLiveData<GenderModel> = MutableLiveData()
    private val synchronizeRemoteAndLocalGenders: MutableLiveData<List<GenderModel>> =
            MutableLiveData()
    private val removeGender: MutableLiveData<GenderModel> = MutableLiveData()
    private val getBookGendersFromCache: MutableLiveData<Boolean> = MutableLiveData()


    private val userPreferences = userPreferencesRepository.userPreferencesFlow.asLiveData()

    val getRemoteGendersFromRemoteResponse: LiveData<State<List<GenderModel>>> =
            Transformations.switchMap(userPreferences) { _ ->
                liveData(Dispatchers.IO) {
                    bookGenderInteractors.getBookGendersFromServer.getBookGendersFromServer().collect {
                        emit(it)
                    }
                }
            }

    val synchronizeRemoteAndLocalGendersResponse: LiveData<State<List<GenderModel>>> =
            Transformations.switchMap(synchronizeRemoteAndLocalGenders) { genders ->
                liveData(Dispatchers.IO) {
                    bookGenderInteractors.synchronizeRemoteAndLocalGenders.synchronizeRemoteAndLocalGenders()
                            .collect {
                                emit(it)
                            }
                }
            }

    /**
     * This function will observe for a stream of data coming from data base
     * when the table for book genders changes
     */
    val getBookGendersFromCacheResponse: LiveData<State<List<GenderModel>>> =
            Transformations.switchMap(getBookGendersFromCache) {
                liveData(Dispatchers.IO) {
                    bookGenderInteractors.getBookGendersFromCache.getBookGendersFromCache().collect {
                        emit(it)
                    }
                }
            }

    val saveGenderResponse: LiveData<State<String>> =
            Transformations.switchMap(saveBookGender) { gender ->
                liveData(Dispatchers.IO) {
                    bookGenderInteractors.saveNewBookGender.saveBookGender(gender).collect {
                        emit(it)
                    }
                }
            }

    val updateGenderResponse: LiveData<State<String>> =
            Transformations.switchMap(updateBookGender) { gender ->
                liveData(Dispatchers.IO) {
                    bookGenderInteractors.updateGender.updateGender(gender)
                            .collect {
                                // get genders updated from cache
                                getBookGendersFromCache()
                                emit(it)
                            }

                }
            }

    val removeGenderResponse: LiveData<State<String>> =
            Transformations.switchMap(removeGender) { gender ->
                liveData(viewModelScope.coroutineContext) {
                    bookGenderInteractors.removeGender.removeGender(gender)
                            .collect {
                                getBookGendersFromCache()
                                emit(it)
                            }
                }
            }

    /**
     * the [getBookGendersFromCache] is a fired that should change it's state to fire
     * the event to get the book genders from cache
     */
    private fun getBookGendersFromCache() {
        if (getBookGendersFromCache.value == true)
            getBookGendersFromCache.postValue(false)
        else
            getBookGendersFromCache.postValue(true)
    }

    fun saveGender(gender: GenderModel) {
        saveBookGender.value = gender
    }

    fun updateGender() {
        updateBookGender.value = gender
    }

    fun saveBookGendersInLocalDB(genders: List<GenderModel>) {
        synchronizeRemoteAndLocalGenders.value = genders
    }

    fun removeBookGender(gender: GenderModel) {
        removeGender.value = gender
    }


}