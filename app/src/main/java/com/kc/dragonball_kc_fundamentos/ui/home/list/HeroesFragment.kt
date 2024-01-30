package com.kc.dragonball_kc_fundamentos.ui.home.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.kc.dragonball_kc_fundamentos.R
import com.kc.dragonball_kc_fundamentos.databinding.FragmentHeroesListBinding
import com.kc.dragonball_kc_fundamentos.model.Hero
import com.kc.dragonball_kc_fundamentos.placeholder.PlaceholderContent
import com.kc.dragonball_kc_fundamentos.ui.home.HomeInterface
import com.kc.dragonball_kc_fundamentos.ui.home.SharedViewModel

/**
 * A fragment representing a list of Items.
 */
class HeroesFragment(private val token: String, val homeInterface: HomeInterface) : Fragment() {

    private lateinit var binding: FragmentHeroesListBinding
    private val viewSharedViewModel: SharedViewModel by activityViewModels()
    private val heroAdapter = HeroesRecyclerViewAdapter()
    val list = listOf(
        Hero("foo","1", "img"),
        Hero("Bar","2", "img"),
        Hero("Baz","3", "img"),
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHeroesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
        populateList()
        setObservers()
        setListeners()
    }

    private fun configureRecyclerView() {
        //binding.recyclerviewHeroes.layoutManager = GridLayoutManager(this, 2)
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = heroAdapter
    }

    private fun populateList() {
        heroAdapter.updateList(list.shuffled())
    }

    private fun setObservers() {
        // TODO
    }

    private fun setListeners() {
        // TODO
    }

}