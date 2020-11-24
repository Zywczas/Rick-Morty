package com.zywczas.rickmorty.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Character (

    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("status")
    @Expose
    val status: String,

    @SerializedName("species")
    @Expose
    val species: String,

    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("gender")
    @Expose
    val gender: String,

    @SerializedName("origin")
    @Expose
    val origin: String,

    @SerializedName("location")
    @Expose
    val location: String,

    @SerializedName("image")
    @Expose
    val imageUrl: String?,

    @SerializedName("created")
    @Expose
    val created: String

)