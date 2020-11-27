package com.zywczas.rickmorty.model.repositories

import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.model.toCharacter
import com.zywczas.rickmorty.model.webservice.ApiResponse
import com.zywczas.rickmorty.model.webservice.ApiService
import com.zywczas.rickmorty.model.webservice.CharacterFromApi
import com.zywczas.rickmorty.utilities.logD
import retrofit2.Response
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun downloadCharacters(page: Int): ApiResource<List<Character>> {
        val response = apiService.getCharacters(page)
        return if (response.isSuccessful) {
            returnCharacters(response)
        } else {
            returnError(response)
        }
    }

    private fun returnCharacters(response: Response<ApiResponse>): ApiResource<List<Character>> {
        val charactersFromApi = response.body()?.charactersFromApi
        return if (charactersFromApi != null) {
            val characters = convertToCharacters(charactersFromApi)
            ApiResource.success(characters)
        } else {
            ApiResource.error(R.string.no_more_pages)
        }
    }

    private fun convertToCharacters(charactersFromApi: List<CharacterFromApi>): List<Character> {
        val characters = mutableListOf<Character>()
        charactersFromApi.forEach { characters.add(toCharacter(it)) }
        return characters
    }

    private fun returnError(response: Response<ApiResponse>): ApiResource<List<Character>> {
        return when (response.code()) {
            in 400..499 -> {
                logD("Client error: ${response.code()}. ${response.message()}")
                ApiResource.error(R.string.other_api_error)
            }
            in 500..599 -> {
                logD("Server error: ${response.code()}. ${response.message()}")
                ApiResource.error(R.string.other_api_error)
            }
            else -> {
                logD("${javaClass.name} error: ${response.code()}. ${response.message()}")
                ApiResource.error(R.string.download_error)
            }
        }
    }


}