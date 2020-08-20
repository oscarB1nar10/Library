package com.example.library.ui.add_books

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap
import com.example.library.models.Book
import com.example.library.states.State
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.collect


class AddBookViewModel
@ViewModelInject constructor(
    private val addBookRepository: AddBookRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
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