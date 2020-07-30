package com.example.library.persistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.library.models.Gender
import kotlinx.coroutines.flow.Flow

@Dao
interface GenderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gender: Gender): Long

    @Query("SELECT * FROM gender")
    fun get(): Flow<List<Gender>>
}