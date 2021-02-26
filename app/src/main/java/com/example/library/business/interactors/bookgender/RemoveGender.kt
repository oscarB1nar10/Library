package com.example.library.business.interactors.bookgender

import com.example.library.business.data.cache.abstraction.BookGenderCacheDataSource
import com.example.library.business.data.network.abstraction.BookGenderNetworkDataSource
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import kotlinx.coroutines.flow.flow

class RemoveGender(
        private val bookGenderCacheDataSource: BookGenderCacheDataSource,
        private val bookGenderNetworkDataSource: BookGenderNetworkDataSource
) {

    suspend fun removeGender(bookGender: GenderModel) = flow<State<String>> {

        emit(State.loading())

        val response = bookGenderCacheDataSource.delete(bookGender)

        if (response > 0) {
            bookGenderNetworkDataSource.delete(bookGender)
        }
    }

}