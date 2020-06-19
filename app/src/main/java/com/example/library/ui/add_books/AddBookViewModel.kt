package com.example.library.ui.add_books

import androidx.lifecycle.ViewModel
import com.example.library.models.Book
import javax.inject.Inject

class AddBookViewModel
@Inject constructor(
    private val addBookRepository: AddBookRepository
): ViewModel(){

    fun addPost(book: Book) = addBookRepository.addBook(book)
}