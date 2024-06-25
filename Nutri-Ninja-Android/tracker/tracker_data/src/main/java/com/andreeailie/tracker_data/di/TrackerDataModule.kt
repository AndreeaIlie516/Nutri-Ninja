package com.andreeailie.tracker_data.di

import android.app.Application
import androidx.room.Room
import com.andreeailie.tracker_data.local.GroceryDatabase
import com.andreeailie.tracker_data.local.TrackerDatabase
import com.andreeailie.tracker_data.remote.CustomFoodApi
import com.andreeailie.tracker_data.repository.TrackerRepositoryImpl
import com.andreeailie.tracker_domain.repository.TrackerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackerDataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideCustomFoodApi(client: OkHttpClient): CustomFoodApi {
        return Retrofit.Builder()
            .baseUrl(CustomFoodApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create()
    }


    @Provides
    @Singleton
    fun provideTrackerDatabase(app: Application): TrackerDatabase {
        return Room.databaseBuilder(
            app,
            TrackerDatabase::class.java,
            "tracker_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGroceryDatabase(app: Application): GroceryDatabase {
        return Room.databaseBuilder(
            app,
            GroceryDatabase::class.java,
            "grocery_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTrackerRepository(
        api: CustomFoodApi,
        trackerDatabase: TrackerDatabase,
        groceryDatabase: GroceryDatabase
    ): TrackerRepository {
        return TrackerRepositoryImpl(
            trackerDao = trackerDatabase.dao,
            groceryDao = groceryDatabase.dao,
            api = api
        )
    }
}