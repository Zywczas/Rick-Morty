package com.zywczas.rickmorty.di

import androidx.fragment.app.Fragment
import com.zywczas.rickmorty.onlineCharacterListFragment.presentation.OnlineCharacterListFragment
import com.zywczas.rickmorty.localCharacterListFragment.presentation.LocalCharacterListFragment
import com.zywczas.rickmorty.characterDetailsFragment.presentation.CharacterDetailsFragment
import com.zywczas.rickmorty.localPhotos.presentation.LocalPhotosFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FragmentFactoryModule {

    @Binds
    @IntoMap
    @FragmentKey(LocalCharacterListFragment::class)
    abstract fun bindLocalCharacterListFragment(fragment: LocalCharacterListFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(OnlineCharacterListFragment::class)
    abstract fun bindOnlineCharacterListFragment(fragment: OnlineCharacterListFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(CharacterDetailsFragment::class)
    abstract fun bindCharacterDetailsFragment(fragment: CharacterDetailsFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(LocalPhotosFragment::class)
    abstract fun bindLocalPhotosFragment(fragment: LocalPhotosFragment) : Fragment

}