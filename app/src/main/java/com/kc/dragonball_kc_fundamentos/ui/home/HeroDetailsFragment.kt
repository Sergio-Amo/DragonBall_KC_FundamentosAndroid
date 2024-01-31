package com.kc.dragonball_kc_fundamentos.ui.home

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.kc.dragonball_kc_fundamentos.R
import com.kc.dragonball_kc_fundamentos.databinding.FragmentHeroDetailsBinding
import com.kc.dragonball_kc_fundamentos.model.Hero

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
        //setObservers()
        //setListeners()
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