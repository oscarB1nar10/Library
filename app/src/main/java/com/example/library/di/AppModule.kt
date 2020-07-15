package com.example.library.di

import android.app.Application
import androidx.room.Room
import com.example.library.persistence.AppDatabase
import com.example.library.persistence.daos.GenderDao
import com.example.library.util.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule{


    @JvmStatic
    @Singleton
    @Provides
    fun provideAppDb(app: Application): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration() // get correct db version if schema changed
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideGenderDao(db: AppDatabase): GenderDao {
        return db.genderDao()
    }
}