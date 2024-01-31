package com.kc.dragonball_kc_fundamentos.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kc.dragonball_kc_fundamentos.databinding.ActivityHomeBinding
import com.kc.dragonball_kc_fundamentos.ui.home.list.HeroesFragment
import com.kc.dragonball_kc_fundamentos.utils.INFO_HOME_ACTIVITY

interface HomeInterface {
    fun showLoading(show: Boolean)
}

class HomeActivity : AppCompatActivity(), HomeInterface {

    companion object {
        fun launchActivity(context: Context, token: String) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(INFO_HOME_ACTIVITY, token)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (intent.getStringExtra(INFO_HOME_ACTIVITY) ?: "").apply {
            showHeroList(this)
        }
    }

    private fun showHeroList(token: String) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, HeroesFragment(token, this))
            //.addToBackStack(null) // Put fragment into back stack
            .commit()
    }

    override fun showLoading(show: Boolean) {
        binding.loadingSpinner.isVisible = show
    }
}