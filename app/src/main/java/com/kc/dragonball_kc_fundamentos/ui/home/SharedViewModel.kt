package com.kc.dragonball_kc_fundamentos.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.kc.dragonball_kc_fundamentos.model.Hero
import com.kc.dragonball_kc_fundamentos.model.HeroDto
import com.kc.dragonball_kc_fundamentos.utils.BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class SharedViewModel : ViewModel() {

    private lateinit var token: String
    fun saveToken(token: String) {
        this.token = token
    }

    private val _listState = MutableStateFlow<ListState>(ListState.Idle())
    val listState: StateFlow<ListState> = _listState

    private val heroes = mutableListOf<Hero>()

    sealed class ListState {
        class Idle : ListState()
        class Error(val errorMessage: String) : ListState()
        class Loading : ListState()
        class HeroesLoaded(val heroes: List<Hero>) : ListState()
        class HeroSelected(val hero: Hero) : ListState()
        class HeroUpdated(val hero: Hero) : ListState()
        class HeroIsDead(val hero: Hero) : ListState()
    }

    private val _detailsState = MutableStateFlow<DetailsState>(DetailsState.Idle())
    val detailsState: StateFlow<DetailsState> = _detailsState

    sealed class DetailsState {
        class Idle : DetailsState()
        class Error(val errorMessage: String) : DetailsState()
        class Loading : DetailsState()
        class HeroUpdated : DetailsState()
        class HeroIsDead : DetailsState()
    }

    fun getHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            _listState.value = ListState.Loading()
            val client = OkHttpClient()
            val url = "${BASE_URL}/heros/all"
            val formBody = FormBody.Builder() // response as POST
                .add("name", "")
                .build()
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $token")
                .post(formBody)
                .build()
            val call = client.newCall(request)
            val response = call.execute()
            _listState.value = if (response.isSuccessful)
                response.body?.let {
                    val heroesDtoArray: Array<HeroDto> =
                        Gson().fromJson(it.string(), Array<HeroDto>::class.java)
                    val heroArray = heroesDtoArray.map { hero ->
                        // Create Hero from HeroDto (health and maxHealth will be set to its default)
                        Hero(hero.name, hero.id, hero.photo)
                    }
                    heroes.addAll(heroArray.toList())
                    ListState.HeroesLoaded(heroes)
                } ?: ListState.Error("Empty Token")
            else
                ListState.Error(response.message)
        }
    }

    fun heroClicked(hero: Hero) {
        if(hero.health > 0)
            _listState.value = ListState.HeroSelected(hero)
    }

    fun hitHero(hero: Hero) {
        hero.getHit()
        updateHero(hero)
    }

    fun healHero(hero: Hero) {
        hero.getHeal()
        updateHero(hero)
    }

    private fun updateHero(hero: Hero) {
        if (hero.health == 0) {
            _detailsState.value = DetailsState.HeroIsDead()
            _listState.value = ListState.HeroIsDead(hero)
        } else {
            _detailsState.value = DetailsState.HeroUpdated()
            _listState.value = ListState.HeroUpdated(hero)
        }
    }

    fun resetDetailsState() {
        _detailsState.value = DetailsState.Idle()
    }

    fun healAllHeroes() {
        heroes.map {
            it.health = it.maxHealth
        }
        _listState.value = ListState.HeroesLoaded(heroes)
    }
}