package com.example.library.ui.add_books

import com.example.library.models.Book
import com.example.library.states.State
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddBookRepository
@Inject
constructor(
    private val bookCollection: CollectionReference
){

    suspend fun addBook(book: Book) = flow<State<DocumentReference>>{

        // Emit loading state
        emit(State.loading())

        val postBookReference = bookCollection.add(book).await()

        // Emit success state with post reference
        emit(State.success(postBookReference))

    }.catch {
        // If exception is throw , emit failed state along with message
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}