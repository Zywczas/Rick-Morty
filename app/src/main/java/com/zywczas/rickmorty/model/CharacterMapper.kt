package com.zywczas.rickmorty.model

import com.zywczas.rickmorty.model.db.CharacterFromDb
import com.zywczas.rickmorty.model.webservice.CharacterFromApi

private const val noInfo = "no info"

fun toCharacter(characterFromApi: CharacterFromApi) : Character =
    characterFromApi.run {
        Character(
            id ?: 0,
            name ?: noInfo,
            status ?: noInfo,
            species ?: noInfo,
            type ?: noInfo,
            gender ?: noInfo,
            originFromApi?.name ?: noInfo,
            locationFromApi?.name ?: noInfo,
            imageUrl,
            created ?: noInfo
        )
    }


fun toCharacterFromDb(character : Character) : CharacterFromDb =
    character.run {
        CharacterFromDb(id, name, status, species, type, gender, origin, location, imageUrl, created)
    }

fun toCharacter(characterFromDb : CharacterFromDb) : Character =
    characterFromDb.run {
        Character(id, name, status, species, type, gender, origin, location, imageUrl, created)
    }

