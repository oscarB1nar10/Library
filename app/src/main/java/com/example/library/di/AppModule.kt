package com.example.library.di

import android.app.Application
import androidx.room.Room
import com.example.library.persistence.AppDatabase
import com.example.library.persistence.daos.GenderDao
import com.example.library.util.Constants.DB_NAME
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun provideAppDb(app: Application): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration() // get correct db version if schema changed
            .build()
    }

    @Singleton
    @Provides
    fun provideGenderDao(db: AppDatabase): GenderDao {
        return db.genderDao()
    }

    @Singleton
    @Provides
    fun provideFirebaseDb(): DatabaseReference{
        return Firebase.database.reference
    }

}