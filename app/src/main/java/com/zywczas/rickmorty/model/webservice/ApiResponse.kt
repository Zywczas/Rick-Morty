package com.zywczas.rickmorty.model.webservice

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiResponse (

    @SerializedName("results")
    @Expose
    val charactersFromApi: List<CharacterFromApi>?

)


