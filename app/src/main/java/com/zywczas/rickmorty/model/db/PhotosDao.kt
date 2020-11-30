package com.zywczas.rickmorty.model.db

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface PhotosDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(photo : PhotoDbEntity) : Long

    @Query("SELECT * FROM photos")
    suspend fun getPhotos() : List<PhotoDbEntity>

}