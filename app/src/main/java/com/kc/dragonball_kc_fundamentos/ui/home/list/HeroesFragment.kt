package com.kc.dragonball_kc_fundamentos.ui.home.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kc.dragonball_kc_fundamentos.R
import com.kc.dragonball_kc_fundamentos.databinding.FragmentHeroesListBinding
import com.kc.dragonball_kc_fundamentos.model.Hero
import com.kc.dragonball_kc_fundamentos.placeholder.PlaceholderContent
import com.kc.dragonball_kc_fundamentos.ui.home.HomeInterface
import com.kc.dragonball_kc_fundamentos.ui.home.SharedViewModel
import com.kc.dragonball_kc_fundamentos.ui.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class HeroesFragment(private val token: String, private val homeInterface: HomeInterface) :
    Fragment() {

    private lateinit var binding: FragmentHeroesListBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val heroAdapter = HeroesRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Save token in the shared view model
        sharedViewModel.saveToken(token)
        binding = FragmentHeroesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
        getHeroes()
        setObservers()
        setListeners()
    }

    private fun configureRecyclerView() {
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = heroAdapter
    }

    private fun getHeroes() {
        sharedViewModel.getHeroes()
    }

    private fun setListeners() {
        // TODO
    }

    private fun setObservers() {
        lifecycleScope.launch(Dispatchers.Main) {
            sharedViewModel.listState.collect { state ->
                when (state) {
                    is SharedViewModel.ListState.Idle -> idle()
                    is SharedViewModel.ListState.Error -> showError(state.errorMessage)
                    is SharedViewModel.ListState.Loading -> showLoading(true)
                    is SharedViewModel.ListState.HeroesLoaded -> populateList(state.heroes)
                    is SharedViewModel.ListState.HeroSelected -> heroSelected(state.hero)
                }
            }
        }
    }

    private fun idle() {
        showLoading(false)
    }

    private fun showError(error: String) {
        showLoading(false)
        Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
    }

    private fun showLoading(show: Boolean) {
        homeInterface.showLoading(show)
    }

    private fun populateList(heroes: List<Hero>) {
        heroAdapter.updateList(heroes)
        showLoading(false)
    }

    private fun heroSelected(hero: Hero) {
        // TODO: Navigate to hero description
    }
}