package com.zywczas.rickmorty.localPhotosFragment.domain

import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.model.Photo
import com.zywczas.rickmorty.model.db.PhotoDbEntity
import com.zywczas.rickmorty.model.db.PhotosDao
import com.zywczas.rickmorty.model.toPhoto
import com.zywczas.rickmorty.model.toPhotoDbEntity
import javax.inject.Inject

class LocalPhotosRepository @Inject constructor(private val dao: PhotosDao) {

    suspend fun addPhotoToDb(photo : Photo) : Int {
        val result = dao.insert(toPhotoDbEntity(photo))
        return if (result == -1L){
            R.string.operation_error
        } else {
            R.string.operation_success
        }
    }

    suspend fun getAllPhotos() : List<Photo> {
        val photosDbEntities = dao.getPhotos()
        return toPhotos(photosDbEntities)
    }

    private fun toPhotos(photosDbEntities: List<PhotoDbEntity>) : List<Photo>{
        val photos = mutableListOf<Photo>()
        photosDbEntities.forEach { photos.add(toPhoto(it)) }
        return photos
    }


}