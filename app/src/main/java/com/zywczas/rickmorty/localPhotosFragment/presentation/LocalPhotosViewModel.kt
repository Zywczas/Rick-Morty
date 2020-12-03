package com.zywczas.rickmorty.localPhotosFragment.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zywczas.rickmorty.localPhotosFragment.domain.LocalPhotosRepository
import com.zywczas.rickmorty.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalPhotosViewModel @Inject constructor(private val repo : LocalPhotosRepository) : ViewModel() {

    init {
        viewModelScope.launch { getAllPhotos() }
    }

    private val _message = MutableLiveData<@StringRes Int>()
    val message : LiveData<Int> = _message

    private val _photos = MutableLiveData<List<Photo>>()
    val photos : LiveData<List<Photo>> = _photos

    suspend fun addPhotoToList(photo: Photo){
        withContext(Dispatchers.IO){
            _message.postValue(repo.addPhotoToDb(photo))
        }
    }

    suspend fun getAllPhotos(){
        withContext(Dispatchers.IO){
            _photos.postValue(repo.getAllPhotos())
        }
    }


}