package com.example.library.di.main

import com.example.library.framework.datasource.cache.mappers.CacheMapperGender
import com.example.library.persistence.daos.GenderDao
import com.example.library.util.Constants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Qualifier

@Qualifier
annotation class CollectionBooks

@Qualifier
annotation class CollectionBooksGender

@Module
@InstallIn(ActivityComponent::class)
object MainModule {

    @CollectionBooks
    @Provides
    fun provideFirebaseInstanceCollectionBooks(): CollectionReference{
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_BOOKS)
    }

    @CollectionBooksGender
    @Provides
    fun provideFirebaseInstanceBooksGender(): CollectionReference{
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_BOOKS_GENDER)
    }
}