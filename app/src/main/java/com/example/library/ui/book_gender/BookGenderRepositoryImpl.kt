package com.example.library.ui.book_gender

import com.example.library.models.Gender
import com.example.library.persistence.daos.GenderDao
import com.example.library.states.State
import com.example.library.util.Constants.COLLECTION_BOOKS_GENDER
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookGenderRepositoryImpl
@Inject
constructor(
    private val genderCollection: CollectionReference,
    private val genderDao: GenderDao,
    private val firebaseDb: DatabaseReference
): BookGenderRepository{

    override suspend fun addGender(gender: Gender) = flow<State<DocumentReference>>{

        // Emit loading state
        emit(State.loading())

        val postGenderReference = genderCollection.add(gender).await()

        postGenderReference?.let {
            val rowsAffected = genderDao.insert(gender)
            if(rowsAffected > 0){
                // Emit success state with post reference
                emit(State.success(postGenderReference))
            }
        }

    }.catch {
        // If exception is throw , emit failed state along with message
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override suspend fun getGender() = flow<State<List<Gender>>> {
        // Emit loading state
        emit(State.loading())

        //TODO(Validate networks state to get values from local or remote)
        genderDao.get().collect {
            emit(State.success(it))
        }

    }

    override suspend fun getGendersFromFirebaseDb() = flow<State<List<Gender>>>{

        // Emit loading state
        emit(State.loading())

        val gender = firebaseDb.child(COLLECTION_BOOKS_GENDER).listen<Gender>()

        gender.collect{
            emit(it)
        }

    }.catch {
        // If exception is throw , emit failed state along with message
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    inline fun <reified T> DatabaseReference.listen(): Flow<State<List<T>>> =
        callbackFlow {
            val valueListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    close(databaseError.toException())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        val value = dataSnapshot.getValue<List<T>>()
                        value?.let {
                            offer(State.Success(value))
                        }
                    } catch (exp: Exception) {
                        if (!isClosedForSend) offer(State.failed(exp.message.toString()))
                    }
                }
            }
            addValueEventListener(valueListener)

            awaitClose { removeEventListener(valueListener) }
        }

}