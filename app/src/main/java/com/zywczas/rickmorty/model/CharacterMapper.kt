package com.zywczas.rickmorty.model

import com.bumptech.glide.RequestManager
import com.zywczas.rickmorty.model.webservice.CharacterFromApi
import javax.inject.Inject

class CharacterMapper @Inject constructor(private val glide : RequestManager) {

    private val noInfo = "no info"

    fun toCharacter(characterFromApi: CharacterFromApi) =
        Character(
            characterFromApi.id ?: 0,
            characterFromApi.name ?: noInfo,
            characterFromApi.status ?: noInfo,
            characterFromApi.species ?: noInfo,
            characterFromApi.type ?: noInfo,
            characterFromApi.gender ?: noInfo,
            characterFromApi.originFromApi?.name ?: noInfo,
            characterFromApi.locationFromApi?.name ?: noInfo,
            characterFromApi.imageUrl,
            characterFromApi.created ?: noInfo,
            glide
        )
}

