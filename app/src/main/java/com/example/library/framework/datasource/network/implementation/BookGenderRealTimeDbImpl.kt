package com.example.library.framework.datasource.network.implementation

import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import com.example.library.framework.datasource.network.abstraction.BookGenderRealTimeDb
import com.example.library.framework.datasource.network.mappers.BookGenderNetworkMapper
import com.example.library.framework.datasource.network.utility.FirebaseRealTimeDbHelper.listen
import com.example.library.framework.datasource.network.utility.FirebaseRealTimeDbHelper.singleValueEvent
import com.example.library.framework.datasource.network.utility.FirebaseRealTimeDbHelper.valueEvenOneShot
import com.example.library.framework.datasource.network.utility.FirebaseRealTimeDbHelper.valueEventFlow
import com.example.library.ui.auth.UserPreferencesRepository
import com.example.library.ui.auth.UserPreferencesRepositoryImpl
import com.example.library.util.Constants
import com.example.library.util.Constants.DELETE_PERFORMED_SUCCESSFUL
import com.example.library.util.Constants.EMPTY_STRING
import com.example.library.util.Constants.ERROR_INITIAL_USER_TOKEN
import com.example.library.util.Constants.ERROR_TRYING_TO_READ_INITIAL_DATA
import com.example.library.util.Constants.INSERT_PERFORMED_SUCCESSFUL
import com.example.library.util.Constants.SERVER_RESPONSE_TIME_OUT
import com.example.library.util.Constants.UPDATE_PERFORMED_SUCCESSFUL
import com.example.library.util.Constants.USER_TOKEN_RETRIEVED_SUCCESSFULLY
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
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

    var userToken: String = EMPTY_STRING

    @JvmName("getUserToken1")
    private suspend fun getUserToken(): String {
        val jobTimeOut = withTimeoutOrNull(SERVER_RESPONSE_TIME_OUT) {
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

        if (jobTimeOut == null)
            withContext(Main) {
                return@withContext ERROR_INITIAL_USER_TOKEN
            }
        else
            withContext(Main) {
                return@withContext USER_TOKEN_RETRIEVED_SUCCESSFULLY
            }

        return USER_TOKEN_RETRIEVED_SUCCESSFULLY
    }

    override suspend fun insert(gender: GenderModel): State<String> {
        firebaseDb
            .child(Constants.COLLECTION_OWNER)
            .child(userToken)
            .child(Constants.COLLECTION_BOOKS_GENDER)
            .child(gender.pk.toString())
            .setValue(gender)

        return firebaseDb
            .child(Constants.COLLECTION_OWNER)
            .valueEvenOneShot(INSERT_PERFORMED_SUCCESSFUL)

    }

    override suspend fun insertGenders(genders: List<GenderModel>) = flow<State<String>> {
        for (gender in genders) {
            insert(gender)
        }
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override suspend fun updateGender(
        genderToUpdate: GenderModel
    ): State<String> {
        firebaseDb
            .child(Constants.COLLECTION_OWNER)
            .child(userToken)
            .child(Constants.COLLECTION_BOOKS_GENDER)
            .child(genderToUpdate.pk.toString())
            .setValue(genderToUpdate)

        return firebaseDb
            .child(Constants.COLLECTION_OWNER)
            .child(userToken)
            .child(Constants.COLLECTION_BOOKS_GENDER)
            .child(genderToUpdate.pk.toString())
            .valueEvenOneShot(UPDATE_PERFORMED_SUCCESSFUL)

    }

    override suspend fun delete(gender: GenderModel): State<String> {
        val deleteGenderResponse = firebaseDb
            .child(Constants.COLLECTION_OWNER)
            .child(userToken)
            .child(Constants.COLLECTION_BOOKS_GENDER)
            .child(gender.pk.toString())
            .valueEvenOneShot(DELETE_PERFORMED_SUCCESSFUL)

        firebaseDb
            .child(Constants.COLLECTION_OWNER)
            .child(userToken)
            .child(Constants.COLLECTION_BOOKS_GENDER)
            .child(gender.pk.toString())
            .removeValue()

        return deleteGenderResponse
    }

    override suspend fun get(): State<List<GenderModel>> {
        var userTokenResponse = userToken

        if (userToken.isEmpty()) {
            userTokenResponse = getUserToken()
        }

        if (userTokenResponse == ERROR_INITIAL_USER_TOKEN)
            return State.failed(ERROR_INITIAL_USER_TOKEN)
        else {
            val genders = firebaseDb
                .child(Constants.COLLECTION_OWNER)
                .child(userToken)
                .child(Constants.COLLECTION_BOOKS_GENDER)
                .singleValueEvent()

            return when (genders) {
                is State.Success -> {
                    val genderList: MutableList<GenderModel> = mutableListOf()
                    genders.data.children.mapNotNullTo(genderList) {
                        it.getValue<GenderModel>(GenderModel::class.java)
                    }
                    State.Success(genderList)
                }
                is State.Failed -> {
                    State.failed(ERROR_TRYING_TO_READ_INITIAL_DATA)
                }
                else -> {
                    State.failed(ERROR_TRYING_TO_READ_INITIAL_DATA)
                }
            }
        }
    }

    override suspend fun getForFirebasePurposes(): List<GenderModel> {
        TODO("Not yet implemented")
    }

    override suspend fun searchGenderByPk(pk: Int): State<GenderModel?> {
        val gender = firebaseDb
            .child(Constants.COLLECTION_OWNER)
            .child(userToken)
            .child(Constants.COLLECTION_BOOKS_GENDER)
            .singleValueEvent()

        return when (gender) {
            is State.Success -> {
                val genderList: MutableList<GenderModel> = mutableListOf()
                gender.data.children.mapNotNullTo(genderList) {
                    it.getValue<GenderModel>(GenderModel::class.java)
                }
                State.Success(genderList.firstOrNull())
            }
            is State.Failed -> {
                State.failed(ERROR_TRYING_TO_READ_INITIAL_DATA)
            }
            else -> {
                State.failed(ERROR_TRYING_TO_READ_INITIAL_DATA)
            }
        }

    }

    override suspend fun deleteGenders() = flow<State<String>> {
        emit(State.Loading())

        firebaseDb
            .child(Constants.COLLECTION_OWNER)
            .child(userToken)
            .child(Constants.COLLECTION_BOOKS_GENDER)
            .valueEventFlow(DELETE_PERFORMED_SUCCESSFUL)

        firebaseDb
            .child(Constants.COLLECTION_OWNER)
            .child(userToken)
            .child(Constants.COLLECTION_BOOKS_GENDER)
            .removeValue()

    }.catch {
        // If exception is throw , emit failed state along with message
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


}