package com.zywczas.rickmorty.views

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.adapters.CharacterItem
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.utilities.Status
import com.zywczas.rickmorty.utilities.logD
import com.zywczas.rickmorty.utilities.showToast
import com.zywczas.rickmorty.viewmodels.ApiVM
import com.zywczas.rickmorty.viewmodels.UniversalVMFactory
import kotlinx.android.synthetic.main.fragment_api.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ApiFragment @Inject constructor(
    private val viewModelFactory : UniversalVMFactory,
    private val glide : RequestManager
) : Fragment(R.layout.fragment_api) {

    private val viewModel : ApiVM by viewModels { viewModelFactory }
    private val itemAdapter by lazy { ItemAdapter<CharacterItem>() }
    private val fastAdapter by lazy { FastAdapter.with(itemAdapter) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar(true)
        setupNavigationUI()
        setupRecyclerView()
        setupCharactersObserver()
    }

    private fun showProgressBar(isVisible : Boolean){
        progressBar_Api.isVisible = isVisible
    }

    private fun setupNavigationUI(){
        val navController = findNavController()
        val appBarConfig =
            AppBarConfiguration(setOf(R.id.destination_Db, R.id.destination_Api), drawerLayout_Api)
        navDrawer_Api.setupWithNavController(navController)
        toolbar_Api.setupWithNavController(navController, appBarConfig)
    }

    private fun setupRecyclerView(){
        setupRvAdapter()
        setupRvLayoutManager()
        setupRvOnScrollListener()
        recyclerView_Api.setHasFixedSize(true)
    }

    private fun setupRvAdapter(){
        fastAdapter.onClickListener = { _, _, item, _ ->
            goToDetailsFragment(item.character)
            false
        }
        recyclerView_Api.adapter = fastAdapter
    }

    private fun setupRvLayoutManager(){
        var spanCount = 2
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            spanCount = 4
        }
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        recyclerView_Api.layoutManager = layoutManager
    }

    private fun setupRvOnScrollListener(){
        val onScrollListener = object : EndlessRecyclerOnScrollListener(){
            override fun onLoadMore(currentPage: Int) {
                showProgressBar(true)
                lifecycleScope.launch { viewModel.getMoreCharacters() }
            }
        }
        recyclerView_Api.addOnScrollListener(onScrollListener)
    }

    private fun goToDetailsFragment(character : Character){
        val directions = ApiFragmentDirections.actionToDetails(character)
        findNavController().navigate(directions)
    }

    private fun setupCharactersObserver(){
        viewModel.characters.observe(viewLifecycleOwner) { resource ->
            showProgressBar(false)
            when (resource.status) {
                Status.SUCCESS -> {
                    updateUI(resource.data!!)
                }
                Status.ERROR -> {
                    resource.data?.let { updateUI(it) }
                    resource.message?.getContentIfNotHandled()?.let { showMessage(it) }
                }
            }
        }
    }

    private fun updateUI(characters : List<Character>){
        addToRecyclerView(characters)
    }

    private fun addToRecyclerView(characters : List<Character>){
        val items = mutableListOf<CharacterItem>()
        characters.forEach {
            val item = CharacterItem(it, glide)
            items.add(item)
        }
        itemAdapter.add(items)
    }

    private fun showMessage(@StringRes msg :  Int){
        showToast(getString(msg))
    }

}