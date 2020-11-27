package com.zywczas.rickmorty.dbFragment.domain

import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.model.db.CharacterDao
import com.zywczas.rickmorty.model.db.CharacterFromDb
import com.zywczas.rickmorty.dbFragment.utils.DbResource
import com.zywczas.rickmorty.model.toCharacter
import com.zywczas.rickmorty.utilities.logD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asPublisher
import org.reactivestreams.Publisher
import javax.inject.Inject

class DbRepository @Inject constructor(
    private val dao: CharacterDao,
    private val dispatchers : Dispatchers
) {

    fun getCharactersFromDb() : Publisher<DbResource<List<Character>>> =
        dao.getCharacters()
            .map { DbResource.success(toCharacters(it)) }
            .catch {
                logD(it)
                emit(DbResource.error(R.string.other_database_error))
            }
            .flowOn(dispatchers.IO)
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