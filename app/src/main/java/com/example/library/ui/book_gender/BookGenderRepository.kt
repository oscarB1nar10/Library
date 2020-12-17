package com.example.library.ui.book_gender

import com.example.library.business.domain.model.GenderModel
import com.example.library.models.GenderCacheEntity
import com.example.library.business.domain.states.State
import kotlinx.coroutines.flow.Flow

interface BookGenderRepository{

    suspend fun saveGender(gender: GenderModel, userToken: String): Flow<State<Boolean>>

    suspend fun getRemoteGenderToUpdate(genderId: Int, userToken: String): Flow<State<List<GenderModel>>>

    suspend fun updateGender(remoteGender: GenderModel, genderToUpdate: GenderModel, userToken: String): Flow<State<Boolean>>

    suspend fun synchronizeRemoteAndLocalGenders(genders: List<GenderModel>): Flow<State<List<GenderModel>>>

    suspend fun getRemoteGenders(userToken: String): Flow<State<List<GenderModel>>>

    suspend fun removeGender(gender: GenderModel, userToken: String): Flow<State<Boolean>>
}
