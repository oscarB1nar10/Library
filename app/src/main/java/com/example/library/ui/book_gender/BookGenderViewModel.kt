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
    private val mutableSaveBookGender: MutableLiveData<Gender> = MutableLiveData()
    private val mutableBookGenderInLocalDB: MutableLiveData<List<Gender>> = MutableLiveData()
    private val mutableRemoveBookGender: MutableLiveData<Gender> = MutableLiveData()

    val addBookGenderResponse: LiveData<State<Boolean>> =
        Transformations.switchMap(mutableSaveBookGender) { gender ->
            liveData {
                bookGenderRepository.addGender(gender).collect {
                    emit(it)
                }
            }
        }

    val getBookGenderFirebaseUpdateResponse: LiveData<State<List<Gender>>> =
        liveData {
            bookGenderRepository.getGendersFromFirebaseDb().collect {
                emit(it)
            }
        }

    val getSaveBookGendersInLocalDdResponse: LiveData<State<List<Gender>>> =
        Transformations.switchMap(mutableBookGenderInLocalDB){genders ->
        liveData {
            bookGenderRepository.saveGenderInLocalDB(genders)
                .collect{
                emit(it)
            }
        }
    }

    val removeBookGenderResponse: LiveData<State<Boolean>> =
        Transformations.switchMap(mutableRemoveBookGender){gender ->
            liveData(viewModelScope.coroutineContext) {
                bookGenderRepository.removeBookGender(gender)
                    .collect{
                        emit(it)
                    }
            }
        }


    fun saveBookGender(gender: Gender){
        mutableSaveBookGender.value = gender
    }

    fun saveBookGendersInLocalDB(genders: List<Gender>){
        mutableBookGenderInLocalDB.value = genders
    }

    //EXAMPLE PURPOSE
    fun removeBookGender(gender: Gender){
        mutableRemoveBookGender.value = gender
    }

}