package com.zywczas.rickmorty.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CharacterFromDb::class], version = 1)
abstract class CharactersDatabase : RoomDatabase() {

    abstract fun getCharacterDao() : CharacterDao

}