package com.example.library.business.interactors.bookgender

import com.example.library.business.data.cache.abstraction.BookGenderCacheDataSource
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class GetBookGendersFromCache(
        private val bookGenderCacheDataSource: BookGenderCacheDataSource
) {

    suspend fun getBookGendersFromCache() = flow<State<List<GenderModel>>>{

        emit(State.loading())

        val response = bookGenderCacheDataSource.get()

        response.collect { state ->
            when(state){
                is State.Loading -> {}
                is State.Success -> {emit(State.success(state.data))}
                is State.Failed -> {emit(State.failed(state.message))}
            }
        }
    }

}