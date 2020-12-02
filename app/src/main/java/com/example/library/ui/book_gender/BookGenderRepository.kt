package com.example.library.ui.book_gender

import com.example.library.models.Gender
import com.example.library.states.State
import kotlinx.coroutines.flow.Flow

interface BookGenderRepository{

    suspend fun saveGender(gender: Gender, userToken: String): Flow<State<Boolean>>

    suspend fun updateGender(gender: Gender, userToken: String): Flow<State<Boolean>>

    suspend fun synchronizeRemoteAndLocalGenders(genders: List<Gender>): Flow<State<List<Gender>>>

    suspend fun getRemoteGenders(userToken: String): Flow<State<List<Gender>>>

    suspend fun removeGender(gender: Gender, userToken: String): Flow<State<Boolean>>
}
