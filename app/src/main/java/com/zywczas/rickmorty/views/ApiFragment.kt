package com.zywczas.rickmorty.views

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.adapters.CharacterItem
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.utilities.Status
import com.zywczas.rickmorty.utilities.lazyAndroid
import com.zywczas.rickmorty.utilities.showToast
import com.zywczas.rickmorty.viewmodels.ApiVM
import com.zywczas.rickmorty.viewmodels.UniversalVMFactory
import kotlinx.android.synthetic.main.fragment_api.*
import javax.inject.Inject

class ApiFragment @Inject constructor(
    private val viewModelFactory : UniversalVMFactory,
    private val glide : RequestManager
) : Fragment(R.layout.fragment_api) {

    private val viewModel : ApiVM by viewModels { viewModelFactory }
    private val itemAdapter by lazyAndroid { ItemAdapter<CharacterItem>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationUI()
        setupRecyclerView()
        setupObservers()
        if (savedInstanceState == null){
            showProgressBar(true)
            viewModel.getMoreCharacters()
        }
    }

    private fun setupNavigationUI(){
        val navController = findNavController()
        val appBarConfig =
            AppBarConfiguration(setOf(R.id.destination_Db, R.id.destination_Api), drawerLayout_Api)
        navDrawer_Api.setupWithNavController(navController)
        toolbar_Api.setupWithNavController(navController, appBarConfig)
    }

    private fun setupRecyclerView(){
        val fastAdapter = FastAdapter.with(itemAdapter)
        recyclerView_Api.adapter = fastAdapter
        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView_Api.layoutManager = layoutManager
        recyclerView_Api.setHasFixedSize(true)
    }

    private fun setupObservers(){
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

    private fun showProgressBar(isVisible : Boolean){
        progressBar_Api.isVisible = isVisible
    }

    private fun updateUI(characters : List<Character>){
        addToRecyclerView(characters)
    }

//todo dac coroutine
    private fun addToRecyclerView(characters : List<Character>){
        val items = arrayListOf<CharacterItem>()
        characters.forEach {
            val item = CharacterItem(it.id, it.name, it.imageUrl, glide)
            items.add(item)
        }
        itemAdapter.add(items)
    }

    private fun showMessage(msg : Int){
        showToast(getString(msg))
    }

}