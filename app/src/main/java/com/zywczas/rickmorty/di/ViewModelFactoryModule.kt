package com.zywczas.rickmorty.di

import androidx.lifecycle.ViewModel
import com.zywczas.rickmorty.onlineCharacterListFragment.presentation.OnlineCharacterListViewModel
import com.zywczas.rickmorty.localCharacterListFragment.presentation.LocalCharacterListViewModel
import com.zywczas.rickmorty.characterDetailsFragment.presentation.CharacterDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(CharacterDetailsViewModel::class)
    abstract fun bindCharacterDetailsViewModel(vm: CharacterDetailsViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineCharacterListViewModel::class)
    abstract fun bindOnlineCharacterListViewModel(vm: OnlineCharacterListViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocalCharacterListViewModel::class)
    abstract fun bindLocalCharacterListViewModel(vm: LocalCharacterListViewModel) : ViewModel
}