package com.zywczas.rickmorty.model.repositories

import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.model.CharacterMapper
import com.zywczas.rickmorty.model.webservice.ApiResponse
import com.zywczas.rickmorty.model.webservice.ApiService
import com.zywczas.rickmorty.model.webservice.CharacterFromApi
import com.zywczas.rickmorty.utilities.Resource
import com.zywczas.rickmorty.utilities.logD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiService: ApiService,
    private val dispatchers: Dispatchers,
    private val mapper: CharacterMapper
) {

    suspend fun downloadCharacters(page: Int): Resource<List<Character>> {
        return withContext(dispatchers.IO) {
            val response = apiService.getCharacters(page)
            if (response.isSuccessful) {
                returnCharacters(response)
            } else {
                returnError(response)
            }
        }
    }

    private fun returnCharacters(response: Response<ApiResponse>): Resource<List<Character>> {
        val charactersFromApi = response.body()?.charactersFromApi
        return if (charactersFromApi != null) {
            val characters = convertToCharacters(charactersFromApi)
            Resource.success(characters)
        } else {
            Resource.error(R.string.no_more_pages)
        }
    }

    private fun convertToCharacters(charactersFromApi: List<CharacterFromApi>): List<Character> {
        val characters = arrayListOf<Character>()
        charactersFromApi.forEach { characters.add(mapper.toCharacter(it)) }
        return characters
    }

    private fun returnError(response: Response<ApiResponse>): Resource<List<Character>> {
        return when (response.code()) {
            in 400..499 -> {
                logD("Client error: ${response.code()}. ${response.message()}")
                Resource.error(R.string.app_error)
            }
            in 500..599 -> {
                logD("Server error: ${response.code()}. ${response.message()}")
                Resource.error(R.string.app_error)
            }
            else -> {
                logD("${javaClass.name} error: ${response.code()}. ${response.message()}")
                Resource.error(R.string.download_error)
            }
        }
    }


}