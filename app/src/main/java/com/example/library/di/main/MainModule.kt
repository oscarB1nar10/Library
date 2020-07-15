package com.example.library.di.main

import com.example.library.util.Constants
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
object MainModule {

    @JvmStatic
    @MainScope
    @Provides
    @Named(Constants.COLLECTION_BOOKS)
    fun provideFirebaseInstanceCollectionBooks(): CollectionReference{
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_BOOKS)
    }

    @JvmStatic
    @MainScope
    @Provides
    @Named(Constants.COLLECTION_BOOKS_GENDER)
    fun provideFirebaseInstanceBooksGender(): CollectionReference{
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_BOOKS_GENDER)
    }
}