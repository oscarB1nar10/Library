package com.example.library.ui.add_books

import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap
import com.example.library.business.domain.states.State
import com.example.library.models.Book
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel
@Inject
constructor(
    private val addBookRepository: AddBookRepository,
) : ViewModel(), LifecycleObserver {

    private val mutableBook: MutableLiveData<Book> = MutableLiveData()

    val addBookResponse: LiveData<State<DocumentReference>> = switchMap(mutableBook) { book ->
        liveData {
            addBookRepository.addBook(book).collect {
                emit(it)
            }
        }
    }

    fun addBook(book: Book) {
        mutableBook.value = book
    }

}