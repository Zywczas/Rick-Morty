package com.zywczas.rickmorty.localCharacterListFragment.domain

import com.zywczas.rickmorty.localCharacterListFragment.utils.*
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.model.db.CharacterDao
import com.zywczas.rickmorty.model.db.CharacterFromDb
import com.zywczas.rickmorty.model.toCharacter
import javax.inject.Inject

class LocalCharacterListRepository @Inject constructor(private val dao: CharacterDao) {

    suspend fun getAllCharacters() : List<Character> {
        val charactersFromDB = dao.getAllCharacters()
        return toCharacters(charactersFromDB)
    }

    private fun toCharacters(charactersFromDb : List<CharacterFromDb>) : List<Character>{
        val characters = mutableListOf<Character>()
        charactersFromDb.forEach {
            val character = toCharacter(it)
            characters.add(character)
        }
        return characters
    }
}