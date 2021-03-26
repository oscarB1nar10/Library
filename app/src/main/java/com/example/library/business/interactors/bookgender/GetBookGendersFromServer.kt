package com.example.library.business.interactors.bookgender

import com.example.library.business.data.network.abstraction.BookGenderNetworkDataSource
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import com.example.library.util.Constants.ERROR_TRYING_TO_READ_INITIAL_DATA

class GetBookGendersFromServer(
    private val bookGenderNetworkDataSource: BookGenderNetworkDataSource
) {

    suspend fun getBookGendersFromServer(): State<List<GenderModel>> {
        return when (val bookGenderListFromServer = bookGenderNetworkDataSource.get()) {
            is State.Success -> {
                bookGenderListFromServer
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