package com.zywczas.rickmorty.characterDetailsFragment.domain

import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.model.db.CharacterDao
import com.zywczas.rickmorty.model.toCharacterFromDb
import javax.inject.Inject

class CharacterDetailsRepository @Inject constructor(
    private val dao : CharacterDao
) {

    suspend fun isCharacterInDb(charId : Long) : Boolean =
         dao.getCount(charId)
             .toBoolean()

    private fun Int.toBoolean() : Boolean =
         when (this){
            0 -> false
            else -> true
        }

    suspend fun addCharacterToDb(character : Character) : Int {
            val characterFromDb = toCharacterFromDb(character)
            val result = dao.insert(characterFromDb)
            return if (result == -1L){
                R.string.insert_character_error
            } else {
                R.string.insert_character_success
            }
        }

    suspend fun removeCharacterFromDb(character: Character) : Int {
            val characterFromDb = toCharacterFromDb(character)
            val numberOfRowsRemoved = dao.delete(characterFromDb)
            return if (numberOfRowsRemoved == 0){
                R.string.delete_character_error
            } else {
                R.string.delete_character_success
            }
        }


}