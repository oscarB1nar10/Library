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
)  : ViewModel(){

    var gender: Gender? = null
    private val mutableBookGenderRemoteDB: MutableLiveData<Gender> = MutableLiveData()
    private val mutableBookGenderLocalDB: MutableLiveData<Gender> = MutableLiveData()


    val addBookGenderResponseRemoteDB: LiveData<State<DocumentReference>> =
        Transformations.switchMap(mutableBookGenderRemoteDB) { gender ->
            liveData {
                bookGenderRepository.addGender(gender).collect {
                    emit(it)
                }
            }
        }

    val addBookGenderResponseLocalDB: LiveData<State<Gender>> =
        Transformations.switchMap(mutableBookGenderRemoteDB) { gender ->
            liveData {
                bookGenderRepository.addGenderToLocalDB(gender).collect {
                    emit(it)
                }
            }
        }

    fun addBookGenderRemoteDB(gender: Gender){
        mutableBookGenderRemoteDB.value = gender
    }

    fun addBookGenderLocalDB(){
        mutableBookGenderLocalDB.value = gender
    }
}