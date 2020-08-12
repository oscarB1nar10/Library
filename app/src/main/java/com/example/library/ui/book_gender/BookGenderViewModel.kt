package com.example.library.ui.book_gender

import androidx.lifecycle.*
import com.example.library.models.Gender
import com.example.library.states.State
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class BookGenderViewModel
@Inject
constructor(
    private val bookGenderRepository: BookGenderRepository
)  : ViewModel() {

    var gender: Gender? = null
    private val mutableSaveBookGender: MutableLiveData<Gender> = MutableLiveData()
    private val mutableBookGenderInLocalDB: MutableLiveData<List<Gender>> = MutableLiveData()

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
            bookGenderRepository.saveGenderInLocalDB(genders).collect{
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

}