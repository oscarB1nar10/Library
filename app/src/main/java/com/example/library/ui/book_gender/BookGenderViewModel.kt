package com.example.library.ui.book_gender

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.library.models.Gender
import com.example.library.states.State
import kotlinx.coroutines.flow.collect

class BookGenderViewModel
@ViewModelInject
constructor(
    private val bookGenderRepository: BookGenderRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
)  : ViewModel() {

    var gender: Gender? = null
    private val saveBookGender: MutableLiveData<Gender> = MutableLiveData()
    private val getRemoteGenders:  MutableLiveData<Boolean> = MutableLiveData()
    private val synchronizeRemoteAndLocalGenders: MutableLiveData<List<Gender>> = MutableLiveData()
    private val removeGender: MutableLiveData<Gender> = MutableLiveData()

    val saveGenderResponse: LiveData<State<Boolean>> =
        Transformations.switchMap(saveBookGender) { gender ->
            liveData {
                bookGenderRepository.saveGender(gender).collect {
                    emit(it)
                }
            }
        }

    val getRemoteGendersResponse: LiveData<State<List<Gender>>> =
        liveData {
            bookGenderRepository.getRemoteGenders().collect {
                emit(it)
            }
        }

    val synchronizeRemoteAndLocalGendersResponse: LiveData<State<List<Gender>>> =
        Transformations.switchMap(synchronizeRemoteAndLocalGenders){genders ->
        liveData {
            bookGenderRepository.synchronizeRemoteAndLocalGenders(genders)
                .collect{
                emit(it)
            }
        }
    }

    val removeGenderResponse: LiveData<State<Boolean>> =
        Transformations.switchMap(removeGender){gender ->
            liveData(viewModelScope.coroutineContext) {
                bookGenderRepository.removeGender(gender)
                    .collect{
                        emit(it)
                    }
            }
        }


    fun saveGender(gender: Gender){
        saveBookGender.value = gender
    }

    fun saveBookGendersInLocalDB(genders: List<Gender>){
        synchronizeRemoteAndLocalGenders.value = genders
    }

    fun getRemoteGenders(fromRemote: Boolean = true){
        getRemoteGenders.value = fromRemote
    }

    //EXAMPLE PURPOSE
    fun removeBookGender(gender: Gender){
        removeGender.value = gender
    }

}