package com.zywczas.rickmorty.localCharacterListFragment.domain

import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.localCharacterListFragment.utils.*
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.model.db.CharacterDao
import com.zywczas.rickmorty.model.db.CharacterFromDb
import com.zywczas.rickmorty.model.toCharacter
import com.zywczas.rickmorty.utilities.logD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asPublisher
import org.reactivestreams.Publisher
import javax.inject.Inject

class LocalCharacterListRepository @Inject constructor(
    private val dao: CharacterDao
) {

    fun getCharactersFromDb() : Publisher<LocalCharacterListResource> =
        dao.getCharacters()
            .map { LocalCharacterListResource.success(toCharacters(it)) }
            .catch {
                logD(it)
                emit(LocalCharacterListResource.error(R.string.other_database_error))
            }
            .flowOn(Dispatchers.IO)
            .asPublisher()

    private fun toCharacters(charactersFromDb : List<CharacterFromDb>) : List<Character>{
        val characters = mutableListOf<Character>()
        charactersFromDb.forEach {
            val character = toCharacter(it)
            characters.add(character)
        }
        return characters
    }
}