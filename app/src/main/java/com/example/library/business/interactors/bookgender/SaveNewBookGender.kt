package com.example.library.business.interactors.bookgender

import com.example.library.business.data.cache.abstraction.BookGenderCacheDataSource
import com.example.library.business.data.network.abstraction.BookGenderNetworkDataSource
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import com.example.library.util.Constants.ERROR_TRYING_TO_PERFORM_UPDATE
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class SaveNewBookGender(
    private val bookGenderCacheDataSource: BookGenderCacheDataSource,
    private val bookGenderNetworkDataSource: BookGenderNetworkDataSource
) {

    suspend fun saveBookGender(bookGender: GenderModel) = flow<State<String>> {

        emit(State.loading())

        val response = bookGenderCacheDataSource.insert(bookGender)

        if (response > 0) {

            // Get the Gender above inserted, the difference is that this have a different id.
            val responseCacheGender = bookGenderCacheDataSource.getGenderFromQuery(
                name = bookGender.name.toString(),
                description = bookGender.description.toString(),
                updatedAt = bookGender.updated_at.toString()
            )

            responseCacheGender?.let { bookGender ->
                insertBookGenderInServer(bookGender).collect { state ->
                    when (state) {
                        is State.Loading -> {
                            emit(State.loading())
                        }
                        is State.Success -> {
                            emit(State.Success(state.data))
                        }
                        is State.Failed -> {
                            emit(State.failed(state.message))
                        }
                    }
                }
            } ?: emit(State.failed(ERROR_TRYING_TO_PERFORM_UPDATE))

        }
    }

    private suspend fun insertBookGenderInServer(bookGender: GenderModel) = flow<State<String>> {
        val serverResponse = bookGenderNetworkDataSource.insert(bookGender)
        serverResponse.collect { state ->
            when (state) {
                is State.Loading -> {
                    emit(State.loading())
                }
                is State.Success -> {
                    emit(State.Success(state.data))
                }
                is State.Failed -> {
                    emit(State.failed(state.message))
                }
            }
        }
    }

}