package com.zywczas.rickmorty.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CharacterFromDb::class, PhotoDbEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getCharacterDao() : CharacterDao
    abstract fun getPhotosDao() : PhotosDao

}