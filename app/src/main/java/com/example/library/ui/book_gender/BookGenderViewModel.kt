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

    private val mutableBookGender: MutableLiveData<Gender> = MutableLiveData()

    val addBookGenderResponse: LiveData<State<DocumentReference>> =
        Transformations.switchMap(mutableBookGender) { gender ->
            liveData {
                bookGenderRepository.addGender(gender).collect {
                    emit(it)
                }
            }
        }

    fun addBookGender(gender: Gender){
        mutableBookGender.value = gender
    }
}