package com.zywczas.rickmorty.model.webservice

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CharacterFromApi (

    @SerializedName("id")
    @Expose
    val id: Int?,

    @SerializedName("name")
    @Expose
    val name: String?,

    @SerializedName("status")
    @Expose
    val status: String?,

    @SerializedName("species")
    @Expose
    val species: String?,

    @SerializedName("type")
    @Expose
    val type: String?,

    @SerializedName("gender")
    @Expose
    val gender: String?,

    @SerializedName("origin")
    @Expose
    val originFromApi: OriginFromApi?,

    @SerializedName("location")
    @Expose
    val locationFromApi: LocationFromApi?,

    @SerializedName("image")
    @Expose
    val imageUrl: String?,

    @SerializedName("created")
    @Expose
    val created: String?,

    @SerializedName("url")
    @Expose
    val characterUrl: String?

)

