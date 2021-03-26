package com.example.library.business.interactors.bookgender

import com.example.library.business.data.cache.abstraction.BookGenderCacheDataSource
import com.example.library.business.data.network.abstraction.BookGenderNetworkDataSource
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import com.example.library.util.Constants.ERROR_TRYING_TO_PERFORM_DELETE

class RemoveGender(
    private val bookGenderCacheDataSource: BookGenderCacheDataSource,
    private val bookGenderNetworkDataSource: BookGenderNetworkDataSource
) {

    suspend fun removeGender(bookGender: GenderModel): State<String> {
        val response = bookGenderCacheDataSource.delete(bookGender)

        return if (response > 0) {
            bookGenderNetworkDataSource.delete(bookGender)
        } else {
            State.failed(ERROR_TRYING_TO_PERFORM_DELETE)
        }
    }

}