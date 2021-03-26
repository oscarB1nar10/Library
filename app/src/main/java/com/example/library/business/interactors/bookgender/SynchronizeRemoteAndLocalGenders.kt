package com.example.library.business.interactors.bookgender

import com.example.library.business.data.cache.abstraction.BookGenderCacheDataSource
import com.example.library.business.data.network.abstraction.BookGenderNetworkDataSource
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import com.example.library.util.Constants.ERROR_TRYING_TO_SYNCHRONIZE_SERVER_AND_CACHE
class SynchronizeRemoteAndLocalGenders(
    private val bookGenderCacheDataSource: BookGenderCacheDataSource,
    private val bookGenderNetworkDataSource: BookGenderNetworkDataSource
) {

    suspend fun synchronizeRemoteAndLocalGenders(): State<List<GenderModel>> {

        // Get bookgender list from server
        val bookGenderListFromServer = bookGenderNetworkDataSource.get()

        // Get bookGender list from cache
        val bookGenderListFromCache = bookGenderCacheDataSource.getCacheBookGenderAsList()

        return when (bookGenderListFromServer) {
            is State.Success -> {
                val synchronizedGenders = compareEverySingleBookGenderByUpdatedAtDate(
                    serverBookGenderList = bookGenderListFromServer.data,
                    cacheBookGenderList = bookGenderListFromCache
                )
                State.success(synchronizedGenders)
            }
            is State.Failed -> {
                State.failed(ERROR_TRYING_TO_SYNCHRONIZE_SERVER_AND_CACHE)
            }
            else -> {
                State.failed(ERROR_TRYING_TO_SYNCHRONIZE_SERVER_AND_CACHE)
            }
        }
    }

    private suspend fun compareEverySingleBookGenderByUpdatedAtDate(
        serverBookGenderList: List<GenderModel>,
        cacheBookGenderList: List<GenderModel>
    ): List<GenderModel> {

        for (serverBookGender in serverBookGenderList) {
            if (serverBookGender in cacheBookGenderList) {
                val serverBookGenderModelInCache: GenderModel? =
                    cacheBookGenderList.find { it.pk == serverBookGender.pk }
                serverBookGenderModelInCache?.let { cacheBookGender ->
                    if (GenderModel.getUpdatedAtDate(serverBookGender)
                            .after(GenderModel.getUpdatedAtDate(cacheBookGender))
                    ) {
                        // Delete [cacheBookGender] and replace with [serverBookGender]
                        deleteCacheBookGenderAndInsertServerBookGender(
                            serverBookGender,
                            cacheBookGender
                        )
                    } else {
                        updateServerBookGender(cacheBookGender)
                    }
                }
            } else {
                saveServerBookGenderInCache(serverBookGender)
            }
        }

        // get the data from cache at the end (one single source of true)
        return bookGenderCacheDataSource.getCacheBookGenderAsList()
    }

    private suspend fun deleteCacheBookGenderAndInsertServerBookGender(
        serverBookGender: GenderModel,
        cacheBookGender: GenderModel
    ) {
        val response = bookGenderCacheDataSource.delete(cacheBookGender)

        if (response > 0) {
            bookGenderCacheDataSource.insert(serverBookGender)
        }
    }

    private suspend fun updateServerBookGender(cacheBookGender: GenderModel) {
        bookGenderNetworkDataSource.updateGender(cacheBookGender)
    }

    private suspend fun saveServerBookGenderInCache(serverBookGender: GenderModel) {
        bookGenderCacheDataSource.insert(serverBookGender)
    }

}