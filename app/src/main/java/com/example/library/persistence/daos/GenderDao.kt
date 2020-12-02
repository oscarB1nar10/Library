package com.example.library.persistence.daos

import androidx.room.*
import com.example.library.models.Gender
import kotlinx.coroutines.flow.Flow

@Dao
interface GenderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gender: Gender): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenders(gender: List<Gender>)

    @Query(
        """
    UPDATE Gender
    SET
    name = :name,
    description = :description
    WHERE pk = :id
"""
    )
    suspend fun updateGender(id: Int, name: String, description: String): Int

    @Delete
    suspend fun delete(gender: Gender): Int

    @Query("SELECT * FROM gender")
    fun get(): Flow<List<Gender>>

    @Query("SELECT * FROM gender")
    suspend fun getForFirebasePurposes(): List<Gender>

    @Query("SELECT * FROM gender WHERE pk = :pk")
    suspend fun searchByPk(pk: Int): Gender?

    @Query("DELETE FROM gender ")
    suspend fun deleteGenders()
}