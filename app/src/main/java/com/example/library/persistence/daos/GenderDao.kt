package com.example.library.persistence.daos

import androidx.room.*
import com.example.library.models.Gender
import kotlinx.coroutines.flow.Flow

@Dao
interface GenderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gender: Gender): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenders(gender: List<Gender>)

    @Query("SELECT * FROM gender")
    fun get(): Flow<List<Gender>>

    @Query("SELECT * FROM gender")
    fun getForFirebasePurposes(): List<Gender>

    @Query("SELECT * FROM gender WHERE pk = :pk")
    fun searchByPk(pk: Int): Gender?

    @Query("DELETE FROM gender ")
    fun deleteGenders()
}