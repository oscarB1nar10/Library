package com.example.library.business.interactors.bookgender

import com.example.library.business.data.cache.abstraction.BookGenderCacheDataSource
import com.example.library.business.data.network.abstraction.BookGenderNetworkDataSource
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import com.example.library.util.Constants.ERROR_TRYING_TO_PERFORM_DELETE
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class RemoveGender(
    private val bookGenderCacheDataSource: BookGenderCacheDataSource,
    private val bookGenderNetworkDataSource: BookGenderNetworkDataSource
) {

    suspend fun removeGender(bookGender: GenderModel) = flow<State<String>> {

        emit(State.loading())

        val response = bookGenderCacheDataSource.delete(bookGender)

        if (response > 0) {
            val serverResponse = bookGenderNetworkDataSource.delete(bookGender)
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
        } else {
            emit(State.failed(ERROR_TRYING_TO_PERFORM_DELETE))
        }
    }

}