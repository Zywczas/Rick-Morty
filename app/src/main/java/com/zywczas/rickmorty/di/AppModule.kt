package com.zywczas.rickmorty.di

import android.app.Application
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.model.db.CharacterDao
import com.zywczas.rickmorty.model.db.CharactersDatabase
import com.zywczas.rickmorty.model.webservice.ApiService
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
    fun provideRequestOptions() : RequestOptions = RequestOptions()
        .placeholder(R.drawable.white_background)
        .error(R.drawable.error_image)

    @Provides
    @Singleton
    fun provideGlide(app: Application, options: RequestOptions) : RequestManager =
        Glide.with(app)
            .setDefaultRequestOptions(options)

    @Provides
    @Singleton
    fun provideDispatchers() : Dispatchers = Dispatchers

    @Provides
    @Singleton
    fun provideCharactersDatabase(app: Application) : CharactersDatabase =
        Room.databaseBuilder(app.applicationContext, CharactersDatabase::class.java, "CharactersDB")
            .build()

    @Provides
    @Singleton
    fun provideCharacterDao(db: CharactersDatabase) : CharacterDao = db.getCharacterDao()

}