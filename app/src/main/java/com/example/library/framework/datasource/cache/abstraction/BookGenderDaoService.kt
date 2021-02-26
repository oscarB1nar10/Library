package com.example.library.framework.datasource.cache.abstraction

import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import kotlinx.coroutines.flow.Flow

interface BookGenderDaoService {

    suspend fun insert(gender: GenderModel): Long

    suspend fun insertGenders(genders: List<GenderModel>)

    suspend fun updateGender(id: Int, name: String?, description: String?, updatedAt: String?): Int

    suspend fun delete(gender: GenderModel): Int

    suspend fun get(): Flow<State<List<GenderModel>>>

    suspend fun getCacheBookGenderAsList(): List<GenderModel>

    suspend fun getForFirebasePurposes(): List<GenderModel>

    suspend fun searchGenderByPk(pk: Int): State<GenderModel>?

    suspend fun getGenderFromQuery(name: String, description: String, updatedAt: String): GenderModel

    suspend fun deleteGenders()

}