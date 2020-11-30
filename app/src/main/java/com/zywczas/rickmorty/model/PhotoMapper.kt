package com.zywczas.rickmorty.model

import com.zywczas.rickmorty.model.db.PhotoDbEntity

fun toPhoto(photoDbEntity: PhotoDbEntity) =
    photoDbEntity.run { Photo(id, name, timeStamp, photoByteArray) }

fun toPhotoDbEntity(photo: Photo) =
    photo.run { PhotoDbEntity(0, name, timeStamp, photoByteArray) }