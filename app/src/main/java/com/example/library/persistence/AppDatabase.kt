package com.example.library.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.library.models.Author
import com.example.library.models.Book
import com.example.library.models.Gender
import com.example.library.models.Owner
import com.example.library.persistence.daos.GenderDao

@Database(entities = [
    Book::class,
    Gender::class,
    Gender::class,
    Author::class,
    Owner::class
], version = 1)
abstract class AppDatabase: RoomDatabase(){

    abstract fun genderDao(): GenderDao
}