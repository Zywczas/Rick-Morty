package com.zywczas.rickmorty.model.repositories

import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.model.db.CharacterDao
import com.zywczas.rickmorty.model.toCharacterFromDb
import com.zywczas.rickmorty.utilities.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.reactivestreams.Publisher
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val dao : CharacterDao
) {

    suspend fun isCharacterInDb(charId : Int) : Boolean =
         dao.getCount(charId)
             .toBoolean()

    private fun Int.toBoolean() : Boolean =
         when (this){
            0 -> false
            else -> true
        }

    suspend fun addCharacterToDb(character : Character) : DetailsEvent<Int> {
            val characterFromDb = toCharacterFromDb(character)
            val result = dao.insert(characterFromDb)
            return if (result == -1L){
                DetailsEvent(R.string.insert_character_error)
            } else {
                DetailsEvent(R.string.insert_character_success)
            }
        }

    suspend fun removeCharacterFromDb(character: Character) : DetailsEvent<Int> {
            val characterFromDb = toCharacterFromDb(character)
            val numberOfRowsRemoved = dao.delete(characterFromDb)
            return if (numberOfRowsRemoved == 0){
                DetailsEvent(R.string.delete_character_error)
            } else {
                DetailsEvent(R.string.delete_character_success)
            }
        }


}