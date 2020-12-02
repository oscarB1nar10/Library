package com.example.library.ui.book_gender

import com.example.library.di.main.CollectionBooksGender
import com.example.library.models.Gender
import com.example.library.persistence.daos.GenderDao
import com.example.library.states.State
import com.example.library.util.Constants.COLLECTION_BOOKS_GENDER
import com.example.library.util.Constants.COLLECTION_OWNER
import com.example.library.util.Constants.ERROR_TRYING_TO_PERFORM_UPDATE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class BookGenderRepositoryImpl
@Inject
constructor(
    @CollectionBooksGender private val genderCollection: CollectionReference,
    private val genderDao: GenderDao,
    private val firebaseDb: DatabaseReference
) : BookGenderRepository {

    override suspend fun saveGender(gender: Gender, userToken: String) = flow<State<Boolean>> {

        // Emit loading state
        emit(State.loading())

        // Save in local DB
        genderDao.insert(gender)

        // Get the last one Gender (The above), the difference is the ID
        val gendersDB = genderDao.getForFirebasePurposes()
        val genderToUpdate = gendersDB.last()

        firebaseDb
            .child(COLLECTION_OWNER)
            .child(userToken)
            .child(COLLECTION_BOOKS_GENDER)
            .child(genderToUpdate.pk.toString()).setValue(genderToUpdate)

        emit(State.success(true))

    }.catch {
        // If exception is throw , emit failed state along with message
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override suspend fun updateGender(gender: Gender, userToken: String) = flow<State<Boolean>> {

        // Emit loading state
        emit(State.loading())

        // Update Gender
        val updated = genderDao.updateGender(
            id = gender.pk,
            name = gender.name ?: "",
            description = gender.description ?: ""
        )

        if (updated > 0) {
            firebaseDb
                .child(COLLECTION_OWNER)
                .child(userToken)
                .child(COLLECTION_BOOKS_GENDER)
                .child(gender.pk.toString()).setValue(gender)

            emit(State.success(true))
        } else {
            emit(State.failed(ERROR_TRYING_TO_PERFORM_UPDATE))
            emit(State.success(false))
        }

    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override suspend fun synchronizeRemoteAndLocalGenders(genders: List<Gender>) =
        flow<State<List<Gender>>> {

            emit(State.loading())

            saveRemoteGendersIntoLocalDB(genders)

            val gendersDB = genderDao.get()
            gendersDB.collect {
                emit(State.success(it))
            }

            emit(State.success(genders))

        }.catch {
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    override suspend fun getRemoteGenders(userToken: String) = flow {

        // Emit loading state
        emit(State.loading())

        val gender = firebaseDb
            .child(COLLECTION_OWNER)
            .child(userToken)
            .child(COLLECTION_BOOKS_GENDER).listen<Gender>()

        gender.collect {
            emit(it)
        }

    }.catch {
        // If exception is throw , emit failed state along with message
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override suspend fun removeGender(gender: Gender, userToken: String) = flow<State<Boolean>> {
        // Emit loading state
        emit(State.loading())

        // Remove Gender
        val genderDeleted = genderDao.delete(gender) == 1

        firebaseDb
            .child(COLLECTION_OWNER)
            .child(userToken)
            .child(COLLECTION_BOOKS_GENDER)
            .child(gender.pk.toString()).removeValue()

        if (genderDeleted) emit(State.success(genderDeleted)) else emit(State.success(genderDeleted))

    }.catch {
        // If exception is throw , emit failed state along with message
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    private suspend fun saveRemoteGendersIntoLocalDB(genders: List<Gender>) {
        genderDao.deleteGenders()
        genderDao.insertGenders(genders)
    }


    @ExperimentalCoroutinesApi
    inline fun <reified T : Any> DatabaseReference.listen(): Flow<State<List<T>>> =
        callbackFlow {
            val valueListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    close(databaseError.toException())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        val listT: MutableList<T> = mutableListOf()
                        dataSnapshot.children.mapNotNullTo(listT) {
                            it.getValue<T>(T::class.java)
                        }

                        offer(State.Success(listT))

                    } catch (exp: Exception) {
                        if (!isClosedForSend) offer(State.failed(exp.message.toString()))
                    }
                }
            }
            addListenerForSingleValueEvent(valueListener)

            awaitClose { removeEventListener(valueListener) }
        }

}