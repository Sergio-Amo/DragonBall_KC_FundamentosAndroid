package com.kc.dragonball_kc_fundamentos.ui.home.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kc.dragonball_kc_fundamentos.R

import com.kc.dragonball_kc_fundamentos.databinding.FragmentHeroesItemBinding
import com.kc.dragonball_kc_fundamentos.model.Hero

class HeroesRecyclerViewAdapter() :
    RecyclerView.Adapter<HeroesRecyclerViewAdapter.HeroViewHolder>() {

    private var heroList: List<Hero> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {

        return HeroViewHolder(
            FragmentHeroesItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.showHero(heroList[position])
        holder.showPosition(position)
        holder.addListener(heroList[position])
    }

    override fun getItemCount(): Int = heroList.size

    inner class HeroViewHolder(binding: FragmentHeroesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val idView: TextView = binding.itemNumber
        private val contentView: TextView = binding.content
        private val root: LinearLayout = binding.root

        fun showPosition(pos: Int) {
            idView.text = pos.toString()
            if (pos % 2 == 0) root.setBackgroundColor(
                ContextCompat.getColor(root.context, R.color.orangeDragonBall)
            )
            else root.setBackgroundColor(
                ContextCompat.getColor(root.context, R.color.white)
            )
        }

        fun showHero(hero: Hero) {
            contentView.text = hero.name

        }

        fun addListener(hero: Hero) {
            root.setOnClickListener {
                // TODO: Clicked!
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    fun updateList(heroList: List<Hero>) {
        this@HeroesRecyclerViewAdapter.heroList = heroList
        // Needed if data has changed
        notifyDataSetChanged()
    }
}

