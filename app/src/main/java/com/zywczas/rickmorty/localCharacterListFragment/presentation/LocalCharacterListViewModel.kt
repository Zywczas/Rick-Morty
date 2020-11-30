package com.zywczas.rickmorty.localCharacterListFragment.presentation

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.zywczas.rickmorty.localCharacterListFragment.domain.LocalCharacterListRepository
import javax.inject.Inject

class LocalCharacterListViewModel @Inject constructor(repo : LocalCharacterListRepository) : ViewModel() {

    val characters = LiveDataReactiveStreams.fromPublisher(repo.getCharactersFromDb())

}