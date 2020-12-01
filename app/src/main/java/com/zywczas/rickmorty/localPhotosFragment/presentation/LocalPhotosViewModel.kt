package com.zywczas.rickmorty.localPhotosFragment.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zywczas.rickmorty.localPhotosFragment.domain.LocalPhotosRepository
import com.zywczas.rickmorty.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalPhotosViewModel @Inject constructor(private val repo : LocalPhotosRepository) : ViewModel() {

    private val _message = MutableLiveData<@StringRes Int>()
    val message : LiveData<Int> = _message

    private val _photos = MutableLiveData<List<Photo>>()
    val photos : LiveData<List<Photo>> = _photos

    suspend fun addPhotoToList(photo: Photo){
        withContext(Dispatchers.IO){
            _message.postValue(repo.addPhotoToDb(photo))
        }
    }

    //todo pytanie do Michała:
    //czy to dobra praktyka jak funkcje w view model, repository i frgamencie nazywaja sie tak samo jezeli robia to samo?
    //np. a funkcja ponizej mogla by sie nazywac tak samo jak z repository funkcja getAllPhotos().
    //Obie robia to samo, tylko ze w swoim wlasnym przedziale, wiec teoretycznie obie moglyby sie nazywac getPhotos()
    //i wtedy w fragmencie tez np dalbym funkcje getPhotos ktora tylko wywoluje viewModel.getPhotos().
    //Ma to sens?
    suspend fun getPhotos(){
        withContext(Dispatchers.IO){
            _photos.postValue(repo.getAllPhotos())
        }
    }


}