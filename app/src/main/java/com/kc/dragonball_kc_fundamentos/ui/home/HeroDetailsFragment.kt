package com.kc.dragonball_kc_fundamentos.ui.home

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.kc.dragonball_kc_fundamentos.R
import com.kc.dragonball_kc_fundamentos.databinding.FragmentHeroDetailsBinding
import com.kc.dragonball_kc_fundamentos.model.Hero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HeroDetailsFragment(private val hero: Hero, private val homeInterface: HomeInterface) :
    Fragment() {

    private lateinit var binding: FragmentHeroDetailsBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHeroDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showHero()
        setObservers()
        setListeners()
    }

    // Reset detailsState to idle after destroying fragment to prevent triggering HeroIsDead
    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.resetDetailsState()
    }

    private fun setListeners() {
        binding.hitButton.setOnClickListener {
            sharedViewModel.hitHero(hero)
        }
        binding.healButton.setOnClickListener {
            sharedViewModel.healHero(hero)
        }
    }

    private fun setObservers() {
        lifecycleScope.launch(Dispatchers.Main) {
            sharedViewModel.detailsState.collect { state ->
                when (state) {
                    is SharedViewModel.DetailsState.Idle -> idle()
                    is SharedViewModel.DetailsState.Error -> showError(state.errorMessage)
                    is SharedViewModel.DetailsState.Loading -> showLoading(true)
                    is SharedViewModel.DetailsState.HeroUpdated -> updateHero()
                    is SharedViewModel.DetailsState.HeroIsDead -> parentFragmentManager.popBackStack()
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

    private fun updateHero() {
        showHero()
    }

    private fun showHero() {
        //name
        binding.heroName.text = hero.name
        //health
        binding.healthBar.progress = hero.health
        binding.healthBar.max = hero.maxHealth
        binding.healthBar.progressTintList = when {
            hero.health < 26 -> ColorStateList.valueOf(Color.RED)
            hero.health < 51 -> ColorStateList.valueOf(Color.YELLOW)
            else -> ColorStateList.valueOf(Color.GREEN)
        }
        // Image
        Glide
            .with(binding.root)
            .load(hero.photo)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.heroImage)
    }

}