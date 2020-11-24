package com.zywczas.rickmorty.model.webservice

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




data class OriginFromApi (

    @SerializedName("name")
    @Expose
    val name: String?

)