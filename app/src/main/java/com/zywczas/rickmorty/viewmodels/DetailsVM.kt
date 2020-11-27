package com.zywczas.rickmorty.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.model.repositories.DetailsEvent
import com.zywczas.rickmorty.model.repositories.DetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailsVM @Inject constructor(
    private val repo: DetailsRepository,
    private val dispatchers: Dispatchers
) : ViewModel() {

    private val _isCharacterInFavourites = MediatorLiveData<Boolean>()
    val isCharacterInFavourites: LiveData<Boolean> = _isCharacterInFavourites

    private val _message = MutableLiveData<DetailsEvent<@StringRes Int>>()
    val message: LiveData<DetailsEvent<Int>> = _message

    suspend fun checkIfIsInList(charId: Int) {
        withContext(dispatchers.IO) {
            _isCharacterInFavourites.postValue(repo.isCharacterInDb(charId))
        }
    }

    suspend fun addOrRemoveCharacterFromFavourites(character: Character, isInFavourites: Boolean) {
        withContext(dispatchers.IO) {
            when (isInFavourites) {
                true -> removeCharacterFromFavourites(character)
                false -> addCharacterToFavourites(character)
            }
        }
    }

    private suspend fun removeCharacterFromFavourites(character: Character) {
        val result = repo.removeCharacterFromDb(character)
        _message.postValue(result)
    }

    private suspend fun addCharacterToFavourites(character: Character) {
        val result = repo.addCharacterToDb(character)
        _message.postValue(result)
    }


}