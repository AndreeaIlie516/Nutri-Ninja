//package com.andreeailie.core.data.di
//
//import android.app.Application
//import androidx.room.Room
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.moshi.MoshiConverterFactory
//import java.util.concurrent.TimeUnit
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object CoreModule {
//
//    @Provides
//    @Singleton
//    fun provideOkHttpClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .connectTimeout(300, TimeUnit.SECONDS)
//            .readTimeout(300, TimeUnit.SECONDS)
//            .writeTimeout(300, TimeUnit.SECONDS)
//            .addInterceptor(
//                HttpLoggingInterceptor().apply {
//                    level = HttpLoggingInterceptor.Level.BODY
//                }
//            )
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideNutriNinjaApi(client: OkHttpClient): NutriNinjaApi {
//        return Retrofit.Builder()
//            .baseUrl(NutriNinjaApi.BASE_URL)
//            .addConverterFactory(MoshiConverterFactory.create())
//            .client(client)
//            .build()
//            .create()
//    }
//
//
//    @Provides
//    @Singleton
//    fun provideNutriNinjaDatabase(app: Application): NutriNinjaDatabase {
//        return Room.databaseBuilder(
//            app,
//            NutriNinjaDatabase::class.java,
//            "nutri_ninja_db"
//        ).build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideNutriNinjaRepository(
//        api: NutriNinjaApi,
//        nutriNinjaDatabase: NutriNinjaDatabase,
//    ): NutriNinjaRepository {
//        return NutriNinjaRepositoryImpl(
//            nutriNinjaDao = nutriNinjaDatabase.dao,
//            api = api
//        )
//    }
//}