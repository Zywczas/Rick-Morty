package com.zywczas.rickmorty.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.RequestManager
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.utilities.Status
import com.zywczas.rickmorty.utilities.logD
import com.zywczas.rickmorty.utilities.showToast
import com.zywczas.rickmorty.viewmodels.ApiVM
import com.zywczas.rickmorty.viewmodels.UniversalVMFactory
import kotlinx.android.synthetic.main.fragment_api.*
import javax.inject.Inject

class ApiFragment @Inject constructor(
    private val viewModelFactory : UniversalVMFactory,
    private val glide: RequestManager
) : Fragment(R.layout.fragment_api) {

    private val viewModel : ApiVM by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationUI()
        setupObservers()
        if (savedInstanceState == null){
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

    private fun setupObservers(){
        viewModel.characters.observe(viewLifecycleOwner) { resource ->
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
        //todo podac do recycler view
        logD(characters[0].name)
        logD(characters[10].name)
        logD(characters[19].name)
    }

    private fun showMessage(msg : Int){
        showToast(getString(msg))
    }

}