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
import com.zywczas.rickmorty.utilities.lazyAndroid
import com.zywczas.rickmorty.utilities.logD
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class ApiVM @Inject constructor(
    private val repo : ApiRepository,
    private val session : SessionManager
) : ViewModel() {

    private var page = 1
    private val charactersList by lazyAndroid { mutableListOf<Character>() }
    private val _characters
            by lazyAndroid { MediatorLiveData<Resource<List<Character>>>() }
    val characters : LiveData<Resource<List<Character>>> by lazyAndroid { _characters }

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
        viewModelScope.launch {
            try {
                val repoResource = repo.downloadCharacters(page)
                charactersList.addAll(repoResource.data!!)
                _characters.value = Resource.success(charactersList)
                page++
            } catch (e: Exception) {
                logD(e)
                updateWithError(R.string.download_error)
            }
        }
    }

    private fun updateWithError(@StringRes msg : Int){
        _characters.postValue(Resource.error(msg, charactersList))
    }


}