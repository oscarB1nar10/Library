package com.example.library.framework.datasource.network.abstraction

import com.example.library.business.domain.model.GenderModel
import com.example.library.business.domain.states.State

import kotlinx.coroutines.flow.Flow

interface BookGenderRealTimeDb {

    suspend fun insert(gender: GenderModel): State<String>

    suspend fun insertGenders(genders: List<GenderModel>): Flow<State<String>>

    suspend fun updateGender(genderToUpdate: GenderModel): State<String>

    suspend fun delete(gender: GenderModel): State<String>

    suspend fun get(): State<List<GenderModel>>

    suspend fun getForFirebasePurposes(): List<GenderModel>

    suspend fun searchGenderByPk(pk: Int): State<GenderModel?>

    suspend fun deleteGenders(): Flow<State<String>>

}