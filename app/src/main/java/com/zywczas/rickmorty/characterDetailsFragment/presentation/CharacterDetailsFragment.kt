package com.zywczas.rickmorty.characterDetailsFragment.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.RequestManager
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.databinding.FragmentDetailsBinding
import com.zywczas.rickmorty.utilities.logD
import com.zywczas.rickmorty.factories.UniversalViewModelFactory
import com.zywczas.rickmorty.utilities.showSnackbar
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterDetailsFragment @Inject constructor(
    private val viewModelFactory : UniversalViewModelFactory
) : Fragment() {

    private val viewModel : CharacterDetailsViewModel by viewModels { viewModelFactory }
    private val args : CharacterDetailsFragmentArgs by navArgs()
    private val character by lazy { args.character }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            characterXML = character
            vm = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMessageObserver()
        startUISetupChain()
    }

    private fun setupMessageObserver(){
        viewModel.message.observe(viewLifecycleOwner){msg ->
            showMessage(msg)
        }
    }

    private fun showMessage(@StringRes msg :  Int){
        showSnackbar(msg)
    }

    private fun startUISetupChain(){
        setupUIState{success ->
            if (success){
                setupOnClickListeners()
            }
        }
    }

    private fun setupUIState(complete: (Boolean) -> Unit){
        setupAddToDbBtnState()
        setupNavigationUI()
        complete(true)
    }

    private fun setupAddToDbBtnState(){
        setupAddToDbBtnObserver()
        lifecycleScope.launch { viewModel.checkIfIsInList(character.id) }
    }

    private fun setupAddToDbBtnObserver(){
        viewModel.isCharacterInFavourites.observe(viewLifecycleOwner){
            addToDb_btn_Details.isChecked = it
            addToDb_btn_Details.tag = it
        }
    }

    private fun setupNavigationUI(){
        toolbar_details.setupWithNavController(findNavController())
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
        lifecycleScope.launch {
            viewModel.addOrRemoveCharacterFromFavourites(character, isInFavourites)
            viewModel.checkIfIsInList(character.id)
        }
    }


}