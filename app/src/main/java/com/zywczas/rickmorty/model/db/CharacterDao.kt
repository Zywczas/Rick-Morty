package com.zywczas.rickmorty.model.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(character : CharacterFromDb) : Long

    @Delete
    suspend fun delete(character : CharacterFromDb) : Int

    @Query("SELECT * FROM characters")
    suspend fun getAllCharacters() : List<CharacterFromDb>

    @Query("SELECT COUNT(id) FROM characters WHERE id == :characterId")
    suspend fun getCount(characterId: Long) : Int

}