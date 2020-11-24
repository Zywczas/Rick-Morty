package com.zywczas.rickmorty.model

import com.zywczas.rickmorty.model.webservice.CharacterFromApi

private const val noInfo = "no info"

suspend fun toCharacter(characterFromApi: CharacterFromApi) =
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
        characterFromApi.created ?: noInfo
    )