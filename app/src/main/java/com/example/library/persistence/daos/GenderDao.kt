package com.example.library.persistence.daos

import androidx.room.*
import com.example.library.models.GenderCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GenderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gender: GenderCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenders(genders: List<GenderCacheEntity>)

    @Query(
        """
    UPDATE Gender
    SET
    name = :name,
    description = :description,
    updated_at = :updatedAt
    WHERE pk = :id
"""
    )
    suspend fun updateGender(id: Int, name: String?, description: String?, updatedAt: String?): Int

    @Delete
    suspend fun delete(gender: GenderCacheEntity): Int

    @Query("SELECT * FROM gender")
    fun get(): Flow<List<GenderCacheEntity>>

    @Query("SELECT * FROM gender")
    suspend fun getForFirebasePurposes(): List<GenderCacheEntity>

    @Query("SELECT * FROM gender WHERE pk = :pk")
    suspend fun searchGenderByPk(pk: Int): GenderCacheEntity?

    @Query("DELETE FROM gender ")
    suspend fun deleteGenders()
}