package com.example.library.business.data.network.abstraction

import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State
import kotlinx.coroutines.flow.Flow

interface BookGenderNetworkDataSource {

    suspend fun insert(gender: GenderModel): Flow<State<String>>

    suspend fun insertGenders(genders: List<GenderModel>): Flow<State<String>>

    suspend fun updateGender(genderToUpdate: GenderModel): Flow<State<String>>

    suspend fun delete(gender: GenderModel): Flow<State<String>>

    suspend fun get(): Flow<State<List<GenderModel>>>

    suspend fun getForFirebasePurposes(): List<GenderModel>

    suspend fun searchGenderByPk(pk: Int): Flow<State<GenderModel?>>

    suspend fun deleteGenders(): Flow<State<String>>
}