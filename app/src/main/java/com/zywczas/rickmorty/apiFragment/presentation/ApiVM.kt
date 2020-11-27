package com.zywczas.rickmorty.apiFragment.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.SessionManager
import com.zywczas.rickmorty.apiFragment.domain.ApiRepository
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.apiFragment.utils.ApiResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiVM @Inject constructor(
    private val repo: ApiRepository,
    private val session: SessionManager
) : ViewModel() {

    private val _characters = MediatorLiveData<ApiResource<List<Character>>>()
    val characters: LiveData<ApiResource<List<Character>>> = _characters

    private var page = 1

    init {
        viewModelScope.launch { getMoreCharacters() }
    }

    suspend fun getMoreCharacters() {
        withContext(Dispatchers.IO){
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
        _characters.postValue(ApiResource.error(msg))
    }


}