package com.kc.dragonball_kc_fundamentos.ui.home.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kc.dragonball_kc_fundamentos.databinding.FragmentHeroesListBinding
import com.kc.dragonball_kc_fundamentos.model.Hero
import com.kc.dragonball_kc_fundamentos.ui.home.HomeActivity
import com.kc.dragonball_kc_fundamentos.ui.home.HomeInterface
import com.kc.dragonball_kc_fundamentos.ui.home.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface FragmentInterface {
    fun heroClicked(hero: Hero)
}
class HeroesFragment(private val token: String, private val homeInterface: HomeInterface) :
    Fragment(), FragmentInterface {

    private lateinit var binding: FragmentHeroesListBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val heroAdapter = HeroesRecyclerViewAdapter(this)

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
        binding.healAll.setOnClickListener {
            sharedViewModel.healAllHeroes()
        }
    }

    private fun setObservers() {
        lifecycleScope.launch(Dispatchers.Main) {
            sharedViewModel.listState.collect { state ->
                when (state) {
                    is SharedViewModel.ListState.Idle -> idle()
                    is SharedViewModel.ListState.Error -> showError(state.errorMessage)
                    is SharedViewModel.ListState.Loading -> showLoading(true)
                    is SharedViewModel.ListState.HeroesLoaded -> populateList(state.heroes)
                    is SharedViewModel.ListState.HeroSelected -> showHeroDetails(state.hero)
                    is SharedViewModel.ListState.HeroUpdated -> updateHero(state.hero)
                    is SharedViewModel.ListState.HeroIsDead -> updateHero(state.hero)
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

    private fun updateHero(hero: Hero) {
        heroAdapter.updateHero(hero)
    }

    private fun showHeroDetails(hero: Hero) {
        (activity as? HomeActivity)?.showHeroDetails(hero)
    }

    override fun heroClicked(hero: Hero) {
        sharedViewModel.heroClicked(hero)
    }
}