package com.zywczas.rickmorty.model.db

import android.database.sqlite.SQLiteConstraintException
import androidx.room.*
import com.zywczas.rickmorty.model.Character

@Dao
interface CharacterDao {

    @Throws(SQLiteConstraintException::class)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacter(char : Character) : Long

    @Delete
    suspend fun deleteCharacter(char: Character) : Long

    @Query("SELECT * FROM characters")
    suspend fun getCharacters() :

}