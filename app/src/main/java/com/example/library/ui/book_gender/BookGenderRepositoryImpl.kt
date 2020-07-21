package com.example.library.ui.book_gender

import com.example.library.models.Gender
import com.example.library.persistence.daos.GenderDao
import com.example.library.states.State
import com.example.library.util.Constants
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class BookGenderRepositoryImpl
@Inject
constructor(
    private val genderCollection: CollectionReference,
    private val genderDao: GenderDao
): BookGenderRepository{

    override suspend fun addGender(gender: Gender) = flow<State<DocumentReference>>{

        // Emit loading state
        emit(State.loading())

        val postGenderReference = genderCollection.add(gender).await()

        // Emit success state with post reference
        emit(State.success(postGenderReference))

    }.catch {
        // If exception is throw , emit failed state along with message
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override suspend fun addGenderToLocalDB(gender: Gender) = flow<State<Gender>> {

        // Emit loading state
        emit(State.loading())

        val rowsAffected = genderDao.insert(gender)
        if (rowsAffected > 0){
            // Emit success state with Gender
            emit(State.success(gender))
        }
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}