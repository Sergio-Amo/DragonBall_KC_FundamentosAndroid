package com.kc.dragonball_kc_fundamentos.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
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
        binding.heroName.text = hero.name
    }

}