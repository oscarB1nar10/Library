package com.example.library.business.interactors.bookgender

import com.example.library.business.data.cache.abstraction.BookGenderCacheDataSource
import com.example.library.business.data.network.abstraction.BookGenderNetworkDataSource
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class UpdateGender(
        private val bookGenderCacheDataSource: BookGenderCacheDataSource,
        private val bookGenderNetworkDataSource: BookGenderNetworkDataSource
) {

    suspend fun updateGender(bookGender: GenderModel) = flow<State<String>> {
        emit(State.loading())

        val response = bookGenderCacheDataSource.updateGender(
                id = bookGender.pk,
                name = bookGender.name,
                description = bookGender.description,
                updatedAt = bookGender.updated_at
        )

        if (response > 0) {
            val serverResponse = bookGenderNetworkDataSource.updateGender(bookGender)
            serverResponse.collect { state ->
                when (state) {
                    is State.Loading -> {
                    }// Loading state is running
                    is State.Success -> {
                        emit(State.success(state.data))
                    }
                    is State.Failed -> {
                        emit(State.failed(state.message))
                    }
                }

            }
        }
    }
}