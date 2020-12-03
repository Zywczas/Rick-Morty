package com.zywczas.rickmorty.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.model.db.CharacterDao
import com.zywczas.rickmorty.model.db.AppDatabase
import com.zywczas.rickmorty.model.db.PhotosDao
import com.zywczas.rickmorty.model.webservice.ApiService
import com.zywczas.rickmorty.utilities.glideRequestOptions
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object AppModule {

    private const val baseUrl = "https://rickandmortyapi.com/"

    @Provides
    @Singleton
    fun provideAppContext(app: Application) : Context = app.applicationContext

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideRestApiService(retrofit: Retrofit) : ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideGlide(context : Context) : RequestManager =
        Glide.with(context)
            .setDefaultRequestOptions(glideRequestOptions)

    @Provides
    @Singleton
    fun provideDispatchers() : Dispatchers = Dispatchers

    @Provides
    @Singleton
    fun provideCharactersDatabase(context : Context) : AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "CharactersDB")
            .build()

    @Provides
    @Singleton
    fun provideCharacterDao(db: AppDatabase) : CharacterDao = db.getCharacterDao()

    @Provides
    @Singleton
    fun providePhotosDao(db: AppDatabase) : PhotosDao = db.getPhotosDao()

}