package com.example.library.framework.datasource.network.implementation

import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import com.example.library.framework.datasource.network.abstraction.BookGenderRealTimeDb
import com.example.library.framework.datasource.network.mappers.BookGenderNetworkMapper
import com.example.library.framework.datasource.network.utility.FirebaseRealTimeDbHelper.listen
import com.example.library.framework.datasource.network.utility.FirebaseRealTimeDbHelper.valueEventFlow
import com.example.library.ui.auth.UserPreferencesRepository
import com.example.library.ui.auth.UserPreferencesRepositoryImpl
import com.example.library.util.Constants
import com.example.library.util.Constants.DELETE_PERFORMED_SUCCESSFUL
import com.example.library.util.Constants.INSERT_PERFORMED_SUCCESSFUL
import com.example.library.util.Constants.UPDATE_PERFORMED_SUCCESSFUL
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Singleton
class BookGenderRealTimeDbImpl
@Inject
constructor(
        private val firebaseDb: DatabaseReference,
        private val bookGenderNetworkMapper: BookGenderNetworkMapper,
        private val userPreferencesRepository: UserPreferencesRepository
) : BookGenderRealTimeDb {

    lateinit var userToken: String

    init {
            getUserToken()
    }

    private fun getUserToken() {

        val job = SupervisorJob() // Create a scope which will keep reference to its child jobs

        CoroutineScope(Dispatchers.Main + job).launch {
            userPreferencesRepository.userPreferencesFlow.collect(object :
                    FlowCollector<State<UserPreferencesRepositoryImpl.UserPreferences>> {
                override suspend fun emit(value: State<UserPreferencesRepositoryImpl.UserPreferences>) {
                    userToken = when (value) {
                        is State.Success -> {
                            value.data.userToken
                        }
                        else -> ""
                    }
                }

            })
        }
    }

    override suspend fun insert(gender: GenderModel) = flow<State<String>> {

        emit(State.Loading())

        firebaseDb
                .child(Constants.COLLECTION_OWNER)
                .child(userToken)
                .child(Constants.COLLECTION_BOOKS_GENDER)
                .child(gender.pk.toString())
                .setValue(gender)

        firebaseDb
                .child(Constants.COLLECTION_OWNER)
                .child(userToken)
                .child(Constants.COLLECTION_BOOKS_GENDER)
                .child(gender.pk.toString())
                .valueEventFlow(INSERT_PERFORMED_SUCCESSFUL)

    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override suspend fun insertGenders(genders: List<GenderModel>) = flow<State<String>> {
        for (gender in genders) {
            insert(gender)
        }
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override suspend fun updateGender(
            genderToUpdate: GenderModel
    ) = flow {

        firebaseDb
                .child(Constants.COLLECTION_OWNER)
                .child(userToken)
                .child(Constants.COLLECTION_BOOKS_GENDER)
                .child(genderToUpdate.pk.toString())
                .setValue(genderToUpdate)

        firebaseDb
                .child(Constants.COLLECTION_OWNER)
                .child(userToken)
                .child(Constants.COLLECTION_BOOKS_GENDER)
                .child(genderToUpdate.pk.toString())
                .valueEventFlow(UPDATE_PERFORMED_SUCCESSFUL).collect {state ->
                    emit(state)
                }

    }.catch {
        emit(State.failed(it.message.toString()))
    }

    override suspend fun delete(gender: GenderModel) = flow<State<String>> {

        emit(State.Loading())

        firebaseDb
                .child(Constants.COLLECTION_OWNER)
                .child(userToken)
                .child(Constants.COLLECTION_BOOKS_GENDER)
                .child(gender.pk.toString())
                .removeValue()

        firebaseDb
                .child(Constants.COLLECTION_OWNER)
                .child(userToken)
                .child(Constants.COLLECTION_BOOKS_GENDER)
                .child(gender.pk.toString())
                .valueEventFlow(DELETE_PERFORMED_SUCCESSFUL)

    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override suspend fun get() = flow {
        emit(State.Loading())

        val gender = firebaseDb
                .child(Constants.COLLECTION_OWNER)
                .child(userToken)
                .child(Constants.COLLECTION_BOOKS_GENDER).listen<GenderModel>()

        gender.collect {
            emit(it)
        }
    }.catch {
        // If exception is throw , emit failed state along with message
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override suspend fun getForFirebasePurposes(): List<GenderModel> {
        TODO("Not yet implemented")
    }

    override suspend fun searchGenderByPk(pk: Int) = flow<State<GenderModel?>> {
        emit(State.Loading())

        val gender = firebaseDb
                .child(Constants.COLLECTION_OWNER)
                .child(userToken)
                .child(Constants.COLLECTION_BOOKS_GENDER).listen<GenderModel>()

        gender.collect { genderListState ->
            when (genderListState) {
                is State.Loading -> {
                    emit(State.loading())
                }

                is State.Success -> {
                    emit(State.success(genderListState.data.firstOrNull()))
                }

                is State.Failed -> {
                    emit(State.failed(Constants.ERROR_TRYING_TO_GET_REMOTE_GENDER))
                }
            }
        }

    }.catch {
        // If exception is throw , emit failed state along with message
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteGenders() = flow<State<String>> {

        firebaseDb
                .child(Constants.COLLECTION_OWNER)
                .child(userToken)
                .child(Constants.COLLECTION_BOOKS_GENDER)
                .removeValue()


        firebaseDb
                .child(Constants.COLLECTION_OWNER)
                .child(userToken)
                .child(Constants.COLLECTION_BOOKS_GENDER)
                .valueEventFlow(DELETE_PERFORMED_SUCCESSFUL)

    }.catch {
        // If exception is throw , emit failed state along with message
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


}