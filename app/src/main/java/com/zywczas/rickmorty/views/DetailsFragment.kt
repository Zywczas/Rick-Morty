package com.zywczas.rickmorty.views

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.RequestManager
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.utilities.lazyAndroid
import com.zywczas.rickmorty.utilities.logD
import com.zywczas.rickmorty.utilities.showToast
import com.zywczas.rickmorty.viewmodels.DetailsVM
import com.zywczas.rickmorty.viewmodels.UniversalVMFactory
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject
import kotlin.Exception

class DetailsFragment @Inject constructor(
    private val glide : RequestManager,
    private val viewModelFactory : UniversalVMFactory
) : Fragment(R.layout.fragment_details) {

    private val viewModel : DetailsVM by viewModels { viewModelFactory }
    private val character by lazyAndroid { requireArguments().let { DetailsFragmentArgs.fromBundle(it).character } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMessageObserver()
        startUISetupChain()
    }

    private fun setupMessageObserver(){
        viewModel.message.observe(viewLifecycleOwner){event ->
            event.getContentIfNotHandled()?.let { showMessage(it) }
        }
    }

    private fun showMessage(@StringRes msg :  Int){
        //todo pozamieniac na alert dialog
        showToast(getString(msg))
    }

    private fun startUISetupChain(){
        setupUIState{success ->
            if (success){
                setupOnClickListeners()
            }
        }
    }

    private fun setupUIState(complete: (Boolean) -> Unit){
        setupCharacterInfo()
        setupAddToDbBtnState()
        setupNavigationUI()
        complete(true)
    }

    private fun setupCharacterInfo(){
        character.imageUrl?.let { glide.load(it).into(poster_imageView_Details) }
        name_txtV_Details.text = character.name
        status_txtV_Details.text = getString(R.string.status, character.status, character.species)
        type_txtV_Details.text = getString(R.string.type, character.type)
        gender_txtV_Details.text = getString(R.string.gender, character.gender)
        origin_txtV_Details.text = getString(R.string.origin, character.origin)
        location_txtV_Details.text = getString(R.string.location, character.location)
        created_txtV_Details.text = getString(R.string.created, character.created)
    }

    private fun setupAddToDbBtnState(){
        setupAddToDbBtnObserver()
        viewModel.checkIfIsInList(character.id)
    }

    private fun setupAddToDbBtnObserver(){
        viewModel.isCharacterInFavourites.observe(viewLifecycleOwner){
            //todo usunac
            logD("is character in db: $it")
            addToDb_btn_Details.isChecked = it
            addToDb_btn_Details.tag = it
        }
    }

    private fun setupNavigationUI(){
        val appBarConfig =
            AppBarConfiguration(setOf(R.id.destination_Db, R.id.destination_Api))
        toolbar_details.setupWithNavController(findNavController(), appBarConfig)
    }

    private fun setupOnClickListeners(){
        addToDb_btn_Details.setOnClickListener(addToDbBtnClickListener)
    }

    private val addToDbBtnClickListener = View.OnClickListener {
        try {
            val isButtonChecked = it.tag as Boolean
            addOrRemoveCharacterFromFavourites(isButtonChecked)
        } catch (e: Exception) {
            logD(e)
            showMessage(R.string.function_not_working)
        }
    }

    private fun addOrRemoveCharacterFromFavourites(isInFavourites : Boolean){
        viewModel.addOrRemoveCharacterFromFavourites(character, isInFavourites)
    }


}