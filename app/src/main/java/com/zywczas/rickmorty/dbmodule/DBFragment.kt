package com.zywczas.rickmorty.dbmodule

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
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
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.SessionManager
import com.zywczas.rickmorty.adapters.CharacterItem
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.dbmodule.utils.DbStatus
import com.zywczas.rickmorty.utilities.showToast
import com.zywczas.rickmorty.factories.UniversalVMFactory
import com.zywczas.rickmorty.utilities.showSnackbar
import kotlinx.android.synthetic.main.fragment_db.*
import javax.inject.Inject

class DBFragment @Inject constructor (
    private val viewModelFactory: UniversalVMFactory,
    private val glide: RequestManager,
    private val session: SessionManager
) : Fragment(R.layout.fragment_db) {

    private val viewModel : DbVM by viewModels { viewModelFactory }
    private val itemAdapter by lazy { ItemAdapter<CharacterItem>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationUI()
        setupRecyclerView()
        setupCharactersObserver()
        checkConnection()
    }

    private fun setupNavigationUI(){
        val navController = findNavController()
        val appBarConfig =
            AppBarConfiguration(setOf(R.id.destination_Db, R.id.destination_Api), drawerLayout_Db)
        navDrawer_Db.setupWithNavController(navController)
        toolbar_Db.setupWithNavController(navController, appBarConfig)
    }

    private fun setupRecyclerView(){
        val fastAdapter = FastAdapter.with(itemAdapter)
        fastAdapter.onClickListener = { _, _, item, _ ->
            goToDetailsFragment(item.character)
            false
        }
        recyclerView_Db.adapter = fastAdapter
        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView_Db.layoutManager = layoutManager
        recyclerView_Db.setHasFixedSize(true)
    }

    private fun goToDetailsFragment(character : Character){
        val directions = DBFragmentDirections.actionToDetails(character)
        findNavController().navigate(directions)
    }

    private fun setupCharactersObserver(){
        viewModel.characters.observe(viewLifecycleOwner){ resource ->
            when (resource.status){
                DbStatus.SUCCESS -> updateUI(resource.data!!)
                DbStatus.ERROR -> resource.message!!.getContentIfNotHandled()?.let { showMessage(it) }
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
        val items = mutableListOf<CharacterItem>()
        characters.forEach {
            val item = CharacterItem(it, glide)
            items.add(item)
        }
        //todo to jest do kitu bo przesuwa mi recycler view na poczatek za kazdym razem, no chyba ze FastAdapter moze to gdzies zablokowac
        FastAdapterDiffUtil[itemAdapter] = items
        complete(true)
    }

    private fun updateInfoAboutEmptyDb(){
        emptyList_TextView_Db.isVisible = itemAdapter.itemList.isEmpty
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