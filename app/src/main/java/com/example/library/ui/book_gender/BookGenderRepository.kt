package com.example.library.ui.book_gender

import com.example.library.models.Gender
import com.example.library.states.State
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow


interface BookGenderRepository{

    suspend fun addGender(gender: Gender): Flow<State<DocumentReference>>

    suspend fun addGenderToLocalDB(gender: Gender): Flow<State<Gender>>
}
