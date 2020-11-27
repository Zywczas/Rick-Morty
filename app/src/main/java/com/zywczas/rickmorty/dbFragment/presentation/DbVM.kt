package com.zywczas.rickmorty.dbFragment.presentation

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.zywczas.rickmorty.dbFragment.domain.DbRepository
import javax.inject.Inject

class DbVM @Inject constructor(repo : DbRepository) : ViewModel() {

    val characters = LiveDataReactiveStreams.fromPublisher(repo.getCharactersFromDb())

}