package com.example.library.business.interactors.bookgender

import com.example.library.business.data.network.abstraction.BookGenderNetworkDataSource
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class GetBookGendersFromServer(
        private val bookGenderNetworkDataSource: BookGenderNetworkDataSource
) {

    suspend fun getBookGendersFromServer() = flow<State<List<GenderModel>>> {

        emit(State.loading())

        val response = bookGenderNetworkDataSource.get()

        response.collect { state ->
            when (state) {
                is State.Loading -> {
                    emit(State.loading())
                }
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