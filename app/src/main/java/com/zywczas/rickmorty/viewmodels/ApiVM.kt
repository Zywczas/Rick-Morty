package com.zywczas.rickmorty.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.SessionManager
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.model.repositories.ApiRepository
import com.zywczas.rickmorty.utilities.Resource
import com.zywczas.rickmorty.utilities.logD
import kotlinx.coroutines.launch
import javax.inject.Inject

class ApiVM @Inject constructor(
    private val repo : ApiRepository,
    private val session : SessionManager
) : ViewModel() {

    private val _characters = MediatorLiveData<Resource<List<Character>>>()
    val characters : LiveData<Resource<List<Character>>> = _characters

    private var page = 1

    init {
        getMoreCharacters()
    }

    fun getMoreCharacters(){
        if (session.isConnected){
            getNextPage()
        }
        else {
            updateWithError(R.string.connection_error)
        }
    }

    private fun getNextPage(){
        //todo pozamieniac viewmodelscope na dispatchers.IO
        viewModelScope.launch {
            try {
                val repoResource = repo.downloadCharacters(page)
                _characters.value = repoResource
                page++
            } catch (e: Exception) {
                logD(e)
                updateWithError(R.string.download_error)
            }
        }
    }

    private fun updateWithError(@StringRes msg : Int){
        _characters.postValue(Resource.error(msg))
    }


}