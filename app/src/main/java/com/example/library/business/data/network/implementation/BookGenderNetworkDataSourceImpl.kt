package com.example.library.business.data.network.implementation

import com.example.library.business.data.network.abstraction.BookGenderNetworkDataSource
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import com.example.library.framework.datasource.network.abstraction.BookGenderRealTimeDb
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookGenderNetworkDataSourceImpl
@Inject
constructor(
        private val bookGenderRealtimeDb: BookGenderRealTimeDb
) : BookGenderNetworkDataSource {

    override suspend fun insert(gender: GenderModel): Flow<State<String>> {
        return bookGenderRealtimeDb.insert(gender)
    }

    override suspend fun insertGenders(genders: List<GenderModel>): Flow<State<String>> {
        return bookGenderRealtimeDb.insertGenders(genders)
    }

    override suspend fun updateGender(genderToUpdate: GenderModel): Flow<State<String>> {
        return bookGenderRealtimeDb.updateGender(genderToUpdate)
    }

    override suspend fun delete(gender: GenderModel): Flow<State<String>> {
        return bookGenderRealtimeDb.delete(gender)
    }

    override suspend fun get(): Flow<State<List<GenderModel>>> {
        return bookGenderRealtimeDb.get()
    }

    override suspend fun getForFirebasePurposes(): List<GenderModel> {
        return bookGenderRealtimeDb.getForFirebasePurposes()
    }

    override suspend fun searchGenderByPk(pk: Int): Flow<State<GenderModel?>>{
        return bookGenderRealtimeDb.searchGenderByPk(pk)
    }

    override suspend fun deleteGenders(): Flow<State<String>> {
        return bookGenderRealtimeDb.deleteGenders()
    }
}