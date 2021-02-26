package com.example.library.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.library.business.data.cache.abstraction.BookGenderCacheDataSource
import com.example.library.business.data.cache.implementation.BookGenderCacheDataSourceImpl
import com.example.library.business.data.network.abstraction.BookGenderNetworkDataSource
import com.example.library.business.data.network.implementation.BookGenderNetworkDataSourceImpl
import com.example.library.business.interactors.bookgender.*
import com.example.library.framework.datasource.cache.abstraction.BookGenderDaoService
import com.example.library.framework.datasource.cache.implementation.BookGenderDaoServiceImpl
import com.example.library.framework.datasource.cache.mappers.CacheMapperGender
import com.example.library.framework.datasource.network.abstraction.BookGenderRealTimeDb
import com.example.library.framework.datasource.network.implementation.BookGenderRealTimeDbImpl
import com.example.library.framework.datasource.network.mappers.BookGenderNetworkMapper
import com.example.library.persistence.AppDatabase
import com.example.library.persistence.daos.GenderDao
import com.example.library.ui.auth.UserPreferencesRepository
import com.example.library.ui.auth.UserPreferencesRepositoryImpl
import com.example.library.util.Constants.DB_NAME
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

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
    fun provideCacheBookGenderMapper() = CacheMapperGender()

    @Singleton
    @Provides
    fun provideFirebaseDb(): DatabaseReference {
        return Firebase.database.reference
    }

    @Singleton
    @Provides
    fun provideUserPreferencesRepository(@ApplicationContext context: Context): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(context)
    }

    @Singleton
    @Provides
    fun provideBookGenderDaoService(
            genderDao: GenderDao,
            cacheMapperGender: CacheMapperGender
    ): BookGenderDaoService {
        return BookGenderDaoServiceImpl(
                genderDao = genderDao,
                cacheMapperGender = cacheMapperGender
        )
    }

    @Singleton
    @Provides
    fun provideBookGenderCacheDataSource(
            bookGenderDaoService: BookGenderDaoService
    ): BookGenderCacheDataSource {
        return BookGenderCacheDataSourceImpl(
                bookGenderDaoService = bookGenderDaoService
        )
    }

    @Singleton
    @Provides
    fun provideBookGenderRealTimeDb(
            firebaseDb: DatabaseReference,
            bookGenderNetworkMapper: BookGenderNetworkMapper,
            userPreferencesRepository: UserPreferencesRepository
    ): BookGenderRealTimeDb {
        return BookGenderRealTimeDbImpl(
                firebaseDb = firebaseDb,
                bookGenderNetworkMapper = bookGenderNetworkMapper,
                userPreferencesRepository = userPreferencesRepository
        )
    }

    @Singleton
    @Provides
    fun provideBookGenderNetworkDataSource(
            bookGenderRealTimeDb: BookGenderRealTimeDb
    ): BookGenderNetworkDataSource {
        return BookGenderNetworkDataSourceImpl(
                bookGenderRealtimeDb = bookGenderRealTimeDb
        )
    }

    @Singleton
    @Provides
    fun provideBookGenderInteractors(
            bookGenderCacheDataSource: BookGenderCacheDataSource,
            bookGenderNetworkDataSource: BookGenderNetworkDataSource

    ): BookGenderInteractors {
        return BookGenderInteractors(
                getBookGendersFromServer = GetBookGendersFromServer(bookGenderNetworkDataSource),
                getBookGendersFromCache = GetBookGendersFromCache(bookGenderCacheDataSource),
                saveNewBookGender = SaveNewBookGender(
                        bookGenderCacheDataSource,
                        bookGenderNetworkDataSource
                ),
                updateGender = UpdateGender(bookGenderCacheDataSource, bookGenderNetworkDataSource),
                synchronizeRemoteAndLocalGenders = SynchronizeRemoteAndLocalGenders(
                        bookGenderCacheDataSource,
                        bookGenderNetworkDataSource
                ),
                removeGender = RemoveGender(bookGenderCacheDataSource, bookGenderNetworkDataSource)
        )
    }

}