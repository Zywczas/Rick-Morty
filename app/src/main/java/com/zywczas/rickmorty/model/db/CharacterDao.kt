package com.zywczas.rickmorty.model.db

import android.database.sqlite.SQLiteConstraintException
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(character : CharacterFromDb) : Long

    @Delete
    suspend fun delete(character : CharacterFromDb) : Int

    @Throws(Exception::class)
    @Query("SELECT * FROM characters")
    fun getCharacters() : Flow<List<CharacterFromDb>>

    @Throws(Exception::class)
    @Query("SELECT COUNT(id) FROM characters WHERE id == :characterId")
    fun getCount(characterId: Int) : Flow<Int>

}