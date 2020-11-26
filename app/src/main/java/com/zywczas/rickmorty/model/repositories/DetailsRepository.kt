package com.zywczas.rickmorty.model.repositories

import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.model.db.CharacterDao
import com.zywczas.rickmorty.model.toCharacterFromDb
import com.zywczas.rickmorty.utilities.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.withContext
import org.reactivestreams.Publisher
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val dao : CharacterDao,
    private val dispatchers : Dispatchers
) {

    fun isCharacterInDb(charId : Int) : Publisher<Boolean> =
         dao.getCount(charId)
            .map { toBoolean(it) }
            .flowOn(dispatchers.IO)
                //todo czy nie dac dispatchers w Publisher
            .asPublisher()

    private fun toBoolean(count: Int) : Boolean =
         when (count){
            0 -> false
            else -> true
        }

    suspend fun addCharacterToDb(character : Character) : Event<Int> =
        withContext(dispatchers.IO){
            val characterFromDb = toCharacterFromDb(character)
            val result = dao.insert(characterFromDb)
            if (result == -1L){
                Event(R.string.insert_character_error)
            } else {
                Event(R.string.insert_character_success)
            }
        }

    suspend fun removeCharacterFromDb(character: Character) : Event<Int> =
        withContext(dispatchers.IO){
            val characterFromDb = toCharacterFromDb(character)
            val numberOfRowsRemoved = dao.delete(characterFromDb)
            if (numberOfRowsRemoved == 0){
                Event(R.string.delete_character_error)
            } else {
                Event(R.string.delete_character_success)
            }
        }


}