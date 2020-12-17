package com.example.library.ui.book_gender

import com.example.library.business.domain.model.GenderModel
import com.example.library.di.main.CollectionBooksGender
import com.example.library.models.GenderCacheEntity
import com.example.library.persistence.daos.GenderDao
import com.example.library.business.domain.states.State
import com.example.library.framework.datasource.cache.mappers.CacheMapperGender
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@FlowPreview
@ExperimentalCoroutinesApi
class BookGenderRepositoryImpl
@Inject
constructor(
    @CollectionBooksGender private val genderCollection: CollectionReference,
    private val genderDao: GenderDao,
    private val cacheMapperGender: CacheMapperGender,
    private val firebaseDb: DatabaseReference
) : BookGenderRepository {

    override suspend fun saveGender(gender: GenderModel, userToken: String) = flow<State<Boolean>> {

        // Emit loading state
        emit(State.loading())

        // Save in local DB
        genderDao.insert(cacheMapperGender.mapToEntity(gender))

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

    override suspend fun getRemoteGenderToUpdate(genderId: Int, userToken: String) = flow {

        emit(State.Loading())

        val gender = firebaseDb
            .child(COLLECTION_OWNER)
            .child(userToken)
            .child(COLLECTION_BOOKS_GENDER).listen<GenderModel>()

        gender.collect {
            emit(it)
        }
    }

    override suspend fun updateGender(
        remoteGender: GenderModel,
        genderToUpdate: GenderModel,
        userToken: String
    ) = flow<State<Boolean>> {

        // Emit loading state
        emit(State.loading())

        if (GenderModel.getUpdatedAtDate(remoteGender)
                .after(GenderModel.getUpdatedAtDate(genderToUpdate))
        ) {

            // Update local gender
            val updated = genderDao.updateGender(
                id = remoteGender.pk,
                name = remoteGender.name,
                description = remoteGender.description,
                updatedAt = remoteGender.updated_at
            )

            if (updated > 0)
                emit(State.success(true))
            else
                emit(State.success(false))

        }else {

            // Update local gender
            val updated = genderDao.updateGender(
                id = genderToUpdate.pk,
                name = genderToUpdate.name,
                description = genderToUpdate.description,
                updatedAt = genderToUpdate.updated_at
            )

            if (updated > 0) {

                val callback = suspendCoroutine<State<Boolean>> {cont ->

                    val dbCompletionListener = DatabaseReference.CompletionListener { error, _ ->
                        if (error == null)
                            cont.resume(State.Success(true))
                        else {
                            cont.resume(State.Success(false))
                        }
                    }

                    // Update remote
                    firebaseDb
                        .child(COLLECTION_OWNER)
                        .child(userToken)
                        .child(COLLECTION_BOOKS_GENDER)
                        .child(genderToUpdate.pk.toString()).setValue(
                            genderToUpdate, dbCompletionListener)

                }

                emit(callback)

            }
        }

    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override suspend fun synchronizeRemoteAndLocalGenders(genders: List<GenderModel>) =
        flow<State<List<GenderModel>>> {

            emit(State.loading())

            saveRemoteGendersIntoLocalDB(cacheMapperGender.genderListToEntityList(genders))

            val gendersDB = genderDao.get()
            gendersDB.collect {
                emit(State.success(cacheMapperGender.entityListToGenderList(it)))
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
            .child(COLLECTION_BOOKS_GENDER).listen<GenderModel>()

        gender.collect {
            emit(it)
        }

    }.catch {
        // If exception is throw , emit failed state along with message
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    override suspend fun removeGender(gender: GenderModel, userToken: String) = flow<State<Boolean>> {
        // Emit loading state
        emit(State.loading())

        // Remove Gender
        val genderDeleted = genderDao.delete(cacheMapperGender.mapToEntity(gender)) == 1

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


    private suspend fun saveRemoteGendersIntoLocalDB(genders: List<GenderCacheEntity>) {
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