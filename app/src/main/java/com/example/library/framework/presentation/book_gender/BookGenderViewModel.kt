package com.example.library.framework.presentation.book_gender

import androidx.lifecycle.*
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import com.example.library.business.interactors.bookgender.BookGenderInteractors
import com.example.library.ui.auth.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class BookGenderViewModel
@Inject
constructor(
    private val bookGenderInteractors: BookGenderInteractors,
    userPreferencesRepository: UserPreferencesRepository,
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
        Transformations.switchMap(userPreferences) {
            liveData(Dispatchers.Default) {
                emit(State.Loading())
                val getBookGendersFromServerResponse =
                    bookGenderInteractors.getBookGendersFromServer.getBookGendersFromServer()
                emit(getBookGendersFromServerResponse)
            }
        }

    val synchronizeRemoteAndLocalGendersResponse: LiveData<State<List<GenderModel>>> =
        Transformations.switchMap(synchronizeRemoteAndLocalGenders) { genders ->
            liveData(Dispatchers.Default) {
                emit(State.Loading())
                val synchronizeRemoteAndLocalGendersResponse =
                    bookGenderInteractors.synchronizeRemoteAndLocalGenders.synchronizeRemoteAndLocalGenders()
                emit(synchronizeRemoteAndLocalGendersResponse)
            }
        }

    /**
     * This function will observe for a stream of data coming from data base
     * when the table for book genders changes
     */
    val getBookGendersFromCacheResponse: LiveData<State<List<GenderModel>>> =
        Transformations.switchMap(getBookGendersFromCache) {
            liveData(Dispatchers.IO) {
                emit(State.Loading())
                bookGenderInteractors.getBookGendersFromCache.getBookGendersFromCache().collect {
                    emit(it)
                }
            }
        }

    val saveGenderResponse: LiveData<State<String>> =
        Transformations.switchMap(saveBookGender) { gender ->
            liveData(Dispatchers.IO) {
                emit(State.Loading())
                val saveNewBookGenderResponse =
                    bookGenderInteractors.saveNewBookGender.saveBookGender(gender)
                getBookGendersFromCache()
                emit(saveNewBookGenderResponse)
            }
        }

    val updateGenderResponse: LiveData<State<String>> =
        Transformations.switchMap(updateBookGender) { gender ->
            liveData(Dispatchers.IO) {
                emit(State.Loading())
                val updateGenderResponse = bookGenderInteractors.updateGender.updateGender(gender)
                // get genders updated from cache
                getBookGendersFromCache()
                emit(updateGenderResponse)
            }
        }

    val removeGenderResponse: LiveData<State<String>> =
        Transformations.switchMap(removeGender) { gender ->
            liveData(viewModelScope.coroutineContext) {
                emit(State.Loading())
                val removeGenderResponse = bookGenderInteractors.removeGender.removeGender(gender)
                getBookGendersFromCache()
                emit(removeGenderResponse)

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