package com.zywczas.rickmorty.model.webservice

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for contacting with https://rickandmortyapi.com/
 */
interface ApiService {

    @GET("api/character/")
    suspend fun getCharacters(@Query("page") page: Int) : Response<ApiResponse>

}