package com.example.library.di.main

import com.example.library.util.Constants
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
object MainModule {

    @JvmStatic
    @MainScope
    @Provides
    fun provideFirebaseInstance(): CollectionReference{
        return FirebaseFirestore.getInstance().collection(Constants.COLLECTION_BOOKS)
    }
}