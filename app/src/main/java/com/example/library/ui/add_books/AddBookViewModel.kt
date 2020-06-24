package com.example.library.ui.add_books

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.library.models.Book
import com.example.library.states.State
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class AddBookViewModel
@Inject constructor(
    private val addBookRepository: AddBookRepository
): ViewModel(){

    private val mutableBook: MutableLiveData<Book> = MutableLiveData()

    val addBookResponse: LiveData<State<DocumentReference>> = switchMap(mutableBook){book ->
        liveData{
            addBookRepository.addBook(book).collect {
                emit(it)
            }
        }

    }

    fun addBook(book: Book){
        mutableBook.value = book
    }

}