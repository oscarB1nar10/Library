package com.example.library.di.main

import com.example.library.persistence.daos.GenderDao
import com.example.library.ui.book_gender.BookGenderRepository
import com.example.library.ui.book_gender.BookGenderRepositoryImpl
import com.example.library.util.Constants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
object MainModule {

    @MainScope
    @Provides
    @Named(Constants.COLLECTION_BOOKS)
    fun provideFirebaseInstanceCollectionBooks(): CollectionReference{
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_BOOKS)
    }

    @MainScope
    @Provides
    @Named(Constants.COLLECTION_BOOKS_GENDER)
    fun provideFirebaseInstanceBooksGender(): CollectionReference{
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_BOOKS_GENDER)
    }

    @MainScope
    @Provides
    fun provideBookGenderRepository(@Named(Constants.COLLECTION_BOOKS_GENDER)
                                    genderCollection: CollectionReference,
                                    genderDao: GenderDao,
                                    firebaseDb: DatabaseReference
    ): BookGenderRepository{
        return BookGenderRepositoryImpl(genderCollection, genderDao, firebaseDb)
    }
}