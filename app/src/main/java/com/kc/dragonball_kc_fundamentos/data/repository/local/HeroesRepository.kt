package com.kc.dragonball_kc_fundamentos.data.repository.local

import android.content.Context
import com.google.gson.Gson
import com.kc.dragonball_kc_fundamentos.model.Hero
import com.kc.dragonball_kc_fundamentos.utils.HEROES_VALUE

class HeroesRepository {
    companion object {

        private val gson = Gson()
        fun getHeroes(context: Context): List<Hero>? {
            val heroesString = context.getSharedPreferences(HEROES_VALUE, Context.MODE_PRIVATE)
                .getString(HEROES_VALUE, null)
            if (heroesString.isNullOrEmpty()) return null
            return gson.fromJson(heroesString, Array<Hero>::class.java).toList()
        }

        fun saveHeroes(context: Context, heroes: List<Hero>) {
            context.getSharedPreferences(HEROES_VALUE, Context.MODE_PRIVATE).edit().apply {
                putString(HEROES_VALUE, gson.toJson(heroes))
                apply()
            }
        }
    }
}