package com.example.library.business.interactors.bookgender

import com.example.library.business.data.cache.abstraction.BookGenderCacheDataSource
import com.example.library.business.data.network.abstraction.BookGenderNetworkDataSource
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import com.example.library.util.Constants.ERROR_TRYING_TO_PERFORM_INSERT

class SaveNewBookGender(
    private val bookGenderCacheDataSource: BookGenderCacheDataSource,
    private val bookGenderNetworkDataSource: BookGenderNetworkDataSource
) {

    suspend fun saveBookGender(bookGender: GenderModel): State<String> {
        val response = bookGenderCacheDataSource.insert(bookGender)

        return if (response > 0) {

            // Get the Gender above inserted, the difference is that this have a different id.
            val responseCacheGender = bookGenderCacheDataSource.getGenderFromQuery(
                name = bookGender.name.toString(),
                description = bookGender.description.toString(),
                updatedAt = bookGender.updated_at.toString()
            )

            responseCacheGender?.let { cacheGender ->
                bookGenderNetworkDataSource.insert(cacheGender)
            } ?: State.failed(ERROR_TRYING_TO_PERFORM_INSERT)
        } else {
            State.failed(ERROR_TRYING_TO_PERFORM_INSERT)
        }
    }
}