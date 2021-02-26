package com.example.library.business.data.cache.implementation

import com.example.library.business.data.cache.abstraction.BookGenderCacheDataSource
import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import com.example.library.framework.datasource.cache.abstraction.BookGenderDaoService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookGenderCacheDataSourceImpl
@Inject
constructor(
    private val bookGenderDaoService: BookGenderDaoService
) : BookGenderCacheDataSource {

    override suspend fun insert(gender: GenderModel): Long {
        return bookGenderDaoService.insert(gender)
    }

    override suspend fun insertGenders(genders: List<GenderModel>) {
        return bookGenderDaoService.insertGenders(genders)
    }

    override suspend fun updateGender(
        id: Int,
        name: String?,
        description: String?,
        updatedAt: String?
    ): Int {
        return bookGenderDaoService.updateGender(
            id,
            name,
            description,
            updatedAt
        )
    }

    override suspend fun getGenderFromQuery(
        name: String,
        description: String,
        updatedAt: String
    ): GenderModel? {
        return bookGenderDaoService.getGenderFromQuery(
            name,
            description,
            updatedAt
        )
    }


    override suspend fun delete(gender: GenderModel): Int {
        return bookGenderDaoService.delete(gender)
    }

    override suspend fun get(): Flow<State<List<GenderModel>>> {
        return bookGenderDaoService.get()
    }

    override suspend fun getCacheBookGenderAsList(): List<GenderModel> {
        return bookGenderDaoService.getCacheBookGenderAsList()
    }

    override suspend fun getForFirebasePurposes(): List<GenderModel> {
        return bookGenderDaoService.getForFirebasePurposes()
    }

    override suspend fun searchGenderByPk(pk: Int): State<GenderModel>? {
        return bookGenderDaoService.searchGenderByPk(pk)
    }

    override suspend fun deleteGenders() {
        return bookGenderDaoService.deleteGenders()
    }

}