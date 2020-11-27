package com.zywczas.rickmorty.dbmodule

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.zywczas.rickmorty.dbmodule.DbRepository
import javax.inject.Inject

class DbVM @Inject constructor(repo : DbRepository) : ViewModel() {

    val characters = LiveDataReactiveStreams.fromPublisher(repo.getCharactersFromDb())

}