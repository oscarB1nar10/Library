package com.example.library.framework.datasource.cache.implementation

import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import com.example.library.framework.datasource.cache.abstraction.BookGenderDaoService
import com.example.library.framework.datasource.cache.mappers.CacheMapperGender
import com.example.library.persistence.daos.GenderDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookGenderDaoServiceImpl
@Inject
constructor(
    private val genderDao: GenderDao,
    private val cacheMapperGender: CacheMapperGender
) : BookGenderDaoService {

    override suspend fun insert(gender: GenderModel): Long {
        val bookGenderEntity = cacheMapperGender.mapToEntity(gender)
        return genderDao.insert(bookGenderEntity)
    }

    override suspend fun insertGenders(genders: List<GenderModel>) {
        val bookGenderEntityList = cacheMapperGender.genderListToEntityList(genders)
        return genderDao.insertGenders(bookGenderEntityList)
    }

    override suspend fun updateGender(
        id: Int,
        name: String?,
        description: String?,
        updatedAt: String?
    ): Int {
        return genderDao.updateGender(
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
    ): GenderModel {
        return cacheMapperGender.mapFromEntity(
            genderDao.getGenderFromQuery(
                name = name,
                description = description,
                updatedAt = updatedAt
            )
        )
    }

    override suspend fun delete(gender: GenderModel): Int {
        val bookGenderEntity = cacheMapperGender.mapToEntity(gender)
        return genderDao.delete(bookGenderEntity)
    }

    override suspend fun get() = flow<State<List<GenderModel>>> {

        emit(State.loading())

        genderDao.get().collect { genderCacheEntityList ->
            emit(State.success(cacheMapperGender.entityListToGenderList(genderCacheEntityList)))
        }

    }.catch {
        // If exception is throw , emit failed state along with message
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override suspend fun getCacheBookGenderAsList(): List<GenderModel> {
        return cacheMapperGender.entityListToGenderList(genderDao.getCacheBookGenderAsList())
    }

    override suspend fun getForFirebasePurposes(): List<GenderModel> {
        return arrayListOf()
    }

    override suspend fun searchGenderByPk(pk: Int): State<GenderModel>? {

        return genderDao.searchGenderByPk(pk)?.let {
            State.success(cacheMapperGender.mapFromEntity(it))
        }
    }

    override suspend fun deleteGenders() {
        return genderDao.deleteGenders()
    }

}