package com.example.library.business.interactors.bookgender

import com.example.library.business.data.cache.abstraction.BookGenderCacheDataSource
import com.example.library.business.data.network.abstraction.BookGenderNetworkDataSource
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import com.example.library.util.Constants.ERROR_TRYING_TO_PERFORM_UPDATE

class UpdateGender(
    private val bookGenderCacheDataSource: BookGenderCacheDataSource,
    private val bookGenderNetworkDataSource: BookGenderNetworkDataSource
) {

    suspend fun updateGender(bookGender: GenderModel): State<String> {
        val response = bookGenderCacheDataSource.updateGender(
            id = bookGender.pk,
            name = bookGender.name,
            description = bookGender.description,
            updatedAt = bookGender.updated_at
        )
        return if (response > 0)
            bookGenderNetworkDataSource.updateGender(bookGender)
        else
            State.failed(ERROR_TRYING_TO_PERFORM_UPDATE)
    }
}