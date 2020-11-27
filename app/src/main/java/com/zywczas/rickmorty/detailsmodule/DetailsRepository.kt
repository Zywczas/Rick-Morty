package com.zywczas.rickmorty.detailsmodule

import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.model.db.CharacterDao
import com.zywczas.rickmorty.detailsmodule.utils.DetailsEvent
import com.zywczas.rickmorty.model.toCharacterFromDb
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