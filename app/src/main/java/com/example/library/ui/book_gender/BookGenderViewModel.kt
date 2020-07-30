package com.example.library.ui.book_gender

import androidx.lifecycle.*
import com.example.library.models.Gender
import com.example.library.states.State
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class BookGenderViewModel
@Inject
constructor(
    private val bookGenderRepository: BookGenderRepository
)  : ViewModel() {

    var gender: Gender? = null
    private val mutableSaveBookGender: MutableLiveData<Gender> = MutableLiveData()
    private val mutableGetBookGender: MutableLiveData<Boolean> = MutableLiveData()

    val addBookGenderResponse: LiveData<State<DocumentReference>> =
        Transformations.switchMap(mutableSaveBookGender) { gender ->
            liveData {
                bookGenderRepository.addGender(gender).collect {
                    emit(it)
                }
            }
        }

    val getBookGenderResponse: LiveData<State<List<Gender>>> =
        liveData {
            bookGenderRepository.getGender().collect {
                emit(it)
            }
        }

    val getBookGenderFirebaseUpdateResponse: LiveData<State<List<Gender>>> =
        liveData {
            bookGenderRepository.getGendersFromFirebaseDb().collect {
                emit(it)
            }
        }


    fun saveBookGender(gender: Gender){
        mutableSaveBookGender.value = gender
    }

    /*fun getBookGenders(){
        //TODO("Replace code to avoid pass a value as default")
        mutableGetBookGender.value = true
    }*/
}