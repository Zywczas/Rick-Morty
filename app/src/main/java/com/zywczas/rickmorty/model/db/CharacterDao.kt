package com.zywczas.rickmorty.model.db

import android.database.sqlite.SQLiteConstraintException
import androidx.room.*
import com.zywczas.rickmorty.model.Character
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    //todo to chyba nie musi byc
    @Throws(SQLiteConstraintException::class)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacter(char : Character) : Long

    @Delete
    suspend fun deleteCharacter(char: Character) : Long

    @Throws(Exception::class)
    @Query("SELECT * FROM characters")
    suspend fun getCharacters() : Flow<List<Character>>

    @Throws(Exception::class)
    @Query("SELECT COUNT(id) FROM characters WHERE id == :charId")
    suspend fun getIdCount(charId: Int) : Flow<Int>

}