package com.zywczas.rickmorty.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.SessionManager
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.model.repositories.ApiRepoResource
import com.zywczas.rickmorty.model.repositories.ApiRepository
import com.zywczas.rickmorty.utilities.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiVM @Inject constructor(
    private val repo: ApiRepository,
    private val session: SessionManager,
    private val dispatchers: Dispatchers
) : ViewModel() {

    private val _characters = MediatorLiveData<ApiRepoResource<List<Character>>>()
    val characters: LiveData<ApiRepoResource<List<Character>>> = _characters

    private var page = 1

    init {
        viewModelScope.launch { getMoreCharacters() }
    }

    suspend fun getMoreCharacters() {
        withContext(dispatchers.IO){
            if (session.isConnected) {
                getNextPage()
            } else {
                updateWithError(R.string.connection_error)
            }
        }
    }

    private suspend fun getNextPage() {
        val repoResource = repo.downloadCharacters(page)
        _characters.postValue(repoResource)
        page++
    }

    private fun updateWithError(@StringRes msg: Int) {
        _characters.postValue(ApiRepoResource.error(msg))
    }


}