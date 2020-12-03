package com.zywczas.rickmorty.localCharacterListFragment.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.SessionManager
import com.zywczas.rickmorty.localCharacterListFragment.adapter.LocalCharacterListItem
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.localCharacterListFragment.utils.LocalCharacterListStatus
import com.zywczas.rickmorty.factories.UniversalViewModelFactory
import com.zywczas.rickmorty.utilities.attachAppBarConfiguration
import com.zywczas.rickmorty.utilities.showSnackbar
import kotlinx.android.synthetic.main.fragment_local_character_list.*
import javax.inject.Inject

class LocalCharacterListFragment @Inject constructor (
    private val viewModelFactory: UniversalViewModelFactory,
    private val glide: RequestManager,
    private val session: SessionManager
) : Fragment(R.layout.fragment_local_character_list) {

    private val viewModel : LocalCharacterListViewModel by viewModels { viewModelFactory }
    private val itemAdapter by lazy { ItemAdapter<LocalCharacterListItem>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationUI()
        setupRecyclerView()
        setupCharactersObserver()
        checkConnection()
    }

    private fun setupNavigationUI(){
        val navController = findNavController()
        val appBarConfig = drawerLayout_localCharacterList.attachAppBarConfiguration()
        navDrawer_localCharacterList.setupWithNavController(navController)
        toolbar_localCharacterList.setupWithNavController(navController, appBarConfig)
    }

    private fun setupRecyclerView(){
        setupRvAdapter()
        setupRvLayoutManager()
        recyclerView_localCharacterList.setHasFixedSize(true)
    }

    private fun setupRvAdapter(){
        val fastAdapter = FastAdapter.with(itemAdapter)
        fastAdapter.onClickListener = { _, _, item, _ ->
            goToDetailsFragment(item.character)
            false
        }
        recyclerView_localCharacterList.adapter = fastAdapter
    }

    private fun setupRvLayoutManager(){
        var spanCount = 2
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            spanCount = 4
        }
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        recyclerView_localCharacterList.layoutManager = layoutManager
    }

    private fun goToDetailsFragment(character : Character){
        val directions = LocalCharacterListFragmentDirections.actionToDetails(character)
        findNavController().navigate(directions)
    }

    private fun setupCharactersObserver(){
        viewModel.characters.observe(viewLifecycleOwner){ resource ->
            when (resource.status){
                LocalCharacterListStatus.SUCCESS -> updateUI(resource.data!!)
                LocalCharacterListStatus.ERROR -> resource.message?.getContentIfNotHandled()?.let { showMessage(it) }
            }
        }
    }

    private fun updateUI(characters : List<Character>){
        addToRecyclerView(characters){finished ->
            if (finished){
                updateInfoAboutEmptyDb()
            }
        }
    }

    private fun addToRecyclerView(characters : List<Character>, complete: (Boolean) -> Unit){
        val items = mutableListOf<LocalCharacterListItem>()
        characters.forEach {
            val item = LocalCharacterListItem(it, glide)
            items.add(item)
        }
        FastAdapterDiffUtil[itemAdapter] = items
        complete(true)
    }

    private fun updateInfoAboutEmptyDb(){
        emptyList_TextView_localCharacterList.isVisible = itemAdapter.itemList.isEmpty
    }

    private fun showMessage(@StringRes msg :  Int){
        showSnackbar(msg)
    }

    private fun checkConnection(){
        if (session.isConnected.not()){
            showMessage(R.string.connection_error)
        }
    }

}