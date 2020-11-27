package com.zywczas.rickmorty.onlineCharacterListFragment.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.SessionManager
import com.zywczas.rickmorty.onlineCharacterListFragment.domain.OnlineCharacterListRepository
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.onlineCharacterListFragment.utils.OnlineCharacterListResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OnlineCharacterListViewModel @Inject constructor(
    private val repo: OnlineCharacterListRepository,
    private val session: SessionManager
) : ViewModel() {

    private val _characters = MediatorLiveData<OnlineCharacterListResource>()
    val characters: LiveData<OnlineCharacterListResource> = _characters

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
        _characters.postValue(OnlineCharacterListResource.error(msg))
    }


}