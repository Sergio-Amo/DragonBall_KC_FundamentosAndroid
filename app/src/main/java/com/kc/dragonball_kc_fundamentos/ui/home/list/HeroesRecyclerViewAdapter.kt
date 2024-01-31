package com.kc.dragonball_kc_fundamentos.ui.home.list

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kc.dragonball_kc_fundamentos.R
import com.kc.dragonball_kc_fundamentos.databinding.FragmentHeroesItemBinding
import com.kc.dragonball_kc_fundamentos.model.Hero

class HeroesRecyclerViewAdapter(val callback: FragmentInterface) :
    RecyclerView.Adapter<HeroesRecyclerViewAdapter.HeroViewHolder>() {

    private var heroes: List<Hero> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {

        return HeroViewHolder(
            FragmentHeroesItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.showHero(heroes[position])
        holder.addListener(heroes[position])
    }

    override fun getItemCount(): Int = heroes.size

    inner class HeroViewHolder(binding: FragmentHeroesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val heroNameView: TextView = binding.heroName
        private val heroImage: ImageView = binding.heroImage
        private val healthBar: ProgressBar = binding.healthBar
        private val root: CardView = binding.root

        fun showHero(hero: Hero) {
            //name
            heroNameView.text = hero.name
            //health
            healthBar.progress = hero.health
            healthBar.max = hero.maxHealth
            healthBar.progressTintList = when {
                hero.health < 26 -> ColorStateList.valueOf(Color.RED)
                hero.health < 51 -> ColorStateList.valueOf(Color.YELLOW)
                else -> ColorStateList.valueOf(Color.GREEN)
            }
            // Image
            Glide
                .with(root)
                .load(hero.photo)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(heroImage)
        }

        fun addListener(hero: Hero) {
            root.setOnClickListener {
                callback.heroClicked(hero)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + heroNameView.text + "'"
        }
    }

    fun updateList(heroes: List<Hero>) {
        this@HeroesRecyclerViewAdapter.heroes = heroes
        notifyDataSetChanged()
    }
}

