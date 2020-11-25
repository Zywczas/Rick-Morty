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
import com.zywczas.rickmorty.utilities.lazyAndroid
import com.zywczas.rickmorty.viewmodels.DetailsVM
import com.zywczas.rickmorty.viewmodels.UniversalVMFactory
import kotlinx.android.synthetic.main.fragment_api.*
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

class DetailsFragment @Inject constructor(
    private val glide : RequestManager,
    private val viewModelFactory : UniversalVMFactory
) : Fragment(R.layout.fragment_details) {

    private val viewModel : DetailsVM by viewModels { viewModelFactory }
    private val character by lazyAndroid { requireArguments().let { DetailsFragmentArgs.fromBundle(it).character } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCharacterInfo()
        setupObservers()
        setupOnClickListeners()
        setupNavigationUI()
    }

    private fun setupCharacterInfo(){
        character.imageUrl?.let { glide.load(it).into(poster_imageView_Details) }
        name_txtV_Details.text = character.name
        status_txtV_Details.text = "${getString(R.string.status)} ${character.status}"
        species_txtV_Details.text = "${getString(R.string.species)} ${character.species}"
        //todo dokonczyc
    }

    private fun setupObservers(){
        setupAddToDbBtnObserver()
    }

    private fun setupAddToDbBtnObserver(){
        //todo
    }

    private fun setupOnClickListeners(){
        addToDb_btn_Details.setOnClickListener(addToDbBtnClickListener)
    }

    private val addToDbBtnClickListener = View.OnClickListener {
        val isButtonChecked = it.tag as Boolean
//        viewModel.
    }

    private fun setupNavigationUI(){
        val appBarConfig =
            AppBarConfiguration(setOf(R.id.destination_Db, R.id.destination_Api))
        toolbar_details.setupWithNavController(findNavController(), appBarConfig)
    }

}