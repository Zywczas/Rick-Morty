package com.zywczas.rickmorty.di

import androidx.fragment.app.Fragment
import com.zywczas.rickmorty.onlineCharacterListFragment.presentation.OnlineCharacterListFragment
import com.zywczas.rickmorty.localCharacterListFragment.presentation.LocalCharacterListFragment
import com.zywczas.rickmorty.detailsFragment.presentation.DetailsFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FragmentFactoryModule {

    @Binds
    @IntoMap
    @FragmentKey(LocalCharacterListFragment::class)
    abstract fun bindDBFragment(fragment: LocalCharacterListFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(OnlineCharacterListFragment::class)
    abstract fun bindApiFragment(fragment: OnlineCharacterListFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(DetailsFragment::class)
    abstract fun bindDetailsFragment(fragment: DetailsFragment) : Fragment

}